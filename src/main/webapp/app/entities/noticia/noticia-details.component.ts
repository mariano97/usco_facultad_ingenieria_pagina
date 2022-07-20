import { Component, Vue, Inject } from 'vue-property-decorator';

import { INoticia } from '@/shared/model/noticia.model';
import NoticiaService from './noticia.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class NoticiaDetails extends Vue {
  @Inject('noticiaService') private noticiaService: () => NoticiaService;
  @Inject('alertService') private alertService: () => AlertService;

  public noticia: INoticia = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.noticiaId) {
        vm.retrieveNoticia(to.params.noticiaId);
      }
    });
  }

  public retrieveNoticia(noticiaId) {
    this.noticiaService()
      .find(noticiaId)
      .then(res => {
        this.noticia = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
