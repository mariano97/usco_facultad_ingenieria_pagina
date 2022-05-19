import { IPrograma } from '@/shared/model/programa.model';
import ProgramaService from '@/entities/programa/programa.service';
import { Vue, Component, Inject } from 'vue-property-decorator';
import accionesConstants from '@/shared/constants/acciones.constants';
import './programas-lista.scss';
@Component
export default class ProgramasLista extends Vue {
  @Inject('programaService') private programaService: () => ProgramaService;

  public itemsPerPage = 10;
  public page = 1;
  public previusPage = 1;
  public propiedadOrder = 'id';
  public reverse = false;
  public totalItems = 0;
  public queryCount: number = null;

  public programasList: IPrograma[] = [];

  public constantEditarPrograma = accionesConstants.ACCION_EDITAR_PROGRAMAS_LISTA;
  public constantCrearPrograma = accionesConstants.ACCION_EDITAR_PROGRAMAS_LISTA;

  public mounted(): void {
    this.consultarAlProgramas();
  }

  public consultarAlProgramas(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.programaService()
      .retrieve(paginacionQuery)
      .then(
        res => {
          this.programasList = res.data;
          this.totalItems = Number(res.headers['x-total-count']);
          this.queryCount = this.totalItems;
        },
        err => {
          console.error("error obtener programas");
          console.error(err);
        }
      );
  }

  public sort(): Array<any> {
    const result = [this.propiedadOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propiedadOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previusPage) {
      this.previusPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.consultarAlProgramas();
  }

}
