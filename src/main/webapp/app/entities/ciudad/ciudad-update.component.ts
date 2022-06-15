import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import EstadosService from '@/entities/estados/estados.service';
import { IEstados } from '@/shared/model/estados.model';

import { ICiudad, Ciudad } from '@/shared/model/ciudad.model';
import CiudadService from './ciudad.service';

const validations: any = {
  ciudad: {
    nombre: {
      required,
    },
    codigo: {
      required,
    },
    latitud: {},
    longitud: {},
    estados: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class CiudadUpdate extends Vue {
  @Inject('ciudadService') private ciudadService: () => CiudadService;
  @Inject('alertService') private alertService: () => AlertService;

  public ciudad: ICiudad = new Ciudad();

  @Inject('estadosService') private estadosService: () => EstadosService;

  public estados: IEstados[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ciudadId) {
        vm.retrieveCiudad(to.params.ciudadId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.ciudad.id) {
      this.ciudadService()
        .update(this.ciudad)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.ciudad.updated', { param: param.id });
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
      this.ciudadService()
        .create(this.ciudad)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.ciudad.created', { param: param.id });
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

  public retrieveCiudad(ciudadId): void {
    this.ciudadService()
      .find(ciudadId)
      .then(res => {
        this.ciudad = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.estadosService()
      .retrieve()
      .then(res => {
        this.estados = res.data;
      });
  }
}
