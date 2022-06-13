import { Component, Vue, Inject } from 'vue-property-decorator';

import { IRedesPrograma } from '@/shared/model/redes-programa.model';
import RedesProgramaService from './redes-programa.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class RedesProgramaDetails extends Vue {
  @Inject('redesProgramaService') private redesProgramaService: () => RedesProgramaService;
  @Inject('alertService') private alertService: () => AlertService;

  public redesPrograma: IRedesPrograma = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.redesProgramaId) {
        vm.retrieveRedesPrograma(to.params.redesProgramaId);
      }
    });
  }

  public retrieveRedesPrograma(redesProgramaId) {
    this.redesProgramaService()
      .find(redesProgramaId)
      .then(res => {
        this.redesPrograma = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
