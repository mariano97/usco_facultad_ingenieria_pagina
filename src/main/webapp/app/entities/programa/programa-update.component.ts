import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, numeric, decimal } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

import FacultadService from '@/entities/facultad/facultad.service';
import { IFacultad } from '@/shared/model/facultad.model';

import SedeService from '@/entities/sede/sede.service';
import { ISede } from '@/shared/model/sede.model';

import ProfesorService from '@/entities/profesor/profesor.service';
import { IProfesor } from '@/shared/model/profesor.model';

import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';
import { ICursoMateria } from '@/shared/model/curso-materia.model';

import { IPrograma, Programa } from '@/shared/model/programa.model';
import ProgramaService from './programa.service';

const validations: any = {
  programa: {
    nombre: {
      required,
    },
    codigoSnies: {
      required,
      numeric,
    },
    codigoRegistroCalificado: {
      required,
      numeric,
    },
    fechaRegistroCalificado: {
      required,
    },
    nombreTituloOtorgado: {
      required,
    },
    numeroCreditos: {
      required,
      numeric,
    },
    duracionPrograma: {},
    presentacionPrograma: {
      required,
    },
    mision: {
      required,
    },
    vision: {
      required,
    },
    perfilEstudiante: {},
    perfilEgresado: {
      required,
    },
    perfilOcupacional: {},
    urlFotoPrograma: {},
    dirigidoAQuien: {},
    costoPrograma: {
      required,
      decimal,
    },
    estado: {
      required,
    },
    emailContacto: {
      required,
    },
    nivelFormacion: {
      required,
    },
    tipoFormacion: {
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
export default class ProgramaUpdate extends Vue {
  @Inject('programaService') private programaService: () => ProgramaService;
  @Inject('alertService') private alertService: () => AlertService;

  public programa: IPrograma = new Programa();

  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;

  public tablaElementoCatalogos: ITablaElementoCatalogo[] = [];

  @Inject('facultadService') private facultadService: () => FacultadService;

  public facultads: IFacultad[] = [];

  @Inject('sedeService') private sedeService: () => SedeService;

  public sedes: ISede[] = [];

  @Inject('profesorService') private profesorService: () => ProfesorService;

  public profesors: IProfesor[] = [];

  @Inject('cursoMateriaService') private cursoMateriaService: () => CursoMateriaService;

  public cursoMaterias: ICursoMateria[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.programaId) {
        vm.retrievePrograma(to.params.programaId);
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
    this.programa.sedes = [];
    this.programa.profesors = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.programa.id) {
      this.programaService()
        .update(this.programa)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.updated', { param: param.id });
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
      this.programaService()
        .create(this.programa)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.created', { param: param.id });
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
      this.programa[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.programa[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.programa[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.programa[field] = null;
    }
  }

  public retrievePrograma(programaId): void {
    this.programaService()
      .find(programaId)
      .then(res => {
        res.fechaRegistroCalificado = new Date(res.fechaRegistroCalificado);
        this.programa = res;
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
    this.sedeService()
      .retrieve()
      .then(res => {
        this.sedes = res.data;
      });
    this.profesorService()
      .retrieve()
      .then(res => {
        this.profesors = res.data;
      });
    this.cursoMateriaService()
      .retrieve()
      .then(res => {
        this.cursoMaterias = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
