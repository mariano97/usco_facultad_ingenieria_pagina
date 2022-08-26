import LaboratorioService from '@/entities/laboratorio/laboratorio.service';
import AlertService from '@/shared/alert/alert.service';
import { ILaboratorio } from '@/shared/model/laboratorio.model';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './laboratorios-lista.scss';

@Component
export default class LaboratoriosLista extends Vue {
  @Inject('laboratorioService') private laboratorioService: () => LaboratorioService;
  @Inject('alertService') private alertService: () => AlertService;

  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = true;
  public totalItems = 0;

  public laboratorios: ILaboratorio[] = [];

  public mounted(): void {
    this.consultarLaboratorios();
  }

  public consultarLaboratorios(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.laboratorioService()
      .retrieve(paginacionQuery)
      .then(res => {
        this.laboratorios = res.data;
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(err => {
        this.laboratorios = [];
      });
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
    this.consultarLaboratorios();
  }

  public changeOrder(propOrder: string): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

}
