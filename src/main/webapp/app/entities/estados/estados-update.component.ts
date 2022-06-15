import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import PaisesService from '@/entities/paises/paises.service';
import { IPaises } from '@/shared/model/paises.model';

import { IEstados, Estados } from '@/shared/model/estados.model';
import EstadosService from './estados.service';

const validations: any = {
  estados: {
    nombre: {
      required,
    },
    codigo: {
      required,
    },
    paises: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class EstadosUpdate extends Vue {
  @Inject('estadosService') private estadosService: () => EstadosService;
  @Inject('alertService') private alertService: () => AlertService;

  public estados: IEstados = new Estados();

  @Inject('paisesService') private paisesService: () => PaisesService;

  public paises: IPaises[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.estadosId) {
        vm.retrieveEstados(to.params.estadosId);
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
    if (this.estados.id) {
      this.estadosService()
        .update(this.estados)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.estados.updated', { param: param.id });
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
      this.estadosService()
        .create(this.estados)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.estados.created', { param: param.id });
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

  public retrieveEstados(estadosId): void {
    this.estadosService()
      .find(estadosId)
      .then(res => {
        this.estados = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.paisesService()
      .retrieve()
      .then(res => {
        this.paises = res.data;
      });
  }
}
