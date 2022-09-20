import LaboratorioService from '@/entities/laboratorio/laboratorio.service';
import AlertService from '@/shared/alert/alert.service';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
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
  public urlSitio = '';

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.laboratorioId) {
        vm.consultarSemillero(to.params.laboratorioId);
      }
    });
  }

  public beforeRouteUpdate(to, from) {
    // just use `this`
    console.log('dentro de beforeRouteUpdate');
    console.log(to);
    console.log(from);
    // this.$router.replace(to.path);
    this.laboratorio = {};
    this.consultarSemillero(to.params.laboratorioId);
  }

  private consultarSemillero(laboratorioId: number): void {
    this.laboratorioService()
      .findCustom(this.$store.getters.authenticated, laboratorioId)
      .then(res => {
        this.laboratorio = res;
        this.asignarUrl(this.laboratorio);
        this.downloadImagePerfil(this.laboratorio);
      })
      .catch(err => {
        this.$router.go(-1);
        this.alertService().showHttpError(this, err.response);
      });
  }

  private asignarUrl(laboratorio: ILaboratorio): void {
    if (laboratorio.tipoLaboratorio.id === identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_GRANJA) {
      this.urlSitio = 'https://www.usco.edu.co/es/granja-usco/';
    }
    if (laboratorio.tipoLaboratorio.id === identificadoresConstants.IDENTIFICADOR_ID_TIPO_LABORATORIO_MUSEO) {
      this.urlSitio = '';
    }
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
