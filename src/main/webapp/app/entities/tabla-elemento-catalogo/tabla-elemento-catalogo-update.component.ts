import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TablaTiposCatalogoService from '@/entities/tabla-tipos-catalogo/tabla-tipos-catalogo.service';
import { ITablaTiposCatalogo } from '@/shared/model/tabla-tipos-catalogo.model';

import { ITablaElementoCatalogo, TablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import TablaElementoCatalogoService from './tabla-elemento-catalogo.service';

const validations: any = {
  tablaElementoCatalogo: {
    nombre: {
      required,
    },
    abreviatura: {},
    estado: {
      required,
    },
    tablaTiposCatalogo: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class TablaElementoCatalogoUpdate extends Vue {
  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('alertService') private alertService: () => AlertService;

  public tablaElementoCatalogo: ITablaElementoCatalogo = new TablaElementoCatalogo();

  @Inject('tablaTiposCatalogoService') private tablaTiposCatalogoService: () => TablaTiposCatalogoService;

  public tablaTiposCatalogos: ITablaTiposCatalogo[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tablaElementoCatalogoId) {
        vm.retrieveTablaElementoCatalogo(to.params.tablaElementoCatalogoId);
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
    if (this.tablaElementoCatalogo.id) {
      this.tablaElementoCatalogoService()
        .update(this.tablaElementoCatalogo)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.updated', { param: param.id });
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
      this.tablaElementoCatalogoService()
        .create(this.tablaElementoCatalogo)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.created', { param: param.id });
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

  public retrieveTablaElementoCatalogo(tablaElementoCatalogoId): void {
    this.tablaElementoCatalogoService()
      .find(tablaElementoCatalogoId)
      .then(res => {
        this.tablaElementoCatalogo = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.tablaTiposCatalogoService()
      .retrieve()
      .then(res => {
        this.tablaTiposCatalogos = res.data;
      });
  }
}
