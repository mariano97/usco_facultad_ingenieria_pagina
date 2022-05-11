import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFacultad } from '@/shared/model/facultad.model';
import FacultadService from './facultad.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FacultadDetails extends Vue {
  @Inject('facultadService') private facultadService: () => FacultadService;
  @Inject('alertService') private alertService: () => AlertService;

  public facultad: IFacultad = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.facultadId) {
        vm.retrieveFacultad(to.params.facultadId);
      }
    });
  }

  public retrieveFacultad(facultadId) {
    this.facultadService()
      .find(facultadId)
      .then(res => {
        this.facultad = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
