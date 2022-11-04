import { Authority } from '@/shared/security/authority';
import AccountService from '@/account/account.service';
import axios from 'axios';
import { Component, Vue, Inject } from 'vue-property-decorator';

import './login.scss';
import { IUser } from '@/shared/model/user.model';

@Component
export default class Login extends Vue {
vText(vText: any) {
throw new Error('Method not implemented.');
}
$t(arg0: string): unknown {
throw new Error('Method not implemented.');
}
  @Inject('accountService')
  private accountService: () => AccountService;
  public authenticationError = null;
  public userLoginAccount: any = {
    username: undefined,
    password: undefined,
    rememberMe: true,
  };

  public titleIngresar = 'Ingresar';

  public mounted(): void {
    if (this.$route.query.passChanged && this.$route.query.passChanged === '1') {
      this.titleIngresar = 'Vuelve a ingresar';
    }
  }

  public doLogin(): void {
    axios
      .post('api/authenticate', this.userLoginAccount)
      .then(result => {
        const bearerToken = result.headers.authorization;
        if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
          const jwt = bearerToken.slice(7, bearerToken.length);
          if (this.userLoginAccount.rememberMe) {
            localStorage.setItem('jhi-authenticationToken', jwt);
            sessionStorage.removeItem('jhi-authenticationToken');
          } else {
            sessionStorage.setItem('jhi-authenticationToken', jwt);
            localStorage.removeItem('jhi-authenticationToken');
          }
        }
        this.authenticationError = false;
        // this.$root.$emit('bv::hide::modal', 'login-page');
        this.accountService()
          .retrieveAccount()
          .then(res => {
            if (res) {
              const account: IUser = this.accountService().userAccount;
              if (!account.passwordAsignada) {
                const authoritiesString = this.accountService().userAuthorities as Array<string>;
                this.redirectSegunAuthorities(authoritiesString);
              } else {
                this.$router.push({
                  name: 'ChangePassword',
                  query: { passAsigned: '1' },
                });
              }
            }
          });
      })
      .catch(() => {
        this.authenticationError = true;
      });
  }

  private redirectSegunAuthorities(authorities: string[]): void {
    /* if (authorities.includes(Authority.ADMIN)) {
      this.$router.push('/faultad-ingenieria/programas-lista');
    } else if (authorities.includes(Authority.PROFESOR)) {
      this.$router.push({
        name: 'inicio_usuario',
      });
    } else {
      console.log("dentro de else");
    } */
    this.$router.push({
        name: 'inicio_usuario',
      });
  }
}
