import { required, numeric } from 'vuelidate/lib/validators';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './curos-materias-formulario.scss';
import { CursoMateria, ICursoMateria } from '@/shared/model/curso-materia.model';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
import AlertService from '@/shared/alert/alert.service';
import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';
import ProgramaService from '@/entities/programa/programa.service';
import { IPrograma } from '@/shared/model/programa.model';

const validations: any = {
  cursoMateria: {
    nombre: {
      required,
    },
    numeroCreditos: {
      required,
      numeric,
    },
    semestreImpartida: {
      required,
      numeric,
    },
    nivelAcademico: {
      required,
    },
  },
};

@Component({
  validations: validations,
})
export default class CursosMateriasFormulario extends Vue {
  @Inject('cursoMateriaService') private cursoMateriaService: () => CursoMateriaService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('programaService') private programaService: () => ProgramaService;

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;

  public cursoMateria: ICursoMateria = new CursoMateria();

  public listNivelesAcademicos: ITablaElementoCatalogo[] = [];
  public programasFacultad: IPrograma[] = [];
  public programasFacultadFilter: IPrograma[] = [];
  public modelFilterPorgrama = '';
  public programaAgregadoId = 0;

  beforeRouteEnter (to, from, next) {
    next(vm => {
      if (to.params.materiaId) {
        vm.consultarMateriaByMateriaId(to.params.materiaId);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
      vm.consultarTiposNivelesAcademicos();
    });
  }

  public consultarMateriaByMateriaId(materiaId: number): void {
    this.cursoMateriaService()
      .find(materiaId)
      .then(res => {
        this.cursoMateria = res;
        this.consultarProgramas(this.cursoMateria.id);
        this.obtenerAllPrograma();
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  private consultarProgramas(cursoMateriaId: number): void {
    this.cursoMateria.programas = [];
    this.programaService()
      .findAllByCursoMateria(this.$store.getters.authenticated, Number(cursoMateriaId))
      .then(res => {
        this.cursoMateria.programas = res;
      })
      .catch(err => {
        this.cursoMateria.programas = [];
      });
  }

  private obtenerAllPrograma(): void {
    const paginationQuery = {
      page: 0,
      size: 10000,
      sort: ['id,asc'],
    };
    this.programaService()
      .retrieve(paginationQuery)
      .then(res => {
        this.programasFacultad = res.data;
      });
  }

  public filterProgramas(): void {
    console.log('dentro de filterPorgramas');
    console.log(this.modelFilterPorgrama);
    if (this.modelFilterPorgrama.length > 0) {
      // this.programasFacultadFilter = this.programasFacultad.filter(programa => programa.nombre === this.modelFilterPorgrama);
      this.programasFacultadFilter = this.filtrarProgramasDelCursoMateria().filter(programa =>
        programa.nombre.toUpperCase().includes(this.modelFilterPorgrama.toUpperCase())
      );
      console.log('programas filtrados');
      console.log(this.programasFacultadFilter);
    } else {
      this.programasFacultadFilter = this.filtrarProgramasDelCursoMateria(); // this.programasFacultad;
    }
  }

  public programasToShow(): IPrograma[] {
    // this.filterProgramas();
    if (this.programasFacultadFilter.length < 1) {
      this.programasFacultadFilter = this.filtrarProgramasDelCursoMateria(); // this.programasFacultad;
    }
    return this.programasFacultadFilter;
  }

  public filtrarProgramasDelCursoMateria(): IPrograma[] {
    let programaTemp: IPrograma[] = [];
    if (this.cursoMateria.id && this.cursoMateria.programas && this.cursoMateria.programas.length > 0) {
      programaTemp = this.programasFacultad.filter(
        programa => this.cursoMateria.programas.filter(programaMateria => programaMateria.id === programa.id).length < 1
      );
    } else {
      programaTemp = this.programasFacultad;
    }
    return programaTemp;
  }

  public eliminarProgramaToCursoMaterias(programa: IPrograma): void {
    this.programaAgregadoId = 0;
    if (this.cursoMateria.id) {
      const indexPrograma = this.cursoMateria.programas.findIndex(index => index.id === programa.id);
      console.log('index');
      console.log(indexPrograma);
      this.cursoMateria.programas.splice(indexPrograma, 1);
      this.cursoMateriaService()
        .update(this.cursoMateria)
        .then(res => {
          this.cursoMateria = res;
          this.programaAgregadoId = 0;
          this.consultarProgramas(this.cursoMateria.id);
        })
        .catch(err => {
          this.cursoMateria.programas.push(programa);
          this.programaAgregadoId = 0;
        });
    } else {
      if (this.cursoMateria.programas) {
        const indexPrograma = this.cursoMateria.programas.indexOf(programa);
        this.cursoMateria.programas.splice(indexPrograma, 1);
        this.programaAgregadoId = 0;
      } else {
        this.programaAgregadoId = 0;
      }
    }
  }

  public agregarProgramaToCursoMateria(programa: IPrograma): void {
    this.programaAgregadoId = programa.id;
    if (this.cursoMateria.id) {
      this.cursoMateria.programas.push(programa);
      this.cursoMateriaService()
        .update(this.cursoMateria)
        .then(res => {
          this.cursoMateria = res;
          this.programaAgregadoId = 0;
          this.consultarProgramas(this.cursoMateria.id);
          this.closeAllPopups();
        })
        .catch(err => {
          this.programaAgregadoId = 0;
          const indexProgramaTemp = this.cursoMateria.programas.indexOf(programa);
          this.cursoMateria.programas.splice(indexProgramaTemp, 1);
          this.closeAllPopups();
        });
    } else {
      if (this.cursoMateria.programas) {
        this.cursoMateria.programas.push(programa);
        this.programaAgregadoId = 0;
      } else {
        this.cursoMateria.programas = [programa];
        this.programaAgregadoId = 0;
      }
      this.closeAllPopups();
    }
  }

  public openPopupAgregarPrograma(): void {
    // this.filtrarProgramasDelCursoMateria();
    this.programasFacultadFilter = [];
    this.programaAgregadoId = 0;
    this.programasToShow();
    if (<any>this.$refs.modalPopupAgregarPrograma) {
      (<any>this.$refs.modalPopupAgregarPrograma).show();
    }
  }

  public closePopupAgregarPrograma(): void {
    (<any>this.$refs.modalPopupAgregarPrograma).hide();
    this.programaAgregadoId = 0;
    this.programasFacultadFilter = [];
  }

  public closeAllPopups(): void {
    this.closePopupAgregarPrograma();
  }

  public eliminar(): void {
    if (this.cursoMateria.id) {
      this.cursoMateriaService()
        .delete(this.cursoMateria.id)
        .then(res => {
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.deleted', { param: this.cursoMateria.id });
          this.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'danger',
            solid: true,
            autoHideDelay: 5000,
          });
          this.$router.go(-1);
        })
        .catch(error => {
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      const message = 'No se hallo datos a eliminar';
      this.$bvToast.toast(message.toString(), {
        toaster: 'b-toaster-top-center',
        title: 'Info',
        variant: 'danger',
        solid: true,
        autoHideDelay: 5000,
      });
      this.$router.go(-1);
    }
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.cursoMateria.id) {
      this.cursoMateriaService()
        .update(this.cursoMateria)
        .then(res => {
          this.cursoMateria = res;
          this.isSaving = false;
          this.enableFormularioEditar();
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.updated', { param: res.nombre });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.cursoMateriaService()
        .create(this.cursoMateria)
        .then(res => {
          this.cursoMateria = res;
          this.$router.push({ name: 'curso_materias_lista' });
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.updated', { param: res.nombre });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  private consultarTiposNivelesAcademicos(): void {
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_TIPO_NIVEL_ACADEMICO)
      .then(res => {
        this.listNivelesAcademicos = res;
      });
  }

  public enableFormularioEditar(): void {
    this.enableEdit = !this.enableEdit;
  }

  public checkHabilitacionCampos(): boolean {
    return !this.enableEdit;
  }

  public cancelarAccion(): void {
    if (this.isModeEdit) {
      this.enableEdit = !this.enableEdit;
    } else {
      this.$router.go(-1);
    }
  }
}
