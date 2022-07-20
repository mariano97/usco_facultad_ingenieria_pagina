import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import FacultadService from '@/entities/facultad/facultad.service';
import { IFacultad } from '@/shared/model/facultad.model';

import ProfesorService from '@/entities/profesor/profesor.service';
import { IProfesor } from '@/shared/model/profesor.model';

import { ISemillero, Semillero } from '@/shared/model/semillero.model';
import SemilleroService from './semillero.service';

const validations: any = {
  semillero: {
    nombre: {
      required,
    },
    informacionGeneral: {
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
export default class SemilleroUpdate extends Vue {
  @Inject('semilleroService') private semilleroService: () => SemilleroService;
  @Inject('alertService') private alertService: () => AlertService;

  public semillero: ISemillero = new Semillero();

  @Inject('facultadService') private facultadService: () => FacultadService;

  public facultads: IFacultad[] = [];

  @Inject('profesorService') private profesorService: () => ProfesorService;

  public profesors: IProfesor[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.semilleroId) {
        vm.retrieveSemillero(to.params.semilleroId);
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
    if (this.semillero.id) {
      this.semilleroService()
        .update(this.semillero)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.semillero.updated', { param: param.id });
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
      this.semilleroService()
        .create(this.semillero)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.semillero.created', { param: param.id });
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

  public retrieveSemillero(semilleroId): void {
    this.semilleroService()
      .find(semilleroId)
      .then(res => {
        this.semillero = res;
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
    this.profesorService()
      .retrieve()
      .then(res => {
        this.profesors = res.data;
      });
  }
}
