import { IPrograma, Programa } from '@/shared/model/programa.model';
import { required, numeric, decimal, maxLength } from 'vuelidate/lib/validators';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './programa-formulario.scss';
import ProgramaService from '@/entities/programa/programa.service';
import AlertService from '@/shared/alert/alert.service';
import DatePicker from 'vue2-datepicker';
import 'vue2-datepicker/index.css';
import dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';
import carpetasarchivosConstants from '@/shared/constants/carpetasarchivos.constants';
import GoogleStorageService from '@/shared/services/google-storage.service';
import ArchivosProgramaService from '@/entities/archivos-programa/archivos-programa.service';
import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';
import { IFileDocumentoNuevo } from '@/shared/model/file-documento-nuevo.model';
import UtilsService from '@/shared/services/utils.service';
import { IRedesPrograma, RedesPrograma } from '@/shared/model/redes-programa.model';
import RedesProgramaService from '@/entities/redes-programa/redes-programa.service';

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
      maxLength: maxLength(255),
    },
    mision: {
      required,
      maxLength: maxLength(255),
    },
    vision: {
      required,
      maxLength: maxLength(255),
    },
    perfilEstudiante: {
      maxLength: maxLength(255),
    },
    perfilEgresado: {
      required,
      maxLength: maxLength(255),
    },
    perfilOcupacional: {
      maxLength: maxLength(255),
    },
    urlFotoPrograma: {
      maxLength: maxLength(255),
    },
    dirigidoAQuien: {
      maxLength: maxLength(255),
    },
    costoPrograma: {
      required,
      decimal,
    },
    nivelFormacion: {
      required,
    },
    tipoFormacion: {
      required,
    },
  },
  redesPrograma: {
    urlRedSocial: {
      required,
    },
    tablaElementoCatalogo: {
      required,
    },
  },
};

@Component({
  components: {
    DatePicker,
  },
  validations,
})
export default class ProgramaFormulario extends Vue {
  @Inject('programaService') private programaService: () => ProgramaService;
  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('archivosProgramaService') private archivosProgramaService: () => ArchivosProgramaService;
  @Inject('redesProgramaService') private redesProgramaService: () => RedesProgramaService;

  public programa: IPrograma = new Programa();
  public redesPrograma: IRedesPrograma = new RedesPrograma();

  public POPUP_DOCUMENTO_ACCION_CREAR = 'CREAR';
  public POPUP_DOCUMENTO_ACCION_ACTUALIZAR = 'ACTUALIZAR';

  private listTiposPrograma: ITablaElementoCatalogo[] = [];
  private listTiposFormacion: ITablaElementoCatalogo[] = [];
  private archivosProgramaList: IArchivosPrograma[] = [];
  private archvivoProgramaImageProfile: IArchivosPrograma = {};
  private archivoProgramaToUpdate: IArchivosPrograma = {};
  private archivosProgramaDescargados: IFileDocumentoNuevo[] = [];
  public programaDocumentoNuevo: IFileDocumentoNuevo = {};
  public tiposRedSocialElemento: ITablaElementoCatalogo[] = [];
  public redesSocialesPrograma: IRedesPrograma[] = [];
  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;
  public isArchivoDocumentoNuevoUploaded = false;
  public isRedSocialCreatedUptaded = false;
  public hasArchivoProgramaPlanEstudio = false;
  public dateMax = dayjs().format(DATE_FORMAT);
  private carpetaImagen = '';
  private file: any = null;
  public imageProfilePrograma: any;
  public showImage = false;
  public showSpinnerLoader = false;
  public transaccionError = false;
  public popupDocumentoAccion = '';
  public mensajeError = '';

  /* public created(): void {
    this.consultarListaProgramas();
    this.consultarTipoFormacion();
  } */

  public beforeRouteEnter (to, from, next) {
    next(vm => {
      if (to.params.codigoSnies) {
        vm.consultarPrograma(to.params.codigoSnies);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
      vm.consultarListaProgramas();
      vm.consultarTipoFormacion();
      vm.consultarTiposRedSocial();
    });
  }

  private consultarListaProgramas(): void {
    this.listTiposPrograma = [];
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_TIPO_PROGRAMA)
      .then(res => {
        this.listTiposPrograma = res;
      });
  }

