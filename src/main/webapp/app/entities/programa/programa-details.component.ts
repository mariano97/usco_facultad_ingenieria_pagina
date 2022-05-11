import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPrograma } from '@/shared/model/programa.model';
import ProgramaService from './programa.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ProgramaDetails extends Vue {
  @Inject('programaService') private programaService: () => ProgramaService;
  @Inject('alertService') private alertService: () => AlertService;

  public programa: IPrograma = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.programaId) {
        vm.retrievePrograma(to.params.programaId);
      }
    });
  }

  public retrievePrograma(programaId) {
    this.programaService()
      .find(programaId)
      .then(res => {
        this.programa = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
