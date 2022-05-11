import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITablaTiposCatalogo } from '@/shared/model/tabla-tipos-catalogo.model';
import TablaTiposCatalogoService from './tabla-tipos-catalogo.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TablaTiposCatalogoDetails extends Vue {
  @Inject('tablaTiposCatalogoService') private tablaTiposCatalogoService: () => TablaTiposCatalogoService;
  @Inject('alertService') private alertService: () => AlertService;

  public tablaTiposCatalogo: ITablaTiposCatalogo = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tablaTiposCatalogoId) {
        vm.retrieveTablaTiposCatalogo(to.params.tablaTiposCatalogoId);
      }
    });
  }

  public retrieveTablaTiposCatalogo(tablaTiposCatalogoId) {
    this.tablaTiposCatalogoService()
      .find(tablaTiposCatalogoId)
      .then(res => {
        this.tablaTiposCatalogo = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
