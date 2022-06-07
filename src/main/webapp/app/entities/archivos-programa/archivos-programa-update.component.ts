import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, numeric } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ProgramaService from '@/entities/programa/programa.service';
import { IPrograma } from '@/shared/model/programa.model';

import { IArchivosPrograma, ArchivosPrograma } from '@/shared/model/archivos-programa.model';
import ArchivosProgramaService from './archivos-programa.service';

const validations: any = {
  archivosPrograma: {
    urlName: {
      required,
    },
    generationStorage: {
      required,
      numeric,
    },
    storageContentType: {},
    tipoDocumento: {},
    planEstudio: {
      required,
    },
    programa: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ArchivosProgramaUpdate extends Vue {
  @Inject('archivosProgramaService') private archivosProgramaService: () => ArchivosProgramaService;
  @Inject('alertService') private alertService: () => AlertService;

  public archivosPrograma: IArchivosPrograma = new ArchivosPrograma();

  @Inject('programaService') private programaService: () => ProgramaService;

  public programas: IPrograma[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.archivosProgramaId) {
        vm.retrieveArchivosPrograma(to.params.archivosProgramaId);
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
    if (this.archivosPrograma.id) {
      this.archivosProgramaService()
        .update(this.archivosPrograma)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.updated', { param: param.id });
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
      this.archivosProgramaService()
        .create(this.archivosPrograma)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.created', { param: param.id });
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

  public retrieveArchivosPrograma(archivosProgramaId): void {
    this.archivosProgramaService()
      .find(archivosProgramaId)
      .then(res => {
        this.archivosPrograma = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.programaService()
      .retrieve()
      .then(res => {
        this.programas = res.data;
      });
  }
}
