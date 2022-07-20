import { maxLength, minLength, required } from 'vuelidate/lib/validators';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IFileDocumentoNuevo, IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { INoticia, Noticia } from '@/shared/model/noticia.model';
import dayjs from 'dayjs';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './noticias-formulario.scss';
import AlertService from '@/shared/alert/alert.service';
import NoticiaService from '@/entities/noticia/noticia.service';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import carpetasarchivosConstants from '@/shared/constants/carpetasarchivos.constants';

const validations: any = {
  noticia: {
    titulo: {
      required,
      maxLength: maxLength(500),
      minLength: minLength(1),
    },
    sintesis: {
      required,
      maxLength: maxLength(500),
      minLength: minLength(1),
    },
    cuerpoDescripcion: {
      required,
      maxLength: maxLength(1000),
      minLength: minLength(1),
    },
    fecha: {
      required,
    },
    urlFoto: {},
  },
};

@Component({
  validations,
})
export default class NoticiasFormulario extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('noticiaService') private noticiaService: () => NoticiaService;
  @Inject('alertService') private alertService: () => AlertService;

  public noticia: INoticia = new Noticia();

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
      if (to.params.noticiaId) {
        vm.consultarNoticia(to.params.noticiaId);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
    });
  }

  private consultarNoticia(noticiaId: number): void {
    this.noticiaService()
      .find(noticiaId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        this.noticia = res;
        this.downloadImageProfesorPerfil();
      })
      .catch(error => {
        this.$router.go(-1);
        this.alertService().showHttpError(this, error.response);
      });
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
      if (this.noticia.id) {
        this.showImage = false;
        this.uploadFileToStorage(this.file.type, this.noticia.id, this.file, this.generateUrlFotoPerfilFolderUpload(this.noticia.id + ''));
      }
    }
  }

  private generateUrlFotoPerfilFolderUpload(noticiaId: string): string {
    return carpetasarchivosConstants.CARPETA_BASE_NOTICIA_FOTO_PERFIL.replace('{{noticiaId}}', noticiaId + '')
  }

  private downloadImageProfesorPerfil(): void {
    if (this.noticia.id && this.noticia.urlFoto) {
      if (this.utilsService().existeFileInList(this.noticia.urlFoto)) {
        const file: IFileDownloaded = this.utilsService().obtenerFileByFileName(this.noticia.urlFoto);
        this.imageProfileProfesor = file.file;
        this.showImage = true;
      } else {
        this.googleStorageService()
          .downloadFileByOnlyFileName(this.$store.getters.authenticated, this.noticia.urlFoto)
          .then(res => {
            this.imageProfileProfesor = res;
            this.showImage = true;
            this.utilsService().agregarFileToList(this.noticia.urlFoto, res);
          });
      }
    }
  }

  private uploadFileToStorage(contentType: string, noticiaId: number, file: File, nameCarpeta: string): void {
    this.showSpinnerLoader = true;
    this.googleStorageService()
      .uploadFotoNoticia(contentType, noticiaId, nameCarpeta, file)
      .then(res => {
        this.noticia = res;
        this.showSpinnerLoader = false;
      })
      .catch(err => {
        this.showSpinnerLoader = false;
        this.alertService().showHttpError(this, err.response);
      });
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.noticia.id) {
      this.noticia.facultad = {
        id: 1,
      };
      this.noticiaService()
        .update(this.noticia)
        .then(res => {
          this.isSaving = false;
          this.enableFormularioEditar();
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.noticia.updated', { param: res.id });
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
      this.noticia.facultad = {
        id: 1,
      };
      this.noticiaService()
        .create(this.noticia)
        .then(param => {
          this.noticia = param;
          this.isSaving = false;
          if (this.file !== null) {
            this.uploadFileToStorage(
              this.file.type,
              this.noticia.id,
              this.file,
              this.generateUrlFotoPerfilFolderUpload(this.noticia.id + '')
            );
          }
          this.$router.push({ name: 'noticias_lista' });
          // this.$router.go(-1);
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
      this.noticia[field] = dayjs(event.target.value, DATE_FORMAT);
    } else {
      this.noticia[field] = null;
    }
  }
}
