import PaisesService from '@/entities/paises/paises.service';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import TituloAcademicoProfesorService from '@/entities/titulo-academico-profesor/titulo-academico-profesor.service';
import AlertService from '@/shared/alert/alert.service';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IPaises } from '@/shared/model/paises.model';
import { IProfesor, Profesor } from '@/shared/model/profesor.model';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { ITituloAcademicoProfesor, TituloAcademicoProfesor } from '@/shared/model/titulo-academico-profesor.model';
import { IUser, User } from '@/shared/model/user.model';
import { IUsuarioProfesorFull, UsuarioProfesorFull } from '@/shared/model/usuario-profesor.model';
import { Authority } from '@/shared/security/authority';
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
  @Inject('usuarioProfesorFullService') private usuarioProfesorFullService: () => UsuarioProfesorFullService;
  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('tituloAcademicoProfesorService') private tituloAcademicoProfesorService: () => TituloAcademicoProfesorService;
  @Inject('paisesService') private paisesService: () => PaisesService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('utilsService') private utilsService: () => UtilsService;

  public usuarioProfesor: IUsuarioProfesorFull = new UsuarioProfesorFull();
  public userAccount: IUser = new User();
  public profesor: IProfesor = new Profesor();
  public tituloAcademicoProfesor: ITituloAcademicoProfesor = new TituloAcademicoProfesor();

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;
  public isSaveTituloAcademico = false;

  public tiposProfesoresElemento: ITablaElementoCatalogo[] = [];
  public listaPaises: IPaises[] = [];
  public listaTiposTitulosAcademicos: ITablaElementoCatalogo[] = [];

  public titulosAcademicosProfesorLista: ITituloAcademicoProfesor[] = [];

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
    });
  }

  public consultarUsuarioProfesor(userLogin: string): void {
    this.usuarioProfesorFullService()
      .getOneUsuarioProfesorByUserLogin(this.$store.getters.authenticated, userLogin)
      .then(res => {
        this.userAccount = res.adminUserDTO;
        this.profesor = res.profesorDTO;
        this.consultarTitulosAcademicosProfesor(this.profesor.id);
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
      this.userAccount.authorities = [Authority.JEFE_PROGRAMA, Authority.PROFESOR, Authority.USER];
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

  public updateInstantFieldTituloAcademico(field, event) {
    console.log(event);
    if (event.target.value && event.target.value.length > 0 && event.target.validationMessage.length < 1) {
      this.tituloAcademicoProfesor.yearTitulo = dayjs(event.target.value, DATE_FORMAT).toDate();
    } else {
      this.tituloAcademicoProfesor.yearTitulo = null;
    }
    this.checkCamposFormularioTitulosAcademicos('');
  }

  public openPopupEditarNuevoTituloAcademico(tittulo: ITituloAcademicoProfesor): void {
    this.tituloAcademicoProfesor = tittulo;
    if (<any>this.$refs.modalPopupCrearEstudioProfesor) {
      (<any>this.$refs.modalPopupCrearEstudioProfesor).show();
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

  public closeAllPopups(): void {
    this.closePopupAgregarNuevoTituloAcademico();
  }

  public checkCamposFormularioTitulosAcademicos(event): void {
    this.$v.$touch();
  }
}
