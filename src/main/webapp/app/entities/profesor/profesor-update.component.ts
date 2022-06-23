import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, numeric } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

import FacultadService from '@/entities/facultad/facultad.service';
import { IFacultad } from '@/shared/model/facultad.model';

import ProgramaService from '@/entities/programa/programa.service';
import { IPrograma } from '@/shared/model/programa.model';

import { IProfesor, Profesor } from '@/shared/model/profesor.model';
import ProfesorService from './profesor.service';

const validations: any = {
  profesor: {
    emailAlternativo: {},
    activo: {
      required,
    },
    perfil: {
      required,
    },
    telefonoCelular: {},
    oficina: {},
    userId: {
      required,
      numeric,
    },
    tablaElementoCatalogo: {
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
export default class ProfesorUpdate extends Vue {
  @Inject('profesorService') private profesorService: () => ProfesorService;
  @Inject('alertService') private alertService: () => AlertService;

  public profesor: IProfesor = new Profesor();

  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;

  public tablaElementoCatalogos: ITablaElementoCatalogo[] = [];

  @Inject('facultadService') private facultadService: () => FacultadService;

  public facultads: IFacultad[] = [];

  @Inject('programaService') private programaService: () => ProgramaService;

  public programas: IPrograma[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.profesorId) {
        vm.retrieveProfesor(to.params.profesorId);
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
    if (this.profesor.id) {
      this.profesorService()
        .update(this.profesor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.profesor.updated', { param: param.id });
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
      this.profesorService()
        .create(this.profesor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.profesor.created', { param: param.id });
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

  public retrieveProfesor(profesorId): void {
    this.profesorService()
      .find(profesorId)
      .then(res => {
        this.profesor = res;
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
    this.programaService()
      .retrieve()
      .then(res => {
        this.programas = res.data;
      });
  }
}
