import { required } from 'vuelidate/lib/validators';
import AlertService from '@/shared/alert/alert.service';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IFileDocumentoNuevo, IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { ISemillero, Semillero } from '@/shared/model/semillero.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './semillero-formulario.scss';
import SemilleroService from '@/entities/semillero/semillero.service';
import carpetasarchivosConstants from '@/shared/constants/carpetasarchivos.constants';
import './semillero-formulario.scss';

const validations: any = {
  semillero: {
    nombre: {
      required,
    },
    informacionGeneral: {
      required,
    },
    urlFoto: {},
  },
};

@Component({
  validations,
})
export default class SemilleroFormulario extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('semilleroService') private semilleroService: () => SemilleroService;
  @Inject('alertService') private alertService: () => AlertService;

  public semillero: ISemillero = new Semillero();

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;
  public showImage = false;
  public showSpinnerLoader = false;

  public imageProfileProfesor: any;
  private file: any = null;
  public programaDocumentoNuevo: IFileDocumentoNuevo = {};

  public dateMax = dayjs().format(DATE_FORMAT);

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.semilleroId) {
        vm.consultarSemillero(to.params.semilleroId);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
    });
  }

  public consultarSemillero(semilleroId: number): void {
    this.semilleroService()
      .find(semilleroId)
      .then(res => {
        this.semillero = res;
        this.downloadImageProfesorPerfil();
      })
      .catch(error => {
        this.$router.go(-1);
        this.alertService().showHttpError(this, error.response);
      });
  }

  public eliminar(): void {
    if (this.semillero.id) {
      this.semilleroService()
        .delete(this.semillero.id)
        .then(res => {
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.semillero.deleted', { param: this.semillero.nombre });
          this.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'danger',
            solid: true,
            autoHideDelay: 5000,
          });
          this.$router.go(-1);
        })
        .catch(error => {
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      const message = 'No se hallo datos a eliminar';
      this.$bvToast.toast(message.toString(), {
        toaster: 'b-toaster-top-center',
        title: 'Info',
        variant: 'danger',
        solid: true,
        autoHideDelay: 5000,
      });
      this.$router.go(-1);
    }
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.semillero.id) {
      this.semillero.facultad = {
        id: 1,
      };
      this.semilleroService()
        .update(this.semillero)
        .then(res => {
          this.isSaving = false;
          this.enableFormularioEditar();
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.semillero.updated', { param: res.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.enableFormularioEditar();
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.semillero.facultad = {
        id: 1,
      };
      this.semilleroService()
        .create(this.semillero)
        .then(param => {
          this.semillero = param;
          this.isSaving = false;
          if (this.file !== null) {
            this.uploadFileToStorage(
              this.file.type,
              this.semillero.id,
              this.file,
              this.generateUrlFotoPerfilFolderUpload(this.semillero.id + '')
            );
          }
          this.$router.push({ name: 'semillero_lista' });
          // this.$router.go(-1);
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

  public changeImage(event): void {
    this.programaDocumentoNuevo = {};
    console.log(event.target.files);
    if (event.target.files && event.target.files.length > 0) {
      let message = '';
      const file = event.target.files[0];
      this.programaDocumentoNuevo = {
        file: file,
        nombre: file.name,
        size: file.size,
        tipoDocumento: file.type,
        isValidDoc: false,
      };
      const allowedImageTypes = carpetasarchivosConstants.TIPOS_IMAGEN_PERMITIDOS;
      const sizeMaxFile = carpetasarchivosConstants.TAMANO_MAXIMO_ARCHIVOS;
      if (!allowedImageTypes.includes(this.programaDocumentoNuevo.file.type)) {
        console.log("tipo no permitido");
        message = this.$t('archivosPrograma.errors.tipyFileInvalid', {
          filesValid: allowedImageTypes.map(type => this.utilsService().changeTypeFileToString(type)).join(', '),
        }).toString();
      }
      if (this.programaDocumentoNuevo.size > sizeMaxFile) {
        message = this.$t('archivosPrograma.errors.sizeMax', { sizeMax: this.utilsService().sizeToMB(sizeMaxFile) }).toString();
      }
      if (message.length > 0) {
        this.$root.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'info',
          solid: true,
          autoHideDelay: 5000,
        });
        return;
      }
      this.file = file;
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = () => {
        this.imageProfileProfesor = fileReader.result;
        this.showImage = true;
      };
      if (this.semillero.id) {
        this.showImage = false;
        this.uploadFileToStorage(this.file.type, this.semillero.id, this.file, this.generateUrlFotoPerfilFolderUpload(this.semillero.id + ''));
      }
    }
  }

  private generateUrlFotoPerfilFolderUpload(semilleroId: string): string {
    return carpetasarchivosConstants.CARPETA_BASE_SEMILLERO_FOTO_PERFIL.replace('{{labortorioId}}', semilleroId + '');
  }

  private downloadImageProfesorPerfil(): void {
    if (this.semillero.id && this.semillero.urlFoto) {
      if (this.utilsService().existeFileInList(this.semillero.urlFoto)) {
        const file: IFileDownloaded = this.utilsService().obtenerFileByFileName(this.semillero.urlFoto);
        this.imageProfileProfesor = file.file;
        this.showImage = true;
      } else {
        this.googleStorageService()
          .downloadFileByOnlyFileName(this.$store.getters.authenticated, this.semillero.urlFoto)
          .then(res => {
            this.imageProfileProfesor = res;
            this.showImage = true;
            this.utilsService().agregarFileToList(this.semillero.urlFoto, res);
          });
      }
    }
  }

  private uploadFileToStorage(contentType: string, semilleroId: number, file: File, nameCarpeta: string): void {
    this.showSpinnerLoader = true;
    this.googleStorageService()
      .uploadFotoSemillero(contentType, semilleroId, nameCarpeta, file)
      .then(res => {
        this.semillero = res;
        this.showSpinnerLoader = false;
      })
      .catch(err => {
        this.showSpinnerLoader = false;
        this.alertService().showHttpError(this, err.response);
      });
  }

  public enableFormularioEditar(): void {
    this.enableEdit = !this.enableEdit;
  }

  public checkHabilitacionCampos(): boolean {
    return !this.enableEdit;
  }

  public cancelarAccion(): void {
    if (this.isModeEdit) {
      this.enableEdit = !this.enableEdit;
    } else {
      this.$router.go(-1);
    }
  }

  public countCharacter(maxTamano: number, value: string): number {
    return maxTamano - (value ? value.trim().length : 0);
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.semillero[field] = dayjs(event.target.value, DATE_FORMAT);
    } else {
      this.semillero[field] = null;
    }
  }
}
