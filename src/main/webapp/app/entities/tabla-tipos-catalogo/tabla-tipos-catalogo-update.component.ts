import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { ITablaTiposCatalogo, TablaTiposCatalogo } from '@/shared/model/tabla-tipos-catalogo.model';
import TablaTiposCatalogoService from './tabla-tipos-catalogo.service';

const validations: any = {
  tablaTiposCatalogo: {
    nombre: {
      required,
    },
    estado: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class TablaTiposCatalogoUpdate extends Vue {
  @Inject('tablaTiposCatalogoService') private tablaTiposCatalogoService: () => TablaTiposCatalogoService;
  @Inject('alertService') private alertService: () => AlertService;

  public tablaTiposCatalogo: ITablaTiposCatalogo = new TablaTiposCatalogo();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tablaTiposCatalogoId) {
        vm.retrieveTablaTiposCatalogo(to.params.tablaTiposCatalogoId);
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
    if (this.tablaTiposCatalogo.id) {
      this.tablaTiposCatalogoService()
        .update(this.tablaTiposCatalogo)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.updated', { param: param.id });
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
      this.tablaTiposCatalogoService()
        .create(this.tablaTiposCatalogo)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.created', { param: param.id });
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

  public retrieveTablaTiposCatalogo(tablaTiposCatalogoId): void {
    this.tablaTiposCatalogoService()
      .find(tablaTiposCatalogoId)
      .then(res => {
        this.tablaTiposCatalogo = res;
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
