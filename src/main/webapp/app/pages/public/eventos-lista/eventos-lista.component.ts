import EventoService from '@/entities/evento/evento.service';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IEvento } from '@/shared/model/evento.model';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './eventos-lista.scss';

@Component
export default class EventosLista extends Vue {
  @Inject('eventoService') private eventoService: () => EventoService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;

  public eventos: IEvento[] = [];
  public eventosArrayListado: IEvento[][] = [];
  public eventosListado: IEvento[] = [];

  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'fecha';
  public reverse = true;
  public totalItems = 0;

  public mounted() {
    this.consultarEventos();
  }

  private consultarEventos(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.eventoService()
      .retrieveCustom(this.$store.getters.authenticated, paginacionQuery)
      .then(res => {
        this.eventosListado = res.data;
        this.eventosListado.sort((a, b) => (a.fecha < b.fecha ? 1 : -1));
        this.eventosListado.map(noti => {
          this.downloadImageProfesorPerfil(noti);
        });
        this.agruparNoticiasInArray(this.eventosListado);
        // this.ultimaNoticia = this.noticiasListado[0];
        // this.noticiasListado.splice(0, 1);
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
        // this.downloadImageProfesorPerfil(this.ultimaNoticia);

      });
  }

  private agruparNoticiasInArray(eventos: IEvento[]): void {
    this.eventosArrayListado = [];
    for (let i = 0; i < eventos.length; i += 2) {
      if (!(eventos.length % 2) || eventos.length - i != 1) {
        this.eventosArrayListado.push([eventos[i], eventos[i + 1]]);
      } else {
        this.eventosArrayListado.push([eventos[i], {}]);
      }
    }
  }

  public sort(): Array<any> {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.consultarEventos();
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
