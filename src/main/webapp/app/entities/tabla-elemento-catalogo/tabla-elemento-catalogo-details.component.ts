import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import TablaElementoCatalogoService from './tabla-elemento-catalogo.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TablaElementoCatalogoDetails extends Vue {
  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('alertService') private alertService: () => AlertService;

  public tablaElementoCatalogo: ITablaElementoCatalogo = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tablaElementoCatalogoId) {
        vm.retrieveTablaElementoCatalogo(to.params.tablaElementoCatalogoId);
      }
    });
  }

  public retrieveTablaElementoCatalogo(tablaElementoCatalogoId) {
    this.tablaElementoCatalogoService()
      .find(tablaElementoCatalogoId)
      .then(res => {
        this.tablaElementoCatalogo = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
