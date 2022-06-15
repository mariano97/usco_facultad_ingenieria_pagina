import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICiudad } from '@/shared/model/ciudad.model';
import CiudadService from './ciudad.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class CiudadDetails extends Vue {
  @Inject('ciudadService') private ciudadService: () => CiudadService;
  @Inject('alertService') private alertService: () => AlertService;

  public ciudad: ICiudad = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ciudadId) {
        vm.retrieveCiudad(to.params.ciudadId);
      }
    });
  }

  public retrieveCiudad(ciudadId) {
    this.ciudadService()
      .find(ciudadId)
      .then(res => {
        this.ciudad = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
