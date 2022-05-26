import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISede } from '@/shared/model/sede.model';
import SedeService from './sede.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class SedeDetails extends Vue {
  @Inject('sedeService') private sedeService: () => SedeService;
  @Inject('alertService') private alertService: () => AlertService;

  public sede: ISede = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.sedeId) {
        vm.retrieveSede(to.params.sedeId);
      }
    });
  }

  public retrieveSede(sedeId) {
    this.sedeService()
      .find(sedeId)
      .then(res => {
        this.sede = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
