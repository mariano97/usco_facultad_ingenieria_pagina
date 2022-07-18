import AccountService from '@/account/account.service';
import { IUser } from '@/shared/model/user.model';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './usuario-sidbar.scss';

@Component
export default class UsuarioSidbar extends Vue {
  @Inject('accountService') private accountService: () => AccountService;

  public user: IUser = {};

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
}
