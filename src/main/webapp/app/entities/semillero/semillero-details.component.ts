import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISemillero } from '@/shared/model/semillero.model';
import SemilleroService from './semillero.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class SemilleroDetails extends Vue {
  @Inject('semilleroService') private semilleroService: () => SemilleroService;
  @Inject('alertService') private alertService: () => AlertService;

  public semillero: ISemillero = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.semilleroId) {
        vm.retrieveSemillero(to.params.semilleroId);
      }
    });
  }

  public retrieveSemillero(semilleroId) {
    this.semilleroService()
      .find(semilleroId)
      .then(res => {
        this.semillero = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
