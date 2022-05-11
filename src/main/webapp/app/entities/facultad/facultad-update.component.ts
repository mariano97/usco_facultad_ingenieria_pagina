import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IFacultad, Facultad } from '@/shared/model/facultad.model';
import FacultadService from './facultad.service';

const validations: any = {
  facultad: {
    nombre: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class FacultadUpdate extends Vue {
  @Inject('facultadService') private facultadService: () => FacultadService;
  @Inject('alertService') private alertService: () => AlertService;

  public facultad: IFacultad = new Facultad();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.facultadId) {
        vm.retrieveFacultad(to.params.facultadId);
      }
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
    if (this.facultad.id) {
      this.facultadService()
        .update(this.facultad)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.facultad.updated', { param: param.id });
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
      this.facultadService()
        .create(this.facultad)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.facultad.created', { param: param.id });
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

  public retrieveFacultad(facultadId): void {
    this.facultadService()
      .find(facultadId)
      .then(res => {
        this.facultad = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
