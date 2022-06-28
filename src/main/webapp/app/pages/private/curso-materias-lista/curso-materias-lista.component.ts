import './curso-materias-lista.scss';
import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';
import AlertService from '@/shared/alert/alert.service';
import { ICursoMateria } from '@/shared/model/curso-materia.model';
import { Component, Inject, Vue } from 'vue-property-decorator';

@Component
export default class CursoMateriaLista extends Vue {
  @Inject('cursoMateriaService') private cursoMateriaService: () => CursoMateriaService;
  @Inject('alertService') private alertService: () => AlertService;

  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;

  public cursoMateriasLista: ICursoMateria[] = [];

  public mounted() {
    this.consultarCursosMateria();
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
    this.consultarCursosMateria();
  }

  public changeOrder(propOrder): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }
}
