import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import AlertService from '@/shared/alert/alert.service';
import { IUsuarioProfesorFull } from '@/shared/model/usuario-profesor.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UsuarioProfesorFullService from '@/shared/services/usuario-profesor.service';
import UtilsService from '@/shared/services/utils.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './profesorado-lista.scss';
import ProgramaService from '@/entities/programa/programa.service';
import { IPrograma } from '@/shared/model/programa.model';

@Component
export default class ProfesoradoLista extends Vue {
  @Inject('programaService') private programaService: () => ProgramaService;
  @Inject('usuarioProfesorFullService') private usuarioProfesorFullService: () => UsuarioProfesorFullService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('utilsService') private utilsService: () => UtilsService;

  public codigoSnies = 0;
  public tituloPaginaProfesorado = '';
  public formSelectPrograma = 0;
  public showFiltering = false;

  public itemsPerPage = 8;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;

  public usuariosProfesores: IUsuarioProfesorFull[][] = [];
  public listadoProgramas: IPrograma[] = [];

  beforeRouteEnter (to, from, next) {
    next(vm => {
      if (to.params.programa_codigo_snies) {
        vm.codigoSnies = to.params.programa_codigo_snies;
        vm.showFiltering = false;
      } else {
        vm.showFiltering = true;
        vm.consultarListadoProgramas();
      }
      vm.obtenerNombrePrograma();
      vm.consultarProfesoresByProgramaCodigoSnies();
    });
  }

  private obtenerNombrePrograma(): void {
    if (this.codigoSnies !== null && this.codigoSnies !== undefined && this.codigoSnies > 0) {
      this.programaService().findNameProgramaByCodigoSnies(this.$store.getters.authenticated, this.codigoSnies)
        .then(res => {
          this.tituloPaginaProfesorado = this.$t('profesor.titulos.title_pagina_profesorado_programa', { programaName: res }).toString();
        })
        .catch(err => {
          this.tituloPaginaProfesorado = this.$t('profesor.titulos.title_pagina_profesorado').toString();
        });
    } else {
      this.tituloPaginaProfesorado = this.$t('profesor.titulos.title_pagina_profesorado').toString();
    }
  }

  private consultarListadoProgramas(): void {
    this.programaService()
      .findAll(this.$store.getters.authenticated)
      .then(res => {
        this.listadoProgramas = res;
      })
      .catch(err => {
        this.listadoProgramas = [];
      });
  }

  private consultarProfesoresByProgramaCodigoSnies(): void {
    this.usuariosProfesores = [];
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    if (this.codigoSnies !== null && this.codigoSnies !== undefined && this.codigoSnies > 0) {
      this.usuarioProfesorFullService()
        .getAllByProgramaCodigoSnies(this.$store.getters.authenticated, Number(this.codigoSnies), paginacionQuery)
        .then(res => {
          this.totalItems = Number(res.headers['x-total-count']);
          this.queryCount = this.totalItems;
          // this.usuariosProfesores = res.data;
          this.agruparProfesoresInArray(res.data);
          this.downloadImage();
        })
        .catch(err => {
          this.usuariosProfesores = [];
        });
    } else {
      this.usuarioProfesorFullService()
        .getAllUsuariosProfesores(this.$store.getters.authenticated, paginacionQuery)
        .then(res => {
          this.totalItems = Number(res.headers['x-total-count']);
          this.queryCount = this.totalItems;
          this.agruparProfesoresInArray(res.data);
          this.downloadImage();
        })
        .catch(err => {
          this.usuariosProfesores = [];
        });
    }
  }

  private agruparProfesoresInArray(profesores: any): void {
    this.usuariosProfesores = [];
    for (let i = 0; i < profesores.length; i += 2) {
      if (!(profesores.length % 2) || profesores.length - i != 1) {
        this.usuariosProfesores.push([profesores[i], profesores[i + 1]]);
      } else {
        this.usuariosProfesores.push([profesores[i]]);
      }
    }
  }

  public sort(): Array<any> {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.consultarProfesoresByProgramaCodigoSnies();
  }

  private downloadImage(): void {
    if (this.usuariosProfesores.length > 0) {
      this.usuariosProfesores.map(usuarioProfesorSubArray => {
        usuarioProfesorSubArray.map(usuarioProfesor => {
          if (
            usuarioProfesor.profesorDTO &&
            usuarioProfesor.profesorDTO.urlFotoProfesor &&
            !this.utilsService().existeFileInList(usuarioProfesor.profesorDTO.urlFotoProfesor)
          ) {
            this.googleStorageService()
              .downloadFileByOnlyFileName(this.$store.getters.authenticated, usuarioProfesor.profesorDTO.urlFotoProfesor)
              .then(res => {
                this.utilsService().agregarFileToList(usuarioProfesor.profesorDTO.urlFotoProfesor, res);
              });
          }
        });
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

  public limpiarCampos(): void {
    this.codigoSnies = 0;
    this.formSelectPrograma = 0;
    this.page = 1;
    this.consultarProfesoresByProgramaCodigoSnies();
  }

  public filtrarProfesores(): void {
    if (this.formSelectPrograma !== null && this.formSelectPrograma !== undefined && this.formSelectPrograma > 0) {
      this.codigoSnies = this.formSelectPrograma;
      this.consultarProfesoresByProgramaCodigoSnies();
    }
  }

  public returnUrlHref(): void {
    return this.$router.go(-1);
  }
}
