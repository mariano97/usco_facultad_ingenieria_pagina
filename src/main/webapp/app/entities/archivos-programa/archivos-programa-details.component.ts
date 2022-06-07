import { Component, Vue, Inject } from 'vue-property-decorator';

import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';
import ArchivosProgramaService from './archivos-programa.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ArchivosProgramaDetails extends Vue {
  @Inject('archivosProgramaService') private archivosProgramaService: () => ArchivosProgramaService;
  @Inject('alertService') private alertService: () => AlertService;

  public archivosPrograma: IArchivosPrograma = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.archivosProgramaId) {
        vm.retrieveArchivosPrograma(to.params.archivosProgramaId);
      }
    });
  }

  public retrieveArchivosPrograma(archivosProgramaId) {
    this.archivosProgramaService()
      .find(archivosProgramaId)
      .then(res => {
        this.archivosPrograma = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
