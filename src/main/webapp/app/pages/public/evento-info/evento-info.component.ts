import { IEvento } from '@/shared/model/evento.model';
import EventoService from '@/entities/evento/evento.service';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './evento-info.scss';
import dayjs from 'dayjs';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class EventoInfo extends Vue {
  @Inject('eventoService') private eventoService: () => EventoService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('alertService') private alertService: () => AlertService;

  public evento: IEvento = {};

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.eventoId) {
        vm.consultarEvento(to.params.eventoId);
      }
    });
  }

  private consultarEvento(eventoId: number): void {
    this.eventoService()
      .findCustom(this.$store.getters.authenticated, eventoId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        this.evento = res;
        this.downloadImageProfesorPerfil(this.evento);
      })
      .catch(err => {
        this.$router.go(-1);
        this.alertService().showHttpError(this, err.response);
      });
  }

  private downloadImageProfesorPerfil(evento: IEvento): void {
    if (evento.id && evento.urlFoto) {
      this.googleStorageService()
        .downloadFileByOnlyFileName(this.$store.getters.authenticated, evento.urlFoto)
        .then(res => {
          this.utilsService().agregarFileToList(evento.urlFoto, res);
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
