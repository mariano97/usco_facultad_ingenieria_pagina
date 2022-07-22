import { Component, Vue, Inject } from 'vue-property-decorator';

import { decimal, required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ProfesorService from '@/entities/profesor/profesor.service';
import { IProfesor } from '@/shared/model/profesor.model';

import { IEscalafonProfesor, EscalafonProfesor } from '@/shared/model/escalafon-profesor.model';
import EscalafonProfesorService from './escalafon-profesor.service';

const validations: any = {
  escalafonProfesor: {
    puntucacionPromedio: {
      required,
      decimal,
    },
    fecha: {
      required,
    },
    profesor: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class EscalafonProfesorUpdate extends Vue {
  @Inject('escalafonProfesorService') private escalafonProfesorService: () => EscalafonProfesorService;
  @Inject('alertService') private alertService: () => AlertService;

  public escalafonProfesor: IEscalafonProfesor = new EscalafonProfesor();

  @Inject('profesorService') private profesorService: () => ProfesorService;

  public profesors: IProfesor[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.escalafonProfesorId) {
        vm.retrieveEscalafonProfesor(to.params.escalafonProfesorId);
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
    if (this.escalafonProfesor.id) {
      this.escalafonProfesorService()
        .update(this.escalafonProfesor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.updated', { param: param.id });
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
      this.escalafonProfesorService()
        .create(this.escalafonProfesor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.created', { param: param.id });
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
      this.escalafonProfesor[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.escalafonProfesor[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.escalafonProfesor[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.escalafonProfesor[field] = null;
    }
  }

  public retrieveEscalafonProfesor(escalafonProfesorId): void {
    this.escalafonProfesorService()
      .find(escalafonProfesorId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        this.escalafonProfesor = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.profesorService()
      .retrieve()
      .then(res => {
        this.profesors = res.data;
      });
  }
}
