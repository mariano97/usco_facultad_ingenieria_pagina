import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITituloAcademicoProfesor } from '@/shared/model/titulo-academico-profesor.model';
import TituloAcademicoProfesorService from './titulo-academico-profesor.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TituloAcademicoProfesorDetails extends Vue {
  @Inject('tituloAcademicoProfesorService') private tituloAcademicoProfesorService: () => TituloAcademicoProfesorService;
  @Inject('alertService') private alertService: () => AlertService;

  public tituloAcademicoProfesor: ITituloAcademicoProfesor = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tituloAcademicoProfesorId) {
        vm.retrieveTituloAcademicoProfesor(to.params.tituloAcademicoProfesorId);
      }
    });
  }

  public retrieveTituloAcademicoProfesor(tituloAcademicoProfesorId) {
    this.tituloAcademicoProfesorService()
      .find(tituloAcademicoProfesorId)
      .then(res => {
        this.tituloAcademicoProfesor = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
