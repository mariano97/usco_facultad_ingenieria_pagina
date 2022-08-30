import { Component, Inject, Vue } from 'vue-property-decorator';
import LoginService from '@/account/login.service';
import AccountService from '@/account/account.service';
import TranslationService from '@/locale/translation.service';

import EntitiesMenu from '@/entities/entities-menu.vue';
import LaboratorioService from '@/entities/laboratorio/laboratorio.service';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
import { ILaboratorio } from '@/shared/model/laboratorio.model';

@Component({
  components: {
    'entities-menu': EntitiesMenu,
  },
})
export default class JhiNavbar extends Vue {
  @Inject('loginService')
  private loginService: () => LoginService;
  @Inject('translationService') private translationService: () => TranslationService;

  @Inject('laboratorioService') private laboratorioService: () => LaboratorioService;

  @Inject('accountService') private accountService: () => AccountService;
  public version = 'v' + VERSION;
  private currentLanguage = this.$store.getters.currentLanguage;
  private languages: any = this.$store.getters.languages;
  private hasAnyAuthorityValues = {};

  public hasLabGranja = false;
  public hasLabMuseo = false;
  public labGranja: ILaboratorio = {};
  public labMuseo: ILaboratorio = {};

  created() {
    this.translationService().refreshTranslation(this.currentLanguage);
  }

  public mounted() {
    this.chechHasLab(identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_GRANJA);
    this.chechHasLab(identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_MUSEO);
  }

  private chechHasLab(tipoLaboratorio: number): void {
    this.laboratorioService()
      .hasTipoLaboratorio(this.$store.getters.authenticated, tipoLaboratorio)
      .then(res => {
        if (tipoLaboratorio === identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_GRANJA) {
          this.hasLabGranja = res;
          this.getLaboratorioByTipoLabId(identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_GRANJA);
        }
        if (tipoLaboratorio === identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_MUSEO) {
          this.hasLabMuseo = res;
          this.getLaboratorioByTipoLabId(identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_MUSEO);
        }
      });
  }

  private getLaboratorioByTipoLabId(tipoLabId: number): void {
    this.laboratorioService()
      .getAllByTipoLaboratorio(this.$store.getters.authenticated, tipoLabId)
      .then(res => {
        if (res.length > 0) {
          if (tipoLabId === identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_GRANJA) {
            this.labGranja = res[0];
          }
          if (tipoLabId === identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_MUSEO) {
            this.labMuseo = res[0];
          }
        }
      });
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

  public changeLanguage(newLanguage: string): void {
    this.translationService().refreshTranslation(newLanguage);
  }

  public isActiveLanguage(key: string): boolean {
    return key === this.$store.getters.currentLanguage;
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

  public openLogin(): void {
    this.loginService().openLogin((<any>this).$root);
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        this.hasAnyAuthorityValues[authorities] = value;
      });
    return this.hasAnyAuthorityValues[authorities] ?? false;
  }

  public get openAPIEnabled(): boolean {
    return this.$store.getters.activeProfiles.indexOf('api-docs') > -1;
  }

  public get inProduction(): boolean {
    return this.$store.getters.activeProfiles.indexOf('prod') > -1;
  }
}
