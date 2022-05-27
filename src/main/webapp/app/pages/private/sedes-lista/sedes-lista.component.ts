import SedeService from '@/entities/sede/sede.service';
import accionesConstants from '@/shared/constants/acciones.constants';
import { ISede } from '@/shared/model/sede.model';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './sedes-lista.scss';

@Component
export default class SedesLista extends Vue {
  @Inject('sedeService') private sedeService: () => SedeService;

  private removeId: number = null;
  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;

  public sedes: ISede[] = [];

  public constantEditarPrograma = accionesConstants.ACCION_EDITAR_PROGRAMAS_LISTA;
  public constantCrearPrograma = accionesConstants.ACCION_CREAR_PROGRAMAS_LISTA;

  public mounted(): void {
    this.consultarAllSedes();
  }

  public consultarAllSedes(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.sedeService()
      .retrieve(paginacionQuery)
      .then(res => {
        this.sedes = res.data;
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(() => {
        this.sedes = [];
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
    this.consultarAllSedes();
  }

  public changeOrder(propOrder): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }
}
