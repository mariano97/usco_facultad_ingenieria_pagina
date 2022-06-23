import { IUser } from './../../../shared/model/user.model';
import { IProfesor } from './../../../shared/model/profesor.model';
import ProfesorService from '@/entities/profesor/profesor.service';
import AlertService from '@/shared/alert/alert.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './profesores-lista.scss';
import UsuarioProfesorFullService from '@/shared/services/usuario-profesor.service';
import { Authority, AuthorityString } from '@/shared/security/authority';

@Component
export default class ProfesoresLista extends Vue {
  @Inject('profesorService') private profesorService: () => ProfesorService;
  @Inject('usuarioProfesorFullService') private usuarioProfesorFullService: () => UsuarioProfesorFullService;
  @Inject('alertService') private alertService: () => AlertService;

  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;

  public profesores: IProfesor[] = [];
  public usuarios: IUser[] = [];

  public mounted(): void {
    this.consultarProfesores();
  }

  public consultarProfesores(nameCompleteFilter?: string): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.usuarioProfesorFullService()
      .getAllUsuariosProfesor(this.$store.getters.authenticated, paginacionQuery, [Authority.PROFESOR], nameCompleteFilter)
      .then(res => {
        this.usuarios = res.data;
        console.log(this.usuarios);
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(() => {
        this.usuarios = [];
      });
    /* this.profesorService()
      .retrieve(paginacionQuery)
      .then(res => {
        this.profesores = res.data;
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(() => {
        this.profesores = [];
      });*/
  }

  public sort(): Array<any> {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.consultarProfesores();
  }

  public changeOrder(propOrder): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public generateRolesString(autorities?: string[]): string {
    console.log(autorities);
    let strRole = '';
    if (autorities) {
      autorities
        .filter(auth => auth !== Authority.ADMIN && auth !== Authority.USER)
        .map((auth, index) => {
          if (auth === Authority.PROFESOR) {
            strRole += AuthorityString.PROFESOR;
          } else if (auth === Authority.DECANO) {
            strRole += AuthorityString.DECANO;
          } else if (auth === Authority.JEFE_PROGRAMA) {
            strRole += AuthorityString.JEFE_PROGRAMA;
          }
          if (index + 1 < autorities.filter(auth2 => auth2 !== Authority.ADMIN && auth2 !== Authority.USER).length && strRole.length > 0) {
            strRole += ', ';
          }
        });
    }
    return strRole;
  }
}
