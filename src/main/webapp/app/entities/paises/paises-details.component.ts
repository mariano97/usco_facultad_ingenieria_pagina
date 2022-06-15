import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPaises } from '@/shared/model/paises.model';
import PaisesService from './paises.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PaisesDetails extends Vue {
  @Inject('paisesService') private paisesService: () => PaisesService;
  @Inject('alertService') private alertService: () => AlertService;

  public paises: IPaises = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.paisesId) {
        vm.retrievePaises(to.params.paisesId);
      }
    });
  }

  public retrievePaises(paisesId) {
    this.paisesService()
      .find(paisesId)
      .then(res => {
        this.paises = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
