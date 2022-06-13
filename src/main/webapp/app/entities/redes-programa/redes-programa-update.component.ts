import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ProgramaService from '@/entities/programa/programa.service';
import { IPrograma } from '@/shared/model/programa.model';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

import { IRedesPrograma, RedesPrograma } from '@/shared/model/redes-programa.model';
import RedesProgramaService from './redes-programa.service';

const validations: any = {
  redesPrograma: {
    urlRedSocial: {
      required,
    },
    programa: {
      required,
    },
    tablaElementoCatalogo: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class RedesProgramaUpdate extends Vue {
  @Inject('redesProgramaService') private redesProgramaService: () => RedesProgramaService;
  @Inject('alertService') private alertService: () => AlertService;

  public redesPrograma: IRedesPrograma = new RedesPrograma();

  @Inject('programaService') private programaService: () => ProgramaService;

  public programas: IPrograma[] = [];

  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;

  public tablaElementoCatalogos: ITablaElementoCatalogo[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.redesProgramaId) {
        vm.retrieveRedesPrograma(to.params.redesProgramaId);
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
    if (this.redesPrograma.id) {
      this.redesProgramaService()
        .update(this.redesPrograma)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.updated', { param: param.id });
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
      this.redesProgramaService()
        .create(this.redesPrograma)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.created', { param: param.id });
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

  public retrieveRedesPrograma(redesProgramaId): void {
    this.redesProgramaService()
      .find(redesProgramaId)
      .then(res => {
        this.redesPrograma = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.programaService()
      .retrieve()
      .then(res => {
        this.programas = res.data;
      });
    this.tablaElementoCatalogoService()
      .retrieve()
      .then(res => {
        this.tablaElementoCatalogos = res.data;
      });
  }
}
