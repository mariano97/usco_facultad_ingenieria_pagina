import { Vue, Component } from 'vue-property-decorator';
import './usuario-sidbar.scss';

@Component
export default class UsuarioSidbar extends Vue {

  public logout(): Promise<any> {
    localStorage.removeItem('jhi-authenticationToken');
    sessionStorage.removeItem('jhi-authenticationToken');
    this.$store.commit('logout');
    if (this.$route.path !== '/') {
      return this.$router.push('/');
    }
    return Promise.resolve(this.$router.currentRoute);
  }

}
