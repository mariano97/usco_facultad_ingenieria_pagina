import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEvento } from '@/shared/model/evento.model';
import EventoService from './evento.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class EventoDetails extends Vue {
  @Inject('eventoService') private eventoService: () => EventoService;
  @Inject('alertService') private alertService: () => AlertService;

  public evento: IEvento = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.eventoId) {
        vm.retrieveEvento(to.params.eventoId);
      }
    });
  }

  public retrieveEvento(eventoId) {
    this.eventoService()
      .find(eventoId)
      .then(res => {
        this.evento = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
