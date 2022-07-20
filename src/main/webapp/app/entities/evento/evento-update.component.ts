import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import FacultadService from '@/entities/facultad/facultad.service';
import { IFacultad } from '@/shared/model/facultad.model';

import { IEvento, Evento } from '@/shared/model/evento.model';
import EventoService from './evento.service';

const validations: any = {
  evento: {
    titulo: {
      required,
    },
    cuerpo: {
      required,
    },
    fecha: {
      required,
    },
    hora: {
      required,
    },
    lugar: {
      required,
    },
    urlFoto: {},
    facultad: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class EventoUpdate extends Vue {
  @Inject('eventoService') private eventoService: () => EventoService;
  @Inject('alertService') private alertService: () => AlertService;

  public evento: IEvento = new Evento();

  @Inject('facultadService') private facultadService: () => FacultadService;

  public facultads: IFacultad[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.eventoId) {
        vm.retrieveEvento(to.params.eventoId);
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
    if (this.evento.id) {
      this.eventoService()
        .update(this.evento)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.evento.updated', { param: param.id });
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
      this.eventoService()
        .create(this.evento)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.evento.created', { param: param.id });
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
      this.evento[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.evento[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.evento[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.evento[field] = null;
    }
  }

  public retrieveEvento(eventoId): void {
    this.eventoService()
      .find(eventoId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        res.hora = new Date(res.hora);
        this.evento = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.facultadService()
      .retrieve()
      .then(res => {
        this.facultads = res.data;
      });
  }
}
