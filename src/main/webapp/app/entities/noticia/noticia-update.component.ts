import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import FacultadService from '@/entities/facultad/facultad.service';
import { IFacultad } from '@/shared/model/facultad.model';

import { INoticia, Noticia } from '@/shared/model/noticia.model';
import NoticiaService from './noticia.service';

const validations: any = {
  noticia: {
    titulo: {
      required,
    },
    sintesis: {
      required,
    },
    cuerpoDescripcion: {
      required,
    },
    fecha: {
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
export default class NoticiaUpdate extends Vue {
  @Inject('noticiaService') private noticiaService: () => NoticiaService;
  @Inject('alertService') private alertService: () => AlertService;

  public noticia: INoticia = new Noticia();

  @Inject('facultadService') private facultadService: () => FacultadService;

  public facultads: IFacultad[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.noticiaId) {
        vm.retrieveNoticia(to.params.noticiaId);
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
    if (this.noticia.id) {
      this.noticiaService()
        .update(this.noticia)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.noticia.updated', { param: param.id });
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
      this.noticiaService()
        .create(this.noticia)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.noticia.created', { param: param.id });
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
      this.noticia[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.noticia[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.noticia[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.noticia[field] = null;
    }
  }

  public retrieveNoticia(noticiaId): void {
    this.noticiaService()
      .find(noticiaId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        this.noticia = res;
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
