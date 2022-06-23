import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

import ProfesorService from '@/entities/profesor/profesor.service';
import { IProfesor } from '@/shared/model/profesor.model';

import PaisesService from '@/entities/paises/paises.service';
import { IPaises } from '@/shared/model/paises.model';

import { ITituloAcademicoProfesor, TituloAcademicoProfesor } from '@/shared/model/titulo-academico-profesor.model';
import TituloAcademicoProfesorService from './titulo-academico-profesor.service';

const validations: any = {
  tituloAcademicoProfesor: {
    nombreTitulo: {
      required,
    },
    nombreUniversidadTitulo: {
      required,
    },
    yearTitulo: {
      required,
    },
    tablaElementoCatalogo: {
      required,
    },
    profesor: {
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
export default class TituloAcademicoProfesorUpdate extends Vue {
  @Inject('tituloAcademicoProfesorService') private tituloAcademicoProfesorService: () => TituloAcademicoProfesorService;
  @Inject('alertService') private alertService: () => AlertService;

  public tituloAcademicoProfesor: ITituloAcademicoProfesor = new TituloAcademicoProfesor();

  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;

  public tablaElementoCatalogos: ITablaElementoCatalogo[] = [];

  @Inject('profesorService') private profesorService: () => ProfesorService;

  public profesors: IProfesor[] = [];

  @Inject('paisesService') private paisesService: () => PaisesService;

  public paises: IPaises[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tituloAcademicoProfesorId) {
        vm.retrieveTituloAcademicoProfesor(to.params.tituloAcademicoProfesorId);
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
    if (this.tituloAcademicoProfesor.id) {
      this.tituloAcademicoProfesorService()
        .update(this.tituloAcademicoProfesor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.updated', { param: param.id });
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
      this.tituloAcademicoProfesorService()
        .create(this.tituloAcademicoProfesor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.tituloAcademicoProfesor[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.tituloAcademicoProfesor[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.tituloAcademicoProfesor[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.tituloAcademicoProfesor[field] = null;
    }
  }

  public retrieveTituloAcademicoProfesor(tituloAcademicoProfesorId): void {
    this.tituloAcademicoProfesorService()
      .find(tituloAcademicoProfesorId)
      .then(res => {
        res.yearTitulo = new Date(res.yearTitulo);
        this.tituloAcademicoProfesor = res;
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
    this.profesorService()
      .retrieve()
      .then(res => {
        this.profesors = res.data;
      });
    this.paisesService()
      .retrieve()
      .then(res => {
        this.paises = res.data;
      });
  }
}
