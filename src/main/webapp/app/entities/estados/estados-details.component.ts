import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEstados } from '@/shared/model/estados.model';
import EstadosService from './estados.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class EstadosDetails extends Vue {
  @Inject('estadosService') private estadosService: () => EstadosService;
  @Inject('alertService') private alertService: () => AlertService;

  public estados: IEstados = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.estadosId) {
        vm.retrieveEstados(to.params.estadosId);
      }
    });
  }

  public retrieveEstados(estadosId) {
    this.estadosService()
      .find(estadosId)
      .then(res => {
        this.estados = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
