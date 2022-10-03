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
import SedeService from '@/entities/sede/sede.service';
import { ISede } from '@/shared/model/sede.model';
import UsuarioProfesorFullService from '@/shared/services/usuario-profesor.service';
import { IUsuarioProfesorFull } from '@/shared/model/usuario-profesor.model';
import { IUser } from '@/shared/model/user.model';
import { IProfesor } from '@/shared/model/profesor.model';

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
  @Inject('usuarioProfesorFullService') private usuarioProfesorFullService: () => UsuarioProfesorFullService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('archivosProgramaService') private archivosProgramaService: () => ArchivosProgramaService;
  @Inject('redesProgramaService') private redesProgramaService: () => RedesProgramaService;
  @Inject('sedeService') private sedeService: () => SedeService;

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
  public listaSedes: ISede[] = [];
  public sedeAgregadaId = 0;
  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;
  public isArchivoDocumentoNuevoUploaded = false;
  public isRedSocialCreatedUptaded = false;
  public hasArchivoProgramaPlanEstudio = false;
  public hasArchivoProgramaMicroDiseno = false;
  public dateMax = dayjs().format(DATE_FORMAT);
  private carpetaImagen = '';
  private file: any = null;
  public imageProfilePrograma: any;
  public showImage = false;
  public showSpinnerLoader = false;
  public transaccionError = false;
  public popupDocumentoAccion = '';
  public mensajeError = '';

  public pageProfesores = 1;
  public itemsPerPagePorfesores = 10;
  public totalItemsProfesor = 0;
  public queryCountProfesor: number = null;
  public previousPageProfesor = 1;
  public nameFilterProfesor = '';
  public profesorAgregadoId = 0;

  public usuariosProfesor: IUsuarioProfesorFull[] = [];
  public usuariosUser: IUser[] = [];

  public beforeRouteEnter (to, from, next) {
    next(vm => {
      if (to.params.codigoSnies) {
        vm.consultarPrograma(to.params.codigoSnies);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
      vm.consultarListaProgramas();
      vm.consultarSedes();
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

  private consultarSedes(): void {
    this.listaSedes = [];
    this.sedeService()
      .retrieve()
      .then(res => {
        this.listaSedes = res.data;
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  public filtrarSedesConPrograma(): ISede[] {
    let sedes: ISede[] = [];
    if (this.programa.sedes && this.programa.sedes.length > 0) {
      sedes = this.listaSedes.filter(
        sedeTemp => this.programa.sedes.filter(sedeProgramaTemp => sedeProgramaTemp.id === sedeTemp.id).length < 1
      );
    } else {
      sedes = this.listaSedes;
    }
    return sedes;
  }

  private isValidoTipoDocumento(file: IFileDocumentoNuevo, tiposDocumentosValidos: string[]): boolean {
    return tiposDocumentosValidos.includes(file.file.type);
  }

  public changeAgregarDocumentoNuevo(event): void {
    console.log("data documento");
    console.log(event);
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
      const allowedImageTypes = !this.hasArchivoProgramaPlanEstudio
        ? carpetasarchivosConstants.TIPOS_DOCUMENTOS_PERMITIDOS.filter(type => type.includes('pdf'))
        : carpetasarchivosConstants.TIPOS_DOCUMENTOS_PERMITIDOS; // ['application/pdf'];
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
        this.checkHasArchivoProgramaMicroDiseno();
        let identificadorTemp = 0;
        if (!this.hasArchivoProgramaPlanEstudio) {
          identificadorTemp = identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_PLAN_ESTUDIO_NUMBER;
        } else if (!this.hasArchivoProgramaMicroDiseno) {
          identificadorTemp = identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_MICRO_DISENO_NUMBER;
        } else {
          identificadorTemp = identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_DOCUMENTO_NUMBER;
        }
        this.uploadFileToStorage(
          this.programaDocumentoNuevo.tipoDocumento,
          this.programa.id,
          identificadorTemp,
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

  public eliminar(): void {
    if (this.programa.id) {
      this.programaService()
        .delete(this.programa.id)
        .then(res => {
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.deleted', { param: this.programa.id });
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
    if (this.programa.id) {
      this.programaService()
        .update(this.programa)
        .then(res => {
          this.isSaving = false;
          this.enableFormularioEditar();
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.updated', { param: res.id });
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
      this.programa.facultad = {
        id: 1,
      };
      this.programa.emailContacto = 'email@contact.com';
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
        this.checkHasArchivoProgramaPlanEstudio();
        this.checkHasArchivoProgramaMicroDiseno();
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
        this.consultarSedesPrograma(this.programa.id);
        this.consultarArchivosPrograma(this.programa.id, false);
        this.consultarRedesPrograma(this.programa.id);
        this.consultarProfesoresPrograma(this.programa.codigoSnies);
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public consultarSedesPrograma(programaId: number): void {
    this.sedeService()
      .findAllByProgramaId(this.$store.getters.authenticated, programaId)
      .then(res => {
        this.programa.sedes = res;
      })
      .catch(err => {
        this.programa.sedes = [];
      });
  }

  public consultarArchivosPrograma(programaId: number, isPopup: boolean): void {
    this.archivosProgramaService()
      .getAllByProgramaId(this.$store.getters.authenticated, programaId)
      .then(res => {
        this.archivoProgramaToUpdate = {};
        this.archivosProgramaList = res;
        this.checkHasArchivoProgramaPlanEstudio();
        this.checkHasArchivoProgramaMicroDiseno();
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

  private consultarUsuarioOfProfesor(userId: number): void {
    this.usuarioProfesorFullService()
      .getUsuarioByUserId(this.$store.getters.authenticated, userId)
      .then(res => {
        this.usuariosUser.push(res);
      });
  }

  public filtarNombreProfesor(userId: number): string {
    const usuarioFilter = this.usuariosUser.filter(usuario => usuario.id === userId);
    if (usuarioFilter.length > 0) {
      return usuarioFilter[0].nameComplete;
    } else {
      return '';
    }
  }

  public consultarProfesoresPrograma(codigoSniesPrograma: number): void {
    this.usuarioProfesorFullService()
      .getAllByProgramaCodigoSnies(this.$store.getters.authenticated, codigoSniesPrograma, {
        page: 0,
        size: 100000,
        sort: ['id,asc'],
      })
      .then(res => {
        if (res.data) {
          res.data.map((usu: IUsuarioProfesorFull) => {
            this.programa.profesors.push(usu.profesorDTO);
            this.consultarUsuarioOfProfesor(usu.profesorDTO.userId);
          });
        }
      })
      .catch(err => {
        this.programa.profesors = [];
      });
  }

  public consultarProfesoresByNameComplete(): void {
    this.usuariosProfesor = [];
    const paginacionQuery = {
      page: this.pageProfesores - 1,
      size: this.itemsPerPagePorfesores,
      sort: ['id,asc'],
    };
    this.usuarioProfesorFullService()
      .getAllUsuariosProfesorNameCompleteFiltering(this.$store.getters.authenticated, paginacionQuery, null, this.nameFilterProfesor)
      .then(res => {
        this.totalItemsProfesor = Number(res.headers['x-total-count']);
        this.queryCountProfesor = this.totalItemsProfesor;
        this.usuariosProfesor = res.data;
      });
  }

  public filtrarProfesoresDelPrograma(): IUsuarioProfesorFull[] {
    let usaurios: IUsuarioProfesorFull[] = [];
    if (this.programa.profesors && this.programa.profesors.length > 0) {
      usaurios = this.usuariosProfesor.filter(
        usuario => this.programa.profesors.filter(profesorsTemp => profesorsTemp.id === usuario.profesorDTO.id).length < 1
      );
    } else {
      usaurios = this.usuariosProfesor;
    }
    return usaurios;
  }

  public loadPageProfesores(page: number): void {
    if (page !== this.previousPageProfesor) {
      this.previousPageProfesor = page;
      this.transition();
    }
  }

  public transition(): void {
    this.consultarProfesoresByNameComplete();
  }

  public eliminarProfesorToPrograma(profesor: IProfesor): void {
    this.profesorAgregadoId = profesor.id;
    if (this.programa.id) {
      const indexSedePrograma = this.programa.profesors.indexOf(profesor);
      this.programa.profesors.splice(indexSedePrograma, 1);
      this.programaService()
        .update(this.programa)
        .then(res => {
          this.programa = res;
          this.profesorAgregadoId = 0;
          this.consultarProfesoresPrograma(this.programa.codigoSnies);
        })
        .catch(() => {
          this.programa.profesors.push(profesor);
          this.profesorAgregadoId = 0;
        });
    } else {
      if (this.programa.profesors) {
        const indexSedePrograma = this.programa.profesors.indexOf(profesor);
        this.programa.profesors.splice(indexSedePrograma, 1);
        this.profesorAgregadoId = 0;
      } else {
        this.profesorAgregadoId = 0;
      }
    }
  }

  public agregarProfesorToPrograma(profeosr: IUsuarioProfesorFull): void {
    this.profesorAgregadoId = profeosr.profesorDTO.id;
    if (this.programa.id) {
      this.programa.profesors.push(profeosr.profesorDTO);
      this.programaService()
        .update(this.programa)
        .then(res => {
          this.programa = res;
          this.profesorAgregadoId = 0;
          this.consultarProfesoresPrograma(this.programa.codigoSnies);
        })
        .catch(() => {
          this.profesorAgregadoId = 0;
          const indexSedeTemp = this.programa.profesors.indexOf(profeosr.profesorDTO);
          this.programa.profesors.splice(indexSedeTemp, 1);
        });
    } else {
      if (this.programa.profesors) {
        this.programa.profesors.push(profeosr.profesorDTO);
        this.sedeAgregadaId = 0;
      } else {
        this.programa.profesors = [profeosr.profesorDTO];
        this.sedeAgregadaId = 0;
      }
    }
  }

  /*public filtrarProfesoresDuplicados(profesores: IProfesor[]): IProfesor[] {
    const unique: IProfesor[] = [];
    console.log(profesores);

    profesores.map(x => {
      console.log("filter " + x.id);
      console.log(unique.filter(a => a.id === x.id));
      if (unique.filter(a => a.id === x.id).length < 1) {
        unique.push(x);
      }
    });
    return unique;
  }*/

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

  public checkHasArchivoProgramaMicroDiseno(): void {
    const planEstudio = this.archivosProgramaList.filter(
      archivo => archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_MICRO_DISENO_NUMBER
    );
    if (planEstudio.length > 0) {
      this.hasArchivoProgramaMicroDiseno = true;
    } else {
      this.hasArchivoProgramaMicroDiseno = false;
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
        archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_PLAN_ESTUDIO_NUMBER ||
        archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_MICRO_DISENO_NUMBER
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

  public eliminarSedeToPrograma(sede: ISede): void {
    this.sedeAgregadaId = sede.id;
    if (this.programa.id) {
      const indexSedePrograma = this.programa.sedes.indexOf(sede);
      this.programa.sedes.splice(indexSedePrograma, 1);
      this.programaService()
        .update(this.programa)
        .then(res => {
          this.programa = res;
          this.sedeAgregadaId = 0;
          this.consultarSedesPrograma(this.programa.id);
        })
        .catch(() => {
          this.programa.sedes.push(sede);
          this.sedeAgregadaId = 0;
        });
    } else {
      if (this.programa.sedes) {
        const indexSedePrograma = this.programa.sedes.indexOf(sede);
        this.programa.sedes.splice(indexSedePrograma, 1);
        this.sedeAgregadaId = 0;
      } else {
        this.sedeAgregadaId = 0;
      }
    }
  }

  public agregarSedeToPrograma(sede: ISede): void {
    this.sedeAgregadaId = sede.id;
    if (this.programa.id) {
      this.programa.sedes.push(sede);
      this.programaService()
        .update(this.programa)
        .then(res => {
          this.programa = res;
          this.sedeAgregadaId = 0;
          this.consultarSedesPrograma(this.programa.id);
        })
        .catch(() => {
          this.sedeAgregadaId = 0;
          const indexSedeTemp = this.programa.sedes.indexOf(sede);
          this.programa.sedes.splice(indexSedeTemp, 1);
        });
    } else {
      if (this.programa.sedes) {
        this.programa.sedes.push(sede);
        this.sedeAgregadaId = 0;
      } else {
        this.programa.sedes = [sede];
        this.sedeAgregadaId = 0;
      }
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

  public validarUrlRedSocial(): boolean {
    return (
      this.redesPrograma.urlRedSocial === null || this.redesPrograma.urlRedSocial === undefined || this.redesPrograma.urlRedSocial === ''
    );
  }

  public validarTipoRedSocial(): boolean {
    return (
      this.redesPrograma.tablaElementoCatalogo === null ||
      this.redesPrograma.tablaElementoCatalogo === undefined ||
      this.redesPrograma.tablaElementoCatalogo.id === null ||
      this.redesPrograma.tablaElementoCatalogo.id === undefined
    );
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

  public openPopupAgregarProfesor(): void {
    if (<any>this.$refs.modalPopupAgregarProfesores) {
      (<any>this.$refs.modalPopupAgregarProfesores).show();
    }
  }

  public openPopupAgregarNuevaRedSocial(): void {
    this.isRedSocialCreatedUptaded = false;
    if (<any>this.$refs.modalPopupAgregarRedSocial) {
      (<any>this.$refs.modalPopupAgregarRedSocial).show();
    }
  }

  public openPopupAgregarSede(): void {
    if (<any>this.$refs.modalPopupAgregarSede) {
      (<any>this.$refs.modalPopupAgregarSede).show();
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

  public closePopupAgregarProfesor(): void {
    (<any>this.$refs.modalPopupAgregarProfesores).hide();
    this.profesorAgregadoId = 0;
  }

  public closePopupAgregarSede(): void {
    (<any>this.$refs.modalPopupAgregarSede).hide();
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
    this.closePopupAgregarProfesor();
    this.mensajeError = '';
  }
}