  private consultarTipoFormacion(): void {
    this.listTiposFormacion = [];
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_TIPO_FORMACION)
      .then(res => {
        this.listTiposFormacion = res;
      });
  }

  private consultarTiposRedSocial(): void {
    this.tiposRedSocialElemento = [];
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_TIPO_RED_SOCIAL)
      .then(res => {
        this.tiposRedSocialElemento = res;
      });
  }

  private isValidoTipoDocumento(file: IFileDocumentoNuevo, tiposDocumentosValidos: string[]): boolean {
    return tiposDocumentosValidos.includes(file.file.type);
  }

  public changeAgregarDocumentoNuevo(event): void {
    this.programaDocumentoNuevo = {};
    this.mensajeError = '';
    if (event.target.files && event.target.files.length > 0) {
      this.programaDocumentoNuevo = {
        file: event.target.files[0],
        nombre: event.target.files[0].name,
        size: event.target.files[0].size,
        tipoDocumento: event.target.files[0].type,
        isValidDoc: false,
      };
      const allowedImageTypes = ['application/pdf'];
      const sizeMaxFile = 15728640;
      if (!this.isValidoTipoDocumento(this.programaDocumentoNuevo, allowedImageTypes)) {
        console.log("tipo no permitido");
        this.mensajeError = this.$t('archivosPrograma.errors.tipyFileInvalid', {
          filesValid: allowedImageTypes.map(type => this.utilsService().changeTypeFileToString(type)).join(', '),
        }).toString();
        return;
      }
      if (this.programaDocumentoNuevo.file.size > sizeMaxFile) {
        this.mensajeError = this.$t('archivosPrograma.errors.sizeMax', { sizeMax: this.utilsService().sizeToMB(sizeMaxFile) }).toString();
        return;
      }
      this.programaDocumentoNuevo.isValidDoc = true;
      const fileReader = new FileReader();
      fileReader.readAsDataURL(this.programaDocumentoNuevo.file);
      fileReader.onload = () => {
        this.programaDocumentoNuevo.fileBase64 = fileReader.result;
      };
    }
  }

  public changeImage(event): void {
    console.log(event.target.files);
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      const allowedImageTypes = ['image/png', 'image/jpeg', 'image/jpg'];
      if (!allowedImageTypes.includes(file.type)) {
        console.log("tipo no permitido");
        return;
      }
      this.file = file;
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = () => {
        this.imageProfilePrograma = fileReader.result;
        this.showImage = true;
      };
      console.log(this.file.type);
      if (this.programa.id) {
        this.showImage = false;
        if (this.archvivoProgramaImageProfile.id) {
          this.updateFileToStorage(
            this.file.type,
            this.generateUrlFolderUpload(this.programa.codigoSnies + '', this.programa.nombre, false),
            this.archvivoProgramaImageProfile.id,
            this.file
          );
        } else {
          this.uploadFileToStorage(
            this.file.type,
            this.programa.id,
            identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_IMAGE_NUMBER,
            this.file,
            this.generateUrlFolderUpload(this.programa.codigoSnies + '', this.programa.nombre, false)
          );
        }
      }
    }
  }

  public subirDocumentoNuevo(): void {
    if (this.programa.id) {
      if (this.popupDocumentoAccion === this.POPUP_DOCUMENTO_ACCION_CREAR) {
        this.checkHasArchivoProgramaPlanEstudio();
        this.uploadFileToStorage(
          this.programaDocumentoNuevo.tipoDocumento,
          this.programa.id,
          this.hasArchivoProgramaPlanEstudio
            ? identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_DOCUMENTO_NUMBER
            : identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_PLAN_ESTUDIO_NUMBER,
          this.programaDocumentoNuevo.file,
          this.generateUrlFolderUpload(this.programa.codigoSnies + '', this.programa.nombre, true),
          true
        );
      } else if (this.popupDocumentoAccion === this.POPUP_DOCUMENTO_ACCION_ACTUALIZAR && this.archivoProgramaToUpdate.id) {
        this.updateFileToStorage(
          this.programaDocumentoNuevo.tipoDocumento,
          this.generateUrlFolderUpload(this.programa.codigoSnies + '', this.programa.nombre, true),
          this.archivoProgramaToUpdate.id,
          this.programaDocumentoNuevo.file,
          true
        );
      }
    }
  }

  public countCharacter(maxTamano: number, value: string): number {
    return maxTamano - (value ? value.trim().length : 0);
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.programa.id) {
      this.programaService()
        .update(this.programa)
        .then(res => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.updated', { param: res.id });
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
      this.programa.facultad = {
        id: 1,
      };
      this.programaService()
        .create(this.programa)
        .then(res => {
          this.isSaving = false;
          if (this.file !== null) {
            this.uploadFileToStorage(
              this.file.type,
              res.id,
              identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_IMAGE_NUMBER,
              this.file,
              this.generateUrlFolderUpload(this.programa.codigoSnies + '', this.programa.nombre, false)
            );
          }
          this.$router.push({ name: 'usuario_programas_lista' });
          // this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.created', { param: res.id });
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

  private uploadFileToStorage(contentType: string, programaId: number, elementoCatalogoId: number, file: File, nameCarpeta: string, isPopup?: boolean): void {
    this.showSpinnerLoader = true;
    this.isArchivoDocumentoNuevoUploaded = true;
    this.googleStorageService()
      .uploadProgramaFile(contentType, programaId, elementoCatalogoId, nameCarpeta, file)
      .then(res1 => {
        console.log(res1);
        this.archivosProgramaList.push(res1);
        console.log('archivos');
        console.log(this.archivosProgramaList);
        this.showSpinnerLoader = false;
        this.isArchivoDocumentoNuevoUploaded = false;
        if (isPopup && isPopup === true) {
          this.closeAllPopups();
        }
      })
      .catch(err => {
        console.error(err);
        this.showSpinnerLoader = false;
        this.isArchivoDocumentoNuevoUploaded = false;
        this.transaccionError = true;
        if (isPopup && isPopup === true) {
          this.closeAllPopups();
        }
        this.alertService().showHttpError(this, err.response);
      });
  }

  public updateFileToStorage(contentType: string, carpetaName: string, archivoProgramaId: number, file: File, isPopup?: boolean): void {
    this.googleStorageService()
      .updateFileArchivoPrograma(contentType, carpetaName, archivoProgramaId, file)
      .then(res => {
        this.archivosProgramaList.map(archivo => {
          if (archivo.id === archivoProgramaId) {
            archivo.generationStorage = res.generationStorage;
            archivo.urlName = res.urlName;
          }
        });
        this.consultarArchivosPrograma(this.programa.id, isPopup);
        if (isPopup && isPopup === true) {
          this.closeAllPopups();
        }
      })
      .catch(err => {
        console.error("error actualizando archivo");
        console.error(err);
        if (isPopup && isPopup === true) {
          this.closeAllPopups();
        }
        this.alertService().showHttpError(this, err.response);
      });
  }

  private generateUrlFolderUpload(codigoSnies: string, namePrograma: string, isDocumento: boolean): string {
    namePrograma = namePrograma.replace(' ', '-');
    if (isDocumento) {
      return carpetasarchivosConstants.CARPETA_BASE_PROGRAMA_DOCUMENTOS.replace('{{snies}}', codigoSnies + '').replace(
        '{{name_programa}}',
        namePrograma
      );
    } else {
      return carpetasarchivosConstants.CARPETA_BASE_PROGRAMA_IMAGES.replace('{{snies}}', codigoSnies + '').replace(
        '{{name_programa}}',
        namePrograma
      );
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.programa[field] = dayjs(event.target.value, DATE_FORMAT);
    } else {
      this.programa[field] = null;
    }
  }

  public consultarPrograma(programaId: number): void {
    this.programaService()
      .findByCodigoSnies(this.$store.getters.authenticated, Number(programaId))
      .then(res => {
        res.fechaRegistroCalificado = new Date(res.fechaRegistroCalificado);
        this.programa = res;
        this.consultarArchivosPrograma(this.programa.id, false);
        this.consultarRedesPrograma(this.programa.id);
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public consultarArchivosPrograma(programaId: number, isPopup: boolean): void {
    this.archivosProgramaService()
      .getAllByProgramaId(this.$store.getters.authenticated, programaId)
      .then(res => {
        this.archivoProgramaToUpdate = {};
        this.archivosProgramaList = res;
        this.checkHasArchivoProgramaPlanEstudio();
        if (!isPopup) {
          this.downloadImageProgramaPerfil();
        }
      })
      .catch(err => {
        console.error("Errore obteniendo archivos");
        console.error(err);
        this.alertService().showHttpError(this, err.response);
      });
  }

  public consultarRedesPrograma(programaId: number): void {
    this.redesProgramaService()
      .findAllByProgramaId(this.$store.getters.authenticated, programaId)
      .then(res => {
        this.redesSocialesPrograma = res;
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  public agregarActualizarRedSocial(): void {
    if (this.programa.id) {
      this.isRedSocialCreatedUptaded = true;
      if (this.redesPrograma.id) {
        this.redesProgramaService()
          .update(this.redesPrograma)
          .then(res => {
            this.redesPrograma = {};
            this.isRedSocialCreatedUptaded = false;
            this.consultarRedesPrograma(this.programa.id);
            this.closeAllPopups();
          })
          .catch(err => {
            this.isRedSocialCreatedUptaded = false;
            this.alertService().showHttpError(this, err.response);
            this.closeAllPopups();
          });
      } else {
        this.redesPrograma.programa = {
          id: this.programa.id,
        };
        this.redesProgramaService()
          .create(this.redesPrograma)
          .then(res => {
            this.redesPrograma = {};
            this.redesSocialesPrograma.push(res);
            this.isRedSocialCreatedUptaded = false;
            this.closeAllPopups();
          })
          .catch(err => {
            this.isRedSocialCreatedUptaded = false;
            this.alertService().showHttpError(this, err.response);
            this.closeAllPopups();
          });
      }
    }
  }

  public checkHasArchivoProgramaPlanEstudio(): void {
    const planEstudio = this.archivosProgramaList.filter(
      archivo => archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_PLAN_ESTUDIO_NUMBER
    );
    if (planEstudio.length > 0) {
      this.hasArchivoProgramaPlanEstudio = true;
    } else {
      this.hasArchivoProgramaPlanEstudio = false;
    }
  }

  private downloadImageProgramaPerfil(): void {
    const archivoProgramaImage = this.archivosProgramaList.filter(
      archivo => archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_IMAGE_NUMBER
    );
    if (archivoProgramaImage.length > 0) {
      this.archvivoProgramaImageProfile = archivoProgramaImage[0];
      this.googleStorageService()
        .downloadFile(this.$store.getters.authenticated, archivoProgramaImage[0].urlName, archivoProgramaImage[0].generationStorage)
        .then(res => {
          this.imageProfilePrograma = res;
          this.showImage = true;
        })
        .catch(err => {
          console.log(err);
        });
    }
  }

  public filtrarArchivosProgramaOnlyDocs(archivosProgramaList: IArchivosPrograma[]): IArchivosPrograma[] {
    return archivosProgramaList.filter(
      archivo =>
        archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_DOCUMENTO_NUMBER ||
        archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_PLAN_ESTUDIO_NUMBER
    );
  }

  public cambiarArchivoPrograma(archivProgram: IArchivosPrograma): void {
    this.archivoProgramaToUpdate = archivProgram;
    this.openPopupCrearDocumentoNuevo(this.POPUP_DOCUMENTO_ACCION_ACTUALIZAR);
  }

  public checkHabilitacionCampos(): boolean {
    return !this.enableEdit;
  }

  public enableFormularioEditar(): void {
    this.enableEdit = !this.enableEdit;
  }

  public cancelarAccion(): void {
    if (this.isModeEdit) {
      this.enableEdit = !this.enableEdit;
    } else {
      this.$router.go(-1);
    }
  }

  public eliminarArchivoPrograma(): void {
    this.isArchivoDocumentoNuevoUploaded = true;
    this.googleStorageService()
      .deleteArchivoProgramaUploaded(this.programaDocumentoNuevo.archivoDocumentoPrograma.id)
      .then(() => {
        this.consultarArchivosPrograma(this.programa.id, true);
        this.programaDocumentoNuevo = {};
        this.isArchivoDocumentoNuevoUploaded = false;
        this.closeAllPopups();
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
        this.programaDocumentoNuevo = {};
        this.isArchivoDocumentoNuevoUploaded = false;
        this.closeAllPopups();
      });
  }

  private convertirFileDownloadedToBlobNewTab(fileBase64: string): void {
    this.utilsService()
      .addHeaderBase64(fileBase64)
      .then(resHeaderBase64 => {
        fetch(resHeaderBase64).then(res => {
          res.blob().then(resBlob => {
            const url = URL.createObjectURL(resBlob);
            window.open(url, '_blank');
          });
        });
      });
  }

  public verArchivoPrograma(archivoProgra: IArchivosPrograma): void {
    if (archivoProgra.urlName) {
      const archivoDescargadoFiltrado = this.archivosProgramaDescargados.filter(
        archivo => archivo.archivoDocumentoPrograma.id === archivoProgra.id
      );
      if (archivoDescargadoFiltrado.length > 0) {
        this.convertirFileDownloadedToBlobNewTab(archivoDescargadoFiltrado[0].fileBase64.toString());
      } else {
        this.googleStorageService()
          .downloadFile(this.$store.getters.authenticated, archivoProgra.urlName, archivoProgra.generationStorage)
          .then(res => {
            this.archivosProgramaDescargados.push({
              tipoDocumento: archivoProgra.tipoDocumento,
              fileBase64: res,
              archivoDocumentoPrograma: archivoProgra,
            });
            this.convertirFileDownloadedToBlobNewTab(res);
          })
          .catch(err => {
            this.alertService().showHttpError(this, err.response);
          });
      }
    }
  }

  public openPopupActualizarDocumento(accionDocumento: string, archivoProgramaToUpdate: IArchivosPrograma): void {
    this.archivoProgramaToUpdate = archivoProgramaToUpdate;
    this.openPopupCrearDocumentoNuevo(accionDocumento);
  }

  public openPopupEliminarDocumento(archivoPrograma: IArchivosPrograma): void {
    this.programaDocumentoNuevo = {};
    if (<any>this.$refs.modalPopupEliminarDocumentoPrograma) {
      (<any>this.$refs.modalPopupEliminarDocumentoPrograma).show();
      this.programaDocumentoNuevo = {
        isValidDoc: true,
        archivoDocumentoPrograma: archivoPrograma,
        nombre: archivoPrograma.nombreArchivo,
      };
    }
  }

  public openPopupEditarRedSocial(redSocial: IRedesPrograma): void {
    this.redesPrograma = redSocial;
    this.openPopupAgregarNuevaRedSocial();
  }

  public openPopupAgregarNuevaRedSocial(): void {
    this.isRedSocialCreatedUptaded = false;
    if (<any>this.$refs.modalPopupAgregarRedSocial) {
      (<any>this.$refs.modalPopupAgregarRedSocial).show();
    }
  }

  public openPopupCrearDocumentoNuevo(accionDocumento: string): void {
    this.popupDocumentoAccion = '';
    if (<any>this.$refs.modalPopupCrearDocumentoPrograma) {
      (<any>this.$refs.modalPopupCrearDocumentoPrograma).show();
      this.programaDocumentoNuevo = {};
      this.popupDocumentoAccion = accionDocumento;
    }
  }

  public closePopupEliminarDocumentoPrograma(): void {
    (<any>this.$refs.modalPopupEliminarDocumentoPrograma).hide();
    this.archivoProgramaToUpdate = {};
    this.programaDocumentoNuevo = {};
  }

  public closePopupAgregarRedSocial(): void {
    (<any>this.$refs.modalPopupAgregarRedSocial).hide();
    this.redesPrograma = {};
    this.isRedSocialCreatedUptaded = false;
  }

  public closePopupCrearDocumentoNuevo(): void {
    (<any>this.$refs.modalPopupCrearDocumentoPrograma).hide();
    this.programaDocumentoNuevo = {};
    this.archivoProgramaToUpdate = {};
    this.popupDocumentoAccion = '';
  }

  public closeAllPopups(): void {
    this.closePopupCrearDocumentoNuevo();
    this.closePopupEliminarDocumentoPrograma();
    this.closePopupAgregarRedSocial();
    this.mensajeError = '';
  }
}
