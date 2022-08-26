import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

import FacultadService from '@/entities/facultad/facultad.service';
import { IFacultad } from '@/shared/model/facultad.model';

import { ILaboratorio, Laboratorio } from '@/shared/model/laboratorio.model';
import LaboratorioService from './laboratorio.service';

const validations: any = {
  laboratorio: {
    nombre: {
      required,
    },
    informacionGeneral: {
      required,
    },
    urlFoto: {},
    latitud: {},
    longitud: {},
    correoContacto: {
      required,
    },
    direccion: {},
    tipoLaboratorio: {
      required,
    },
    facultad: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class LaboratorioUpdate extends Vue {
  @Inject('laboratorioService') private laboratorioService: () => LaboratorioService;
  @Inject('alertService') private alertService: () => AlertService;

  public laboratorio: ILaboratorio = new Laboratorio();

  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;

  public tablaElementoCatalogos: ITablaElementoCatalogo[] = [];

  @Inject('facultadService') private facultadService: () => FacultadService;

  public facultads: IFacultad[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.laboratorioId) {
        vm.retrieveLaboratorio(to.params.laboratorioId);
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
    if (this.laboratorio.id) {
      this.laboratorioService()
        .update(this.laboratorio)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.laboratorio.updated', { param: param.id });
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
      this.laboratorioService()
        .create(this.laboratorio)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.laboratorio.created', { param: param.id });
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

  public retrieveLaboratorio(laboratorioId): void {
    this.laboratorioService()
      .find(laboratorioId)
      .then(res => {
        this.laboratorio = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.tablaElementoCatalogoService()
      .retrieve()
      .then(res => {
        this.tablaElementoCatalogos = res.data;
      });
    this.facultadService()
      .retrieve()
      .then(res => {
        this.facultads = res.data;
      });
  }
}
