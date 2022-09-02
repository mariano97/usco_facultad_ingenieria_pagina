import { DATE_FORMAT_MONTH } from './../../../shared/date/filters';
import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';
import EscalafonProfesorService from '@/entities/escalafon-profesor/escalafon-profesor.service';
import PaisesService from '@/entities/paises/paises.service';
import ProfesorService from '@/entities/profesor/profesor.service';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import TituloAcademicoProfesorService from '@/entities/titulo-academico-profesor/titulo-academico-profesor.service';
import AlertService from '@/shared/alert/alert.service';
import carpetasarchivosConstants from '@/shared/constants/carpetasarchivos.constants';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';
import { ICursoMateria } from '@/shared/model/curso-materia.model';
import { EscalafonProfesor, IEscalafonProfesor } from '@/shared/model/escalafon-profesor.model';
import { IFileDocumentoNuevo, IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { IPaises } from '@/shared/model/paises.model';
import { IProfesor, Profesor } from '@/shared/model/profesor.model';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { ITituloAcademicoProfesor, TituloAcademicoProfesor } from '@/shared/model/titulo-academico-profesor.model';
import { IUser, User } from '@/shared/model/user.model';
import { IUsuarioProfesorFull, UsuarioProfesorFull } from '@/shared/model/usuario-profesor.model';
import { Authority } from '@/shared/security/authority';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UsuarioProfesorFullService from '@/shared/services/usuario-profesor.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Component, Inject, Vue } from 'vue-property-decorator';
import { maxLength, minLength, required, email } from 'vuelidate/lib/validators';
import './profesor-formulario.scss';

const validations: any = {
  userAccount: {
    firstName: {
      required,
      minLength: minLength(5),
      maxLength: maxLength(50),
    },
    secondName: {
      maxLength: maxLength(50),
    },
    lastName: {
      required,
      minLength: minLength(5),
      maxLength: maxLength(50),
    },
    email: {
      required,
      email,
      minLength: minLength(10),
      maxLength: maxLength(50),
    },
  },
  profesor: {
    emailAlternativo: {
      email,
      maxLength: maxLength(50),
    },
    perfil: {
      required,
      minLength: minLength(10),
      maxLength: maxLength(255),
    },
    telefonoCelular: {
      maxLength: maxLength(10),
    },
    tablaElementoCatalogo: {
      required,
    },
    oficina: {
      maxLength: maxLength(255),
    },
  },
  tituloAcademicoProfesor: {
    nombreTitulo: {
      required,
      minLength: minLength(5),
    },
    nombreUniversidadTitulo: {
      required,
      minLength: minLength(5),
    },
    yearTitulo: {
      required,
    },
    tablaElementoCatalogo: {
      required,
    },
    profesor: {},
    paises: {
      required,
    },
  },
};

@Component({
  validations: validations,
})
export default class ProfesorFormulario extends Vue {
  @Inject('profesorService') private profesorService: () => ProfesorService;
  @Inject('usuarioProfesorFullService') private usuarioProfesorFullService: () => UsuarioProfesorFullService;
  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('tituloAcademicoProfesorService') private tituloAcademicoProfesorService: () => TituloAcademicoProfesorService;
  @Inject('escalafonProfesorService') private escalafonProfesorService: () => EscalafonProfesorService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('cursoMateriaService') private cursoMateriaService: () => CursoMateriaService;
  @Inject('paisesService') private paisesService: () => PaisesService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('utilsService') private utilsService: () => UtilsService;

  public usuarioProfesor: IUsuarioProfesorFull = new UsuarioProfesorFull();
  public userAccount: IUser = new User();
  public profesor: IProfesor = new Profesor();
  public tituloAcademicoProfesor: ITituloAcademicoProfesor = new TituloAcademicoProfesor();

  public escalafonProfesor: IEscalafonProfesor = new EscalafonProfesor();
  public escalafonoesProfesor: IEscalafonProfesor[] = [];

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;
  public isSaveTituloAcademico = false;
  public isAgregarCurso = false;
  public isAgregarEscalafon = false;
  public showImage = false;
  public showSpinnerLoader = false;

  public imageProfileProfesor: any;
  private file: any = null;
  public programaDocumentoNuevo: IFileDocumentoNuevo = {};

  public tiposProfesoresElemento: ITablaElementoCatalogo[] = [];
  public listaPaises: IPaises[] = [];
  public listaTiposTitulosAcademicos: ITablaElementoCatalogo[] = [];
  public listaCursosMaterias: ICursoMateria[] = [];

  public titulosAcademicosProfesorLista: ITituloAcademicoProfesor[] = [];

  public cursoMateriaSeleccionado: ICursoMateria = {};

  public dateMax = dayjs().format(DATE_FORMAT);

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.userLogin) {
        vm.consultarUsuarioProfesor(to.params.userLogin);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
      vm.consultarTiposProfesores();
      vm.consultarPaises();
      vm.consultarTipoTitulosAcademicos();
      vm.consultarCursosMaterias();
    });
  }

  public consultarUsuarioProfesor(userLogin: string): void {
    this.usuarioProfesorFullService()
      .getOneUsuarioProfesorByUserLogin(this.$store.getters.authenticated, userLogin)
      .then(res => {
        this.userAccount = res.adminUserDTO;
        this.profesor = res.profesorDTO;
        this.downloadImageProfesorPerfil();
        this.consultarTitulosAcademicosProfesor(this.profesor.id);
        this.consultarCursosMateriaProfesor(this.profesor.id);
        this.consultarEscalafonesProfesor(this.profesor.id);
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  private consultarTitulosAcademicosProfesor(profesorId: number): void {
    this.titulosAcademicosProfesorLista = [];
    this.tituloAcademicoProfesorService()
      .findAllByProfesorId(this.$store.getters.authenticated, profesorId)
      .then(res => {
        this.titulosAcademicosProfesorLista = res;
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  private consultarTiposProfesores(): void {
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_TIPO_PROFESOR)
      .then(res => {
        this.tiposProfesoresElemento = res;
      });
  }

  private consultarTipoTitulosAcademicos(): void {
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_TIPO_TITULO_ACADEMICO)
      .then(res => {
        this.listaTiposTitulosAcademicos = res;
      });
  }

  private consultarPaises(): void {
    this.paisesService()
      .retrieve()
      .then(res => {
        this.listaPaises = res.data;
      });
  }

  private consultarCursosMaterias(): void {
    this.cursoMateriaService()
      .retrieve()
      .then(res => {
        this.listaCursosMaterias = res.data;
      });
  }

  private consultarCursosMateriaProfesor(profesorId: number): void {
    this.profesor.cursoMaterias = [];
    this.cursoMateriaService()
      .findAllByProfesorId(this.$store.getters.authenticated, profesorId)
      .then(res => {
        this.profesor.cursoMaterias = res;
      })
      .catch(err => {
        this.profesor.cursoMaterias = [];
      });
  }

  private consultarEscalafonesProfesor(profesorId: number): void {
    this.escalafonoesProfesor = [];
    this.escalafonProfesorService()
      .findByProfesorId(this.$store.getters.authenticated, profesorId)
      .then(res => {
        this.escalafonoesProfesor = res;
      })
      .catch(err => {
        this.escalafonoesProfesor = [];
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
      if (this.profesor.id) {
        this.showImage = false;
        this.uploadFileToStorage(
          this.file.type,
          this.profesor.id,
          this.file,
          this.generateUrlFotoPerfilFolderUpload(this.userAccount.login + '', this.userAccount.id),
          false
        );
      }
    }
  }

  private downloadImageProfesorPerfil(): void {
    if (this.profesor.id && this.profesor.urlFotoProfesor) {
      if (this.utilsService().existeFileInList(this.profesor.urlFotoProfesor)) {
        const file: IFileDownloaded = this.utilsService().obtenerFileByFileName(this.profesor.urlFotoProfesor);
        this.imageProfileProfesor = file.file;
        this.showImage = true;
      } else {
        this.googleStorageService()
          .downloadFileByOnlyFileName(this.$store.getters.authenticated, this.profesor.urlFotoProfesor)
          .then(res => {
            this.imageProfileProfesor = res;
            this.showImage = true;
            this.utilsService().agregarFileToList(this.profesor.urlFotoProfesor, res);
          });
      }
    }
  }

  private uploadFileToStorage(contentType: string, profesorId: number, file: File, nameCarpeta: string, isPopup?: boolean): void {
    this.showSpinnerLoader = true;
    this.googleStorageService()
      .uploadFotoPerfilProfesor(contentType, profesorId, nameCarpeta, file)
      .then(res => {
        this.profesor = res;
        this.showSpinnerLoader = false;
        if (isPopup && isPopup === true) {
          this.closeAllPopups();
        }
      })
      .catch(err => {
        this.showSpinnerLoader = false;
        if (isPopup && isPopup === true) {
          this.closeAllPopups();
        }
        this.alertService().showHttpError(this, err.response);
      });
  }

  private generateUrlFotoPerfilFolderUpload(userLogin: string, userId: number): string {
    userLogin = userLogin.replace(' ', '-');
    return carpetasarchivosConstants.CARPETA_BASE_PROFESOR_FOTO_PERFIL.replace('{{login}}', userLogin + '').replace(
      '{{userId}}',
      userId + ''
    );
  }

  public eliminar(): void {
    if (this.profesor.id) {
      this.profesorService()
        .delete(this.profesor.id)
        .then(res => {
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.profesor.deleted', { param: this.profesor.id });
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
    if (this.userAccount.id && this.profesor.id) {
      this.profesor.facultad = {
        id: 1,
      };
      const usuarioProfesor: IUsuarioProfesorFull = {
        adminUserDTO: this.userAccount,
        profesorDTO: this.profesor,
      };
      this.usuarioProfesorFullService()
        .updateUsuarioProfesor(usuarioProfesor)
        .then(res => {
          this.userAccount = res.adminUserDTO;
          this.profesor = res.profesorDTO;
          this.enableFormularioEditar();
          this.$router.go(-1);
          const message = this.$t('userManagement.updated', { param: res.adminUserDTO.email });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(err => {
          this.alertService().showHttpError(this, err.response);
        });
    } else {
      this.userAccount.login = this.utilsService().generateUUIDIdentifcator();
      // this.userAccount.authorities = [Authority.JEFE_PROGRAMA, Authority.PROFESOR, Authority.USER];
      this.userAccount.authorities = [Authority.PROFESOR, Authority.USER];
      this.profesor.facultad = {
        id: 1,
      };
      const usuarioProfesor: IUsuarioProfesorFull = {
        adminUserDTO: this.userAccount,
        profesorDTO: this.profesor,
      };
      this.usuarioProfesorFullService()
        .crearUsuarioProfesor(usuarioProfesor)
        .then(res => {
          this.userAccount = res.adminUserDTO;
          this.profesor = res.profesorDTO;
          if (this.file !== null) {
            this.uploadFileToStorage(
              this.file.type,
              this.profesor.id,
              this.file,
              this.generateUrlFotoPerfilFolderUpload(this.userAccount.login + '', this.userAccount.id)
            );
          }
          this.$router.push({ name: 'usuario_profesores_lista' });
          const message = this.$t('userManagement.created', { param: res.adminUserDTO.email });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(err => {
          this.alertService().showHttpError(this, err.response);
        });
    }
  }

  public guardarTituloAcademico(): void {
    this.isSaveTituloAcademico = true;
    if (this.tituloAcademicoProfesor.id) {
      this.tituloAcademicoProfesorService()
        .update(this.tituloAcademicoProfesor)
        .then(res => {
          this.isSaveTituloAcademico = false;
          this.closeAllPopups();
          this.consultarTitulosAcademicosProfesor(this.profesor.id);
        })
        .catch(err => {
          this.alertService().showHttpError(this, err.response);
          this.isSaveTituloAcademico = false;
          this.closeAllPopups();
        });
    } else {
      this.tituloAcademicoProfesor.profesor = {
        id: this.profesor.id,
      };
      this.tituloAcademicoProfesorService()
        .create(this.tituloAcademicoProfesor)
        .then(res => {
          this.titulosAcademicosProfesorLista.push(res);
          this.isSaveTituloAcademico = false;
          this.closeAllPopups();
        })
        .catch(err => {
          this.alertService().showHttpError(this, err.response);
          this.isSaveTituloAcademico = false;
          this.closeAllPopups();
        });
    }
  }

  public eliminarTituloAcademico(titulo: ITituloAcademicoProfesor): void {
    this.tituloAcademicoProfesorService()
      .delete(titulo.id)
      .then(() => {
        this.consultarTitulosAcademicosProfesor(this.profesor.id);
      })
      .catch(err => {
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

  public convertDateFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_FORMAT_MONTH);
    }
    return null;
  }

  public updateInstantFieldTituloAcademico(field, event) {
    console.log(event);
    if (event.target.value && event.target.value.length > 0 && event.target.validationMessage.length < 1) {
      this.tituloAcademicoProfesor.yearTitulo = dayjs(event.target.value, DATE_FORMAT).toDate();
    } else {
      this.tituloAcademicoProfesor.yearTitulo = null;
    }
    this.checkCamposFormularioTitulosAcademicos('');
  }

  public filterListaCursosMaterias(): ICursoMateria[] {
    if (this.profesor.id && this.profesor.cursoMaterias) {
      if (this.isAgregarCurso) {
        return this.listaCursosMaterias.filter(curso => {
          return this.profesor.cursoMaterias.findIndex(cursoTemp => cursoTemp.id === curso.id) < 0;
        });
      }
    }
    return this.listaCursosMaterias;
  }

  public eliminarCursoMateria(cursoMaterio: ICursoMateria): void {
    if (this.profesor.id) {
      const indexCursoMateria = this.profesor.cursoMaterias.indexOf(cursoMaterio);
      this.profesor.cursoMaterias.splice(indexCursoMateria, 1);
      this.profesorService()
        .update(this.profesor)
        .then(res => {
          this.profesor = res;
          this.consultarCursosMateriaProfesor(this.profesor.id);
        })
        .catch(() => {
          this.profesor.cursoMaterias.push(cursoMaterio);
        });
    } else {
      if (this.profesor.cursoMaterias) {
        const indexCurso = this.profesor.cursoMaterias.indexOf(cursoMaterio);
        this.profesor.cursoMaterias.splice(indexCurso, 1);
      }
    }
  }

  public agregarCursoMateria(cursoMateria: ICursoMateria): void {
    if (this.profesor.id) {
      this.profesor.cursoMaterias.push(cursoMateria);
      this.profesorService()
        .update(this.profesor)
        .then(res => {
          this.profesor = res;
          this.cursoMateriaSeleccionado = {};
          this.consultarCursosMateriaProfesor(this.profesor.id);
          this.closeAllPopups();
        })
        .catch(err => {
          const indexCurso = this.profesor.cursoMaterias.indexOf(cursoMateria);
          this.profesor.cursoMaterias.splice(indexCurso, 1);
          this.cursoMateriaSeleccionado = {};
          this.closeAllPopups();
        });
    } else {
      if (this.profesor.cursoMaterias) {
        this.profesor.cursoMaterias.push(cursoMateria);
        this.cursoMateriaSeleccionado = {};
        this.closeAllPopups();
      } else {
        this.profesor.cursoMaterias = [cursoMateria];
        this.cursoMateriaSeleccionado = {};
        this.closeAllPopups();
      }
    }
  }

  public eliminarEscalafon(escalafonProfesor: IEscalafonProfesor): void {
    if (escalafonProfesor.id) {
      this.isAgregarEscalafon = true;
      this.escalafonProfesorService()
        .delete(escalafonProfesor.id)
        .then(res => {
          let indexEscalafon = -1;
          this.escalafonoesProfesor.map((value, index) => {
            console.log('dentro de map');
            console.log(value);
            console.log(escalafonProfesor);
            if (value.id === escalafonProfesor.id) {
              indexEscalafon = index;
            }
          });
          if (indexEscalafon > -1) {
            this.escalafonoesProfesor.splice(indexEscalafon, 1);
          }
          this.escalafonProfesor = {};
        })
        .catch(err => {
          this.isAgregarEscalafon = false;
          this.escalafonProfesor = {};
          // this.closeAllPopups();
        });
    }
  }

  public agregarEscalafon(escalafonProfesor: IEscalafonProfesor): void {
    escalafonProfesor.profesor = {
      id: this.profesor.id,
    };
    this.isAgregarEscalafon = true;
    if (escalafonProfesor.id) {
      this.escalafonProfesorService()
        .update(escalafonProfesor)
        .then(res => {
          let indexEscalafon = -1;
          this.escalafonoesProfesor.map((value, index) => {
            if (value.id === res.id) {
              indexEscalafon = index;
            }
          });
          if (indexEscalafon > -1) {
            this.escalafonoesProfesor.splice(indexEscalafon, 1);
            this.escalafonoesProfesor.push(res);
          }
          this.isAgregarEscalafon = false;
          this.closeAllPopups();
        })
        .catch(err => {
          this.isAgregarEscalafon = false;
          this.closeAllPopups();
        });
    } else {
      this.escalafonProfesorService()
        .create(escalafonProfesor)
        .then(res => {
          this.escalafonoesProfesor.push(res);
          this.isAgregarEscalafon = false;
          this.closeAllPopups();
        })
        .catch(err => {
          this.isAgregarEscalafon = false;
          this.closeAllPopups();
        });
    }
  }

  public openPopupEditarNuevoTituloAcademico(tittulo: ITituloAcademicoProfesor): void {
    this.tituloAcademicoProfesor = tittulo;
    if (<any>this.$refs.modalPopupCrearEstudioProfesor) {
      (<any>this.$refs.modalPopupCrearEstudioProfesor).show();
    }
  }

  public openPopupAgregarEscalafonProfesor(): void {
    this.escalafonProfesor = {};
    if (<any>this.$refs.modalPopupAgregarEscalafon) {
      (<any>this.$refs.modalPopupAgregarEscalafon).show();
      this.escalafonProfesor.fecha = new Date();
    }
  }

  public openPopupAgregarNuevoTituloAcademico(): void {
    this.tituloAcademicoProfesor = {};
    if (<any>this.$refs.modalPopupCrearEstudioProfesor) {
      (<any>this.$refs.modalPopupCrearEstudioProfesor).show();
      this.tituloAcademicoProfesor.yearTitulo = new Date();
    }
  }

  public closePopupAgregarNuevoTituloAcademico(): void {
    (<any>this.$refs.modalPopupCrearEstudioProfesor).hide();
    this.tituloAcademicoProfesor = {};
    this.$v.$reset();
  }

  public openPopupEditarCursoMateria(curso: ICursoMateria): void {
    this.cursoMateriaSeleccionado = {};
    if (<any>this.$refs.modalPopupAgregarMateria) {
      (<any>this.$refs.modalPopupAgregarMateria).show();
      this.cursoMateriaSeleccionado = curso;
      this.isAgregarCurso = false;
    }
  }

  public openPopupAgregarCursoMateria(): void {
    this.cursoMateriaSeleccionado = {};
    if (<any>this.$refs.modalPopupAgregarMateria) {
      (<any>this.$refs.modalPopupAgregarMateria).show();
      this.isAgregarCurso = true;
    }
  }

  public closePopupAgregarCursoMateria(): void {
    (<any>this.$refs.modalPopupAgregarMateria).hide();
    this.cursoMateriaSeleccionado = {};
    this.isAgregarCurso = false;
  }

  public closePopupAgregarEscalafon(): void {
    (<any>this.$refs.modalPopupAgregarEscalafon).hide();
    this.escalafonProfesor = {};
    this.isAgregarEscalafon = false;
  }

  public closeAllPopups(): void {
    this.closePopupAgregarNuevoTituloAcademico();
    this.closePopupAgregarCursoMateria();
    this.closePopupAgregarEscalafon();
  }

  public checkCamposFormularioTitulosAcademicos(event): void {
    this.$v.$touch();
  }

  public changeCursoMateria(event): void {
    this.cursoMateriaSeleccionado = {};
    if (event.target.value) {
      const listCursoMateriaFilter = this.listaCursosMaterias.filter(curso => curso.id === Number(event.target.value));
      if (listCursoMateriaFilter.length > 0) {
        this.cursoMateriaSeleccionado = listCursoMateriaFilter[0];
      }
    }
  }

  public checkFieldStringValid(field: string): boolean {
    return field !== null && field !== undefined && field.trim().length > 0;
  }

  public checkFieldFechaValid(field: any): boolean {
    return field !== null && field !== undefined;
  }

  public updateInstantFechaEscalafon(event) {
    console.log('event');
    console.log(event);
    if (event.target.value && event.target.value.length > 0) {
      const dateTemp = event.target.value.concat('-01');
      this.escalafonProfesor.fecha = dayjs(dateTemp, DATE_FORMAT).toDate();
    } else {
      this.escalafonProfesor.fecha = null;
    }
    console.log(this.escalafonProfesor);
  }
}
