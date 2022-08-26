import { required } from 'vuelidate/lib/validators';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './laboratorio-formulario.scss';
import { ILaboratorio, Laboratorio } from '@/shared/model/laboratorio.model';
import AlertService from '@/shared/alert/alert.service';
import LaboratorioService from '@/entities/laboratorio/laboratorio.service';
import UtilsService from '@/shared/services/utils.service';
import GoogleStorageService from '@/shared/services/google-storage.service';
import { IFileDocumentoNuevo, IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { DATE_FORMAT } from '@/shared/date/filters';
import dayjs from 'dayjs';
import carpetasarchivosConstants from '@/shared/constants/carpetasarchivos.constants';

const validations: any = {
  laboratorio: {
    nombre: {
      required,
    },
    informacionGeneral: {
      required,
    },
    urlFoto: {},
    latitud: {},
    longitud: {},
    correoContacto: {
      required,
    },
    direccion: {},
  },
};

@Component({
  validations,
})
export default class LaboratorioFormulario extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('laboratorioService') private laboratorioService: () => LaboratorioService;
  @Inject('alertService') private alertService: () => AlertService;

  public laboratorio: ILaboratorio = new Laboratorio();

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
      if (to.params.laboratorioId) {
        vm.consultarLaboratorio(to.params.laboratorioId);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
    });
  }

  public consultarLaboratorio(laboratorioId: number): void {
    this.laboratorioService()
      .find(laboratorioId)
      .then(res => {
        this.laboratorio = res;
        this.downloadImageProfesorPerfil();
      })
      .catch(error => {
        this.$router.go(-1);
        this.alertService().showHttpError(this, error.response);
      });
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.laboratorio.id) {
      /* this.laboratorio.facultad = {
        id: 1,
      }; */
      this.laboratorioService()
        .update(this.laboratorio)
        .then(res => {
          this.isSaving = false;
          this.enableFormularioEditar();
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.laboratorio.updated', { param: res.id });
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
      /* this.laboratorio.facultad = {
        id: 1,
      }; */
      this.laboratorioService()
        .create(this.laboratorio)
        .then(param => {
          this.laboratorio = param;
          this.isSaving = false;
          if (this.file !== null) {
            this.uploadFileToStorage(
              this.file.type,
              this.laboratorio.id,
              this.file,
              this.generateUrlFotoPerfilFolderUpload(this.laboratorio.id + '')
            );
          }
          this.$router.push({ name: 'laboratorios_lista' });
          // this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.laboratorio.created', { param: param.id });
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
      if (this.laboratorio.id) {
        this.showImage = false;
        this.uploadFileToStorage(this.file.type, this.laboratorio.id, this.file, this.generateUrlFotoPerfilFolderUpload(this.laboratorio.id + ''));
      }
    }
  }


  private generateUrlFotoPerfilFolderUpload(laboratorioId: string): string {
    return carpetasarchivosConstants.CARPETA_BASE_LABORATORIO_FOTO_PERFIL.replace('{{laboratorioId}}', laboratorioId + '');
  }

  private downloadImageProfesorPerfil(): void {
    if (this.laboratorio.id && this.laboratorio.urlFoto) {
      if (this.utilsService().existeFileInList(this.laboratorio.urlFoto)) {
        const file: IFileDownloaded = this.utilsService().obtenerFileByFileName(this.laboratorio.urlFoto);
        this.imageProfileProfesor = file.file;
        this.showImage = true;
      } else {
        this.googleStorageService()
          .downloadFileByOnlyFileName(this.$store.getters.authenticated, this.laboratorio.urlFoto)
          .then(res => {
            this.imageProfileProfesor = res;
            this.showImage = true;
            this.utilsService().agregarFileToList(this.laboratorio.urlFoto, res);
          });
      }
    }
  }

  private uploadFileToStorage(contentType: string, laboratorioId: number, file: File, nameCarpeta: string): void {
    this.showSpinnerLoader = true;
    this.googleStorageService()
      .uploadFotoLaboratorio(contentType, laboratorioId, nameCarpeta, file)
      .then(res => {
        this.laboratorio = res;
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
      this.laboratorio[field] = dayjs(event.target.value, DATE_FORMAT);
    } else {
      this.laboratorio[field] = null;
    }
  }
}
