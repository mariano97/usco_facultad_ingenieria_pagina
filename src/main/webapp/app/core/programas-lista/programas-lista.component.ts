import TablaElementoCatalogoService from '../../entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import { ITablaElementoCatalogo } from './../../shared/model/tabla-elemento-catalogo.model';
import { Component, Inject, Vue } from 'vue-property-decorator';

import './programas-lista.scss';


@Component
export default class ProgramasLista extends Vue {

  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;

  private listTiposPrograma: ITablaElementoCatalogo[] = [];

  public created(): void {
    this.consultarTiposPrograma();
  }

  public consultarTiposPrograma(): void {
    this.listTiposPrograma = [];
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, 'TIPSGRMA')
      .then(res => {
        this.listTiposPrograma = res;
      });
  }
}
