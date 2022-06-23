import AlertService from '@/shared/alert/alert.service';
import { IProfesor, Profesor } from '@/shared/model/profesor.model';
import { IUser, User } from '@/shared/model/user.model';
import { IUsuarioProfesorFull, UsuarioProfesorFull } from '@/shared/model/usuario-profesor.model';
import { Authority } from '@/shared/security/authority';
import UsuarioProfesorFullService from '@/shared/services/usuario-profesor.service';
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
  },
};

@Component({
  validations: validations,
})
export default class ProfesorFormulario extends Vue {
  @Inject('usuarioProfesorFullService') private usuarioProfesorFullService: () => UsuarioProfesorFullService;
  @Inject('alertService') private alertService: () => AlertService;

  public usuarioProfesor: IUsuarioProfesorFull = new UsuarioProfesorFull();
  public userAccount: IUser = new User();
  public profesor: IProfesor = new Profesor();

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.userLogin) {
        vm.consultarUsuarioProfesor(to.params.userLogin);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
    });
  }

  public consultarUsuarioProfesor(userLogin: string): void {
    this.usuarioProfesorFullService()
      .getOneUsuarioProfesorByUserLogin(this.$store.getters.authenticated, userLogin)
      .then(res => {
        this.userAccount = res.adminUserDTO;
        this.profesor = res.profesorDTO;
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
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
}
