import { IUser } from './../../../shared/model/user.model';
import { IProfesor } from './../../../shared/model/profesor.model';
import ProfesorService from '@/entities/profesor/profesor.service';
import AlertService from '@/shared/alert/alert.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import UserManagementService from '@/admin/user-management/user-management.service';
import './profesores-lista.scss';

@Component
export default class ProfesoresLista extends Vue {
  @Inject('profesorService') private profesorService: () => ProfesorService;
  @Inject('userManagementService') private userManagementService: () => UserManagementService;
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
    console.log("profesores");
    this.consultarProfesores();
  }

  public consultarProfesores(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.userManagementService()
      .retrieve(paginacionQuery)
      .then(res => {
        this.usuarios = res.data;
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
}
