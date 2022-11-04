import AccountService from '@/account/account.service';
import { IUser } from '@/shared/model/user.model';
import { Authority } from '@/shared/security/authority';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './usuario-sidbar.scss';

@Component
export default class UsuarioSidbar extends Vue {
  @Inject('accountService') private accountService: () => AccountService;

  public user: IUser = {};
  private hasAnyAuthorityValues = {};

  public ROLE_AMIND = Authority.ADMIN;
  public ROLE_DECANO = Authority.DECANO;
  public ROLE_JEFE_PROGRAMA = Authority.JEFE_PROGRAMA;

  public mounted() {
    const account: IUser = this.accountService().userAccount;
    this.user = account;
  }

  public logout(): Promise<any> {
    localStorage.removeItem('jhi-authenticationToken');
    sessionStorage.removeItem('jhi-authenticationToken');
    this.$store.commit('logout');
    if (this.$route.path !== '/') {
      return this.$router.push('/');
    }
    return Promise.resolve(this.$router.currentRoute);
  }

  public subIsActive(input) {
    const paths = Array.isArray(input) ? input : [input];
    return paths.some(path => {
      return this.$route.path.indexOf(path) === 0; // current path starts with this path string
    });
  }

  public pathActive(input) {
    const paths = Array.isArray(input) ? input : [input];
    return paths.some(path => {
      return this.$route.path.includes(path); // current path starts with this path string
    });
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        if (this.hasAnyAuthorityValues[authorities] !== value) {
          this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
        }
      });
    return this.hasAnyAuthorityValues[authorities] ?? false;
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }
}
