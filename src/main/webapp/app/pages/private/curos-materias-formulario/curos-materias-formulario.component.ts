import { required, numeric } from 'vuelidate/lib/validators';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './curos-materias-formulario.scss';
import { CursoMateria, ICursoMateria } from '@/shared/model/curso-materia.model';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
import AlertService from '@/shared/alert/alert.service';
import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';

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

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;

  public cursoMateria: ICursoMateria = new CursoMateria();

  public listNivelesAcademicos: ITablaElementoCatalogo[] = [];

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
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      })
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
