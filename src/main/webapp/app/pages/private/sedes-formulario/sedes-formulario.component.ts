import { required, decimal, maxLength, email, helpers } from 'vuelidate/lib/validators';
import { ISede, Sede } from './../../../shared/model/sede.model';
import SedeService from '@/entities/sede/sede.service';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './sedes-formulario.scss';
import AlertService from '@/shared/alert/alert.service';
import * as validators from '@/shared/validators/validators.component';

const validations: any = {
  sede: {
    nombre: {
      required,
      maxLength: maxLength(250),
    },
    latitud: {
      required,
      decimal,
    },
    longitud: {
      required,
      decimal,
    },
    direccion: {
      required,
      maxLength: maxLength(250),
    },
    estado: {
      required,
    },
    telefonoFijo: {
      maxLength: maxLength(7),
    },
    telefonoCelular: {
      maxLength: maxLength(10),
    },
    correoElectronico: {
      required,
      validEmail: validators.validEmail,
    },
    codigoIndicativo: {
      required,
      maxLength: maxLength(20),
    },
  },
};

@Component({
  validations,
})
export default class SedesFormulario extends Vue {
  @Inject('sedeService') private sedeService: () => SedeService;
  @Inject('alertService') private alertService: () => AlertService;

  public sede: ISede = new Sede();

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.sedeId) {
        vm.consultarSede(to.params.sedeId);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
    });
  }

  private consultarSede(codigoIndicativo: string): void {
    this.sede = {};
    this.sedeService()
      .findByCodigoIndicativo(codigoIndicativo)
      .then(res => {
        this.sede = res;
      });
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.sede.id) {
      this.sedeService()
        .update(this.sede)
        .then(res => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.updated', { param: res.id });
          return this.$root.$bvToast.toast(message.toString(), {
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
      this.sede.estado = true;
      this.sedeService()
        .create(this.sede)
        .then(res => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.sede.created', { param: res.id });
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

  public checkHabilitacionCampos(): boolean {
    return !this.enableEdit;
  }

  public enableFormularioEditar(): void {
    this.enableEdit = !this.enableEdit;
  }

  public cancelarAccion(): void {
    if (this.isModeEdit) {
      this.enableEdit = !this.enableEdit;
    } else {
      this.$router.go(-1);
    }
  }
}
