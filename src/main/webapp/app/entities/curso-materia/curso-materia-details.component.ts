import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICursoMateria } from '@/shared/model/curso-materia.model';
import CursoMateriaService from './curso-materia.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class CursoMateriaDetails extends Vue {
  @Inject('cursoMateriaService') private cursoMateriaService: () => CursoMateriaService;
  @Inject('alertService') private alertService: () => AlertService;

  public cursoMateria: ICursoMateria = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.cursoMateriaId) {
        vm.retrieveCursoMateria(to.params.cursoMateriaId);
      }
    });
  }

  public retrieveCursoMateria(cursoMateriaId) {
    this.cursoMateriaService()
      .find(cursoMateriaId)
      .then(res => {
        this.cursoMateria = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
