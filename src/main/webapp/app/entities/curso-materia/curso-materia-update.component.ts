import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, numeric } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

import { ICursoMateria, CursoMateria } from '@/shared/model/curso-materia.model';
import CursoMateriaService from './curso-materia.service';

const validations: any = {
  cursoMateria: {
    nombre: {
      required,
    },
    numeroCreditos: {
      required,
      numeric,
    },
    semestreImpartida: {
      required,
      numeric,
    },
    nivelAcademico: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class CursoMateriaUpdate extends Vue {
  @Inject('cursoMateriaService') private cursoMateriaService: () => CursoMateriaService;
  @Inject('alertService') private alertService: () => AlertService;

  public cursoMateria: ICursoMateria = new CursoMateria();

  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;

  public tablaElementoCatalogos: ITablaElementoCatalogo[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.cursoMateriaId) {
        vm.retrieveCursoMateria(to.params.cursoMateriaId);
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
    if (this.cursoMateria.id) {
      this.cursoMateriaService()
        .update(this.cursoMateria)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.updated', { param: param.id });
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
      this.cursoMateriaService()
        .create(this.cursoMateria)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.created', { param: param.id });
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

  public retrieveCursoMateria(cursoMateriaId): void {
    this.cursoMateriaService()
      .find(cursoMateriaId)
      .then(res => {
        this.cursoMateria = res;
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
  }
}
