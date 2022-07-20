import EventoService from '@/entities/evento/evento.service';
import AlertService from '@/shared/alert/alert.service';
import { IEvento } from '@/shared/model/evento.model';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './eventos-lista.scss';

@Component
export default class EventosLista extends Vue {
  @Inject('eventoService') private eventoService: () => EventoService;
  @Inject('alertService') private alertService: () => AlertService;

  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'fecha';
  public reverse = true;
  public totalItems = 0;

  public eventos: IEvento[] = [];

  public mounted(): void {
    this.consultarEventos();
  }

  public consultarEventos(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.eventoService()
      .retrieve(paginacionQuery)
      .then(res => {
        this.eventos = res.data;
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(err => {
        this.eventos = [];
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
    this.consultarEventos();
  }

  public changeOrder(propOrder: string): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public dateSplit(date: string): string {
    if (date) {
      const splitDate = date.split(',');
      if (splitDate.length) {
        return splitDate[0];
      }
    }
    return '';
  }

}
