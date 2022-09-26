import './curso-materias-lista.scss';
import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';
import AlertService from '@/shared/alert/alert.service';
import { ICursoMateria } from '@/shared/model/curso-materia.model';
import { Component, Inject, Vue } from 'vue-property-decorator';
import { IPrograma } from '@/shared/model/programa.model';
import ProgramaService from '@/entities/programa/programa.service';

@Component
export default class CursoMateriaLista extends Vue {
  @Inject('cursoMateriaService') private cursoMateriaService: () => CursoMateriaService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('programaService') private programaService: () => ProgramaService;

  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'nombre';
  public reverse = false;
  public totalItems = 0;

  public cursoMateriasLista: ICursoMateria[] = [];
  public programasFacultad: IPrograma[] = [];

  public programaSeleccionadaId = 0;

  public mounted() {
    this.obtenerAllPrograma();
    this.consultarCursosMateria();
  }

  private obtenerAllPrograma(): void {
    const paginationQuery = {
      page: 0,
      size: 10000,
      sort: ['nombre,asc'],
    };
    this.programaService()
      .retrieve(paginationQuery)
      .then(res => {
        this.programasFacultad = res.data;
      });
  }

  public consultarCursosMateriasByProgramaId(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.cursoMateriaService()
      .retriveWithPrograma(this.$store.getters.authenticated, this.programaSeleccionadaId, paginacionQuery)
      .then(res => {
        this.cursoMateriasLista = res.data;
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(() => {
        this.cursoMateriasLista = [];
      });
  }

  public consultarCursosMateria(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.cursoMateriaService()
      .findAllMateriasPagination(this.$store.getters.authenticated, paginacionQuery)
      .then(res => {
        this.cursoMateriasLista = res.data;
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
      })
      .catch(() => {
        this.cursoMateriasLista = [];
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
    if (this.programaSeleccionadaId > 0) {
      this.consultarCursosMateriasByProgramaId();
    } else {
      this.consultarCursosMateria();
    }
  }

  public changeOrder(propOrder): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public limpiarCampos(): void {
    this.page = 1;
    this.programaSeleccionadaId = 0;
    this.transition();
  }

  public changeProgramaSeleccionado(): void {
    this.page = 1;
    // this.consultarCursosMateriasByProgramaId();
    this.transition();
  }
}
