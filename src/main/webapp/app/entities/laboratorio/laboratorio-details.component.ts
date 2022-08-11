import { Component, Vue, Inject } from 'vue-property-decorator';

import { ILaboratorio } from '@/shared/model/laboratorio.model';
import LaboratorioService from './laboratorio.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class LaboratorioDetails extends Vue {
  @Inject('laboratorioService') private laboratorioService: () => LaboratorioService;
  @Inject('alertService') private alertService: () => AlertService;

  public laboratorio: ILaboratorio = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.laboratorioId) {
        vm.retrieveLaboratorio(to.params.laboratorioId);
      }
    });
  }

  public retrieveLaboratorio(laboratorioId) {
    this.laboratorioService()
      .find(laboratorioId)
      .then(res => {
        this.laboratorio = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
