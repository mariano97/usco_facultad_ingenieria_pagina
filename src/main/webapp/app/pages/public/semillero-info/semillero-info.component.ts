import SemilleroService from '@/entities/semillero/semillero.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { ISemillero } from '@/shared/model/semillero.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './semillero-info.scss';

@Component
export default class SemilleroInfo extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('semilleroService') private semilleroService: () => SemilleroService;
  @Inject('alertService') private alertService: () => AlertService;

  public semillero: ISemillero = {};

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.semilleroId) {
        vm.consultarSemillero(to.params.semilleroId);
      }
    });
  }

  private consultarSemillero(semilleroId: number): void {
    this.semilleroService()
      .findCustom(this.$store.getters.authenticated, semilleroId)
      .then(res => {
        this.semillero = res;
        this.downloadImagePerfil(this.semillero);
      })
      .catch(err => {
        this.$router.go(-1);
        this.alertService().showHttpError(this, err.response);
      });
  }

  private downloadImagePerfil(semillero: ISemillero): void {
    if (semillero.id && semillero.urlFoto) {
      this.googleStorageService()
        .downloadFileByOnlyFileName(this.$store.getters.authenticated, semillero.urlFoto)
        .then(res => {
          this.utilsService().agregarFileToList(semillero.urlFoto, res);
        });
    }
  }

  public obtenerImagen(fileName: string): any {
    if (this.utilsService().existeFileInList(fileName)) {
      const file: IFileDownloaded = this.utilsService().obtenerFileByFileName(fileName);
      return file.file;
    }
    return '/content/images/static/user-base.png';
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_FORMAT);
    }
    return '';
  }
}
