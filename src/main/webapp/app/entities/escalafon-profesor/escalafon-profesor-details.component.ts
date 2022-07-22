import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEscalafonProfesor } from '@/shared/model/escalafon-profesor.model';
import EscalafonProfesorService from './escalafon-profesor.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class EscalafonProfesorDetails extends Vue {
  @Inject('escalafonProfesorService') private escalafonProfesorService: () => EscalafonProfesorService;
  @Inject('alertService') private alertService: () => AlertService;

  public escalafonProfesor: IEscalafonProfesor = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.escalafonProfesorId) {
        vm.retrieveEscalafonProfesor(to.params.escalafonProfesorId);
      }
    });
  }

  public retrieveEscalafonProfesor(escalafonProfesorId) {
    this.escalafonProfesorService()
      .find(escalafonProfesorId)
      .then(res => {
        this.escalafonProfesor = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
