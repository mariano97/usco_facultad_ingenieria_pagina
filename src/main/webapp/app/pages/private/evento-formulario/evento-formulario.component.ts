import { required, maxLength, minLength } from 'vuelidate/lib/validators';
import { Evento, IEvento } from '@/shared/model/evento.model';
import { Component, Inject, Vue } from 'vue-property-decorator';
import AlertService from '@/shared/alert/alert.service';
import EventoService from '@/entities/evento/evento.service';
import { DATE_FORMAT } from '@/shared/date/filters';
import dayjs from 'dayjs';
import { IFileDocumentoNuevo, IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import UtilsService from '@/shared/services/utils.service';
import GoogleStorageService from '@/shared/services/google-storage.service';
import carpetasarchivosConstants from '@/shared/constants/carpetasarchivos.constants';
import './evento-formulario.scss';

const validations: any = {
  evento: {
    titulo: {
      required,
    },
    cuerpo: {
      required,
      maxLength: maxLength(1000),
      minLength: minLength(1),
    },
    fecha: {
      required,
    },
    /* hora: {
      required,
    }, */
    lugar: {
      required,
    },
    urlFoto: {},
  },
};

@Component({
  validations,
})
export default class EventoFormulario extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('eventoService') private eventoService: () => EventoService;
  @Inject('alertService') private alertService: () => AlertService;

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;
  public showImage = false;
  public showSpinnerLoader = false;

  public imageProfileProfesor: any;
  private file: any = null;
  public programaDocumentoNuevo: IFileDocumentoNuevo = {};

  public dateMax = dayjs().format(DATE_FORMAT);

  public evento: IEvento = new Evento();

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.eventoId) {
        vm.consultarEvento(to.params.eventoId);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
    });
  }

  private consultarEvento(eventoId: number): void {
    this.eventoService()
      .find(eventoId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        res.hora = new Date(res.hora);
        this.evento = res;
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
      if (this.evento.id) {
        this.showImage = false;
        this.uploadFileToStorage(this.file.type, this.evento.id, this.file, this.generateUrlFotoPerfilFolderUpload(this.evento.id + ''));
      }
    }
  }

  private generateUrlFotoPerfilFolderUpload(eventoId: string): string {
    return carpetasarchivosConstants.CARPETA_BASE_EVENTO_FOTO_PERFIL.replace('{{eventoId}}', eventoId + '')
  }

  private downloadImageProfesorPerfil(): void {
    if (this.evento.id && this.evento.urlFoto) {
      if (this.utilsService().existeFileInList(this.evento.urlFoto)) {
        const file: IFileDownloaded = this.utilsService().obtenerFileByFileName(this.evento.urlFoto);
        this.imageProfileProfesor = file.file;
        this.showImage = true;
      } else {
        this.googleStorageService()
          .downloadFileByOnlyFileName(this.$store.getters.authenticated, this.evento.urlFoto)
          .then(res => {
            this.imageProfileProfesor = res;
            this.showImage = true;
            this.utilsService().agregarFileToList(this.evento.urlFoto, res);
          });
      }
    }
  }

  private uploadFileToStorage(contentType: string, noticiaId: number, file: File, nameCarpeta: string): void {
    this.showSpinnerLoader = true;
    this.googleStorageService()
      .uploadFotoEvento(contentType, noticiaId, nameCarpeta, file)
      .then(res => {
        this.evento = res;
        this.showSpinnerLoader = false;
      })
      .catch(err => {
        this.showSpinnerLoader = false;
        this.alertService().showHttpError(this, err.response);
      });
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.evento.id) {
      this.evento.facultad = {
        id: 1,
      };
      this.evento.hora = this.evento.fecha;
      this.eventoService()
        .update(this.evento)
        .then(res => {
          this.isSaving = false;
          this.enableFormularioEditar();
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.evento.updated', { param: res.id });
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
      this.evento.facultad = {
        id: 1,
      };
      this.evento.hora = this.evento.fecha;
      this.eventoService()
        .create(this.evento)
        .then(param => {
          this.evento = param;
          this.isSaving = false;
          if (this.file !== null) {
            this.uploadFileToStorage(
              this.file.type,
              this.evento.id,
              this.file,
              this.generateUrlFotoPerfilFolderUpload(this.evento.id + '')
            );
          }
          this.$router.push({ name: 'evento_lista' });
          // this.$router.go(-1);
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
      this.evento[field] = dayjs(event.target.value, DATE_FORMAT);
    } else {
      this.evento[field] = null;
    }
  }
}
