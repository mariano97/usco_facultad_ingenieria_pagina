import { DATE_FORMAT } from '@/shared/date/filters';
import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';
import PaisesService from '@/entities/paises/paises.service';
import ProfesorService from '@/entities/profesor/profesor.service';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import TituloAcademicoProfesorService from '@/entities/titulo-academico-profesor/titulo-academico-profesor.service';
import AlertService from '@/shared/alert/alert.service';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { ITituloAcademicoProfesor } from '@/shared/model/titulo-academico-profesor.model';
import { IUsuarioProfesorFull, UsuarioProfesorFull } from '@/shared/model/usuario-profesor.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UsuarioProfesorFullService from '@/shared/services/usuario-profesor.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './profesor-info.scss';
import { ICursoMateria } from '@/shared/model/curso-materia.model';

@Component
export default class ProfesorInfo extends Vue {
  @Inject('profesorService') private profesorService: () => ProfesorService;
  @Inject('usuarioProfesorFullService') private usuarioProfesorFullService: () => UsuarioProfesorFullService;
  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('tituloAcademicoProfesorService') private tituloAcademicoProfesorService: () => TituloAcademicoProfesorService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('cursoMateriaService') private cursoMateriaService: () => CursoMateriaService;
  @Inject('paisesService') private paisesService: () => PaisesService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('utilsService') private utilsService: () => UtilsService;

  private tabNameActive = 'PERFIL';

  public usuarioProfesor: IUsuarioProfesorFull = {
    adminUserDTO: {},
    profesorDTO: {},
  };

  public titulosAcademicosProfesorLista: ITituloAcademicoProfesor[][] = [];
  public cursosMateriasProfesorLista: ICursoMateria[][] = [];

  beforeRouteEnter (to, from, next) {
    next(vm => {
      if (to.params.userLogin) {
        vm.consultarUsuarioProfesor(to.params.userLogin);
      }
    });
  }

  private consultarUsuarioProfesor(userLogin: string): void {
    this.usuarioProfesorFullService()
      .getOneUsuarioProfesorByUserLogin(this.$store.getters.authenticated, userLogin)
      .then(res => {
        this.usuarioProfesor = res;
        this.downloadImage();
        this.consultarTitulosAcademicosProfesor(this.usuarioProfesor.profesorDTO.id);
        this.consultarCursosMateriaProfesor(this.usuarioProfesor.profesorDTO.id);
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  private consultarTitulosAcademicosProfesor(profesorId: number): void {
    this.titulosAcademicosProfesorLista = [];
    this.tituloAcademicoProfesorService()
      .findAllByProfesorId(this.$store.getters.authenticated, profesorId)
      .then(res => {
        res.sort((a, b) => (a.yearTitulo > b ? -1 : 1));
        this.agruparTitulosAcademicos(res);
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  private consultarCursosMateriaProfesor(profesorId: number): void {
    this.usuarioProfesor.profesorDTO.cursoMaterias = [];
    this.cursoMateriaService()
      .findAllByProfesorId(this.$store.getters.authenticated, profesorId)
      .then(res => {
        this.usuarioProfesor.profesorDTO.cursoMaterias = res;
        this.agruparCursosMaterias(res);
      })
      .catch(err => {
        this.usuarioProfesor.profesorDTO.cursoMaterias = [];
      });
  }

  private downloadImage(): void {
    if (
      this.usuarioProfesor.profesorDTO &&
      this.usuarioProfesor.profesorDTO.urlFotoProfesor &&
      !this.utilsService().existeFileInList(this.usuarioProfesor.profesorDTO.urlFotoProfesor)
    ) {
      this.googleStorageService()
        .downloadFileByOnlyFileName(this.$store.getters.authenticated, this.usuarioProfesor.profesorDTO.urlFotoProfesor)
        .then(res => {
          this.utilsService().agregarFileToList(this.usuarioProfesor.profesorDTO.urlFotoProfesor, res);
        });
    }
  }

  public obtenerImagen(fileName: string): any {
    if (this.utilsService().existeFileInList(fileName)) {
      const file: IFileDownloaded = this.utilsService().obtenerFileByFileName(fileName);
      return file.file;
    }
    return '/content/images/static/user-base.png';
  }

  public returnUrlHref(): void {
    return this.$router.go(-1);
  }

  private agruparTitulosAcademicos(tutilosAcademicos: ITituloAcademicoProfesor[]): void {
    this.titulosAcademicosProfesorLista = [];
    for (let i = 0; i < tutilosAcademicos.length; i += 2) {
      if (!(tutilosAcademicos.length % 2) || tutilosAcademicos.length - i != 1) {
        this.titulosAcademicosProfesorLista.push([tutilosAcademicos[i], tutilosAcademicos[i + 2]]);
      } else {
        this.titulosAcademicosProfesorLista.push([tutilosAcademicos[i]]);
      }
    }
  }

  private agruparCursosMaterias(cursosMaterias: ICursoMateria[]): void {
    this.cursosMateriasProfesorLista = [];
    for (let i = 0; i < cursosMaterias.length; i += 2) {
      if (!(cursosMaterias.length % 2) || cursosMaterias.length - i != 1) {
        this.cursosMateriasProfesorLista.push([cursosMaterias[i], cursosMaterias[i + 2]]);
      } else {
        this.cursosMateriasProfesorLista.push([cursosMaterias[i]]);
      }
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      const dateFormater = dayjs(date).format(DATE_FORMAT);
      const dateSplit = dateFormater.split('-');
      return dateSplit.length > 0 ? dateSplit[0] : dateFormater;
    }
    return '';
  }

  public activeTab(tabName: string): void {
    this.tabNameActive = tabName;
  }

  public showTabsInfoProfesor(tabName: string): boolean {
    return tabName.toUpperCase() === this.tabNameActive;
  }
}
