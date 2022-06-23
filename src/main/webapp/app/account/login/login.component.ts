import { Authority } from '@/shared/security/authority';
import AccountService from '@/account/account.service';
import axios from 'axios';
import { Component, Vue, Inject } from 'vue-property-decorator';

import './login.scss';

@Component
export default class Login extends Vue {
  @Inject('accountService')
  private accountService: () => AccountService;
  public authenticationError = null;
  public userLoginAccount: any = {
    username: undefined,
    password: undefined,
    rememberMe: true,
  };

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
              const authoritiesString = this.accountService().userAuthorities as Array<string>;
              this.redirectSegunAuthorities(authoritiesString);
            }
          });
      })
      .catch(() => {
        this.authenticationError = true;
      });
  }

  private redirectSegunAuthorities(authorities: string[]): void {
    console.log("dentro de metodo redirect");
    console.log(authorities);
    if (authorities.includes(Authority.ADMIN)) {
      this.$router.push('/faultad-ingenieria/programas-lista');
    } else if (authorities.includes(Authority.PROFESOR)) {
      this.$router.push({
        name: 'inicio_usuario',
      });
    } else {
      console.log("dentro de else");
    }
  }
}
