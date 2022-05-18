import './programas-lista.scss';
import ProgramaService from '@/entities/programa/programa.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import { IPrograma } from '@/shared/model/programa.model';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';


@Component
export default class ProgramasLista extends Vue {

  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('programaService') private programaService: () => ProgramaService;

  private listTiposPrograma: ITablaElementoCatalogo[] = [];
  private listProgramas: IPrograma[] = [];
  private listProgramasTemp: IPrograma[] = [];
  public formNamePrograma = null;
  public formSelectTipoPrograma = 0;

  public created(): void {
    this.consultarTiposPrograma();
    this.consultarListaProgramas();
  }

  public consultarTiposPrograma(): void {
    this.listTiposPrograma = [];
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, 'TIPSGRMA')
      .then(res => {
        this.listTiposPrograma = res;
      });
  }

  private consultarListaProgramas(): void {
    this.listProgramas = [];
    this.listProgramasTemp = [];
    this.programaService()
      .findAll(this.$store.getters.authenticated)
      .then(res => {
        this.listProgramas = res;
        this.listProgramasTemp = res;
      });
  }

  public limpiarCampos(): void {
    this.formNamePrograma = null;
    this.formSelectTipoPrograma = 0;
    this.filtrarProgramas();
  }

  public filtrarProgramas(): void {
    this.listProgramas = [];
    let listaFiltarda: IPrograma[] = [];
    if (this.formNamePrograma != null && this.formNamePrograma.length > 0 && this.formSelectTipoPrograma > 0) {
      listaFiltarda = this.listProgramasTemp.filter(item => {
        return item.nombre.toLowerCase().includes(this.formNamePrograma.toLowerCase()) && item.nivelFormacion.id == this.formSelectTipoPrograma;
      });
    } else if (this.formNamePrograma != null && this.formNamePrograma.length > 0) {
      listaFiltarda = this.listProgramasTemp.filter(item => {
        return item.nombre.toLowerCase().includes(this.formNamePrograma.toLowerCase());
      });
    } else if (this.formSelectTipoPrograma > 0) {
      listaFiltarda = this.listProgramasTemp.filter(item => {
        return item.nivelFormacion.id == this.formSelectTipoPrograma;
      });
    } else {
      listaFiltarda = this.listProgramasTemp;
    }
    this.listProgramas = listaFiltarda;
  }

}
