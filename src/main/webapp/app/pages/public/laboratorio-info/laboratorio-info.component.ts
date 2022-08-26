import LaboratorioService from '@/entities/laboratorio/laboratorio.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { ILaboratorio } from '@/shared/model/laboratorio.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './laboratorio-info.scss';

@Component
export default class LaboratorioInfo extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('laboratorioService') private laboratorioService: () => LaboratorioService;
  @Inject('alertService') private alertService: () => AlertService;

  public laboratorio: ILaboratorio = {};

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.laboratorioId) {
        vm.consultarSemillero(to.params.laboratorioId);
      }
    });
  }

  private consultarSemillero(laboratorioId: number): void {
    this.laboratorioService()
      .findCustom(this.$store.getters.authenticated, laboratorioId)
      .then(res => {
        this.laboratorio = res;
        this.downloadImagePerfil(this.laboratorio);
      })
      .catch(err => {
        this.$router.go(-1);
        this.alertService().showHttpError(this, err.response);
      });
  }

  private downloadImagePerfil(laboratorio: ILaboratorio): void {
    if (laboratorio.id && laboratorio.urlFoto) {
      this.googleStorageService()
        .downloadFileByOnlyFileName(this.$store.getters.authenticated, laboratorio.urlFoto)
        .then(res => {
          this.utilsService().agregarFileToList(laboratorio.urlFoto, res);
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
