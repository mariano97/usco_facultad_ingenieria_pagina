import NoticiaService from '@/entities/noticia/noticia.service';
import { DATE_FORMAT } from '@/shared/date/filters';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { INoticia } from '@/shared/model/noticia.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './noticias-lista.scss';

@Component
export default class NoticiasLista extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('noticiaService') private noticiaService: () => NoticiaService;

  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'fecha';
  public reverse = true;
  public totalItems = 0;

  public ultimaNoticia: INoticia = {};
  public noticiasListado: INoticia[] = [];
  public noticiasArrayListado: INoticia[][] = [];

  public mounted() {
    this.consultarLastNoticia();
    this.consultarNoticias();
  }

  private consultarLastNoticia(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.noticiaService()
      .retrieveCustom(this.$store.getters.authenticated, paginacionQuery)
      .then(res => {
        const noticias: INoticia[] = res.data;
        if (noticias.length > 0) {
          this.ultimaNoticia = noticias[0];
          this.downloadImageProfesorPerfil(this.ultimaNoticia);
        }
    })
  }

  private consultarNoticias(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.noticiaService()
      .retrieveCustom(this.$store.getters.authenticated, paginacionQuery)
      .then(res => {
        this.noticiasListado = res.data;
        this.agruparNoticiasInArray(this.noticiasListado);
        // this.ultimaNoticia = this.noticiasListado[0];
        // this.noticiasListado.splice(0, 1);
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
        // this.downloadImageProfesorPerfil(this.ultimaNoticia);
        this.noticiasListado.map(noti => {
          this.downloadImageProfesorPerfil(noti);
        });
      });
  }

  private agruparNoticiasInArray(noticias: INoticia[]): void {
    this.noticiasArrayListado = [];
    for (let i = 0; i < noticias.length; i += 2) {
      if (!(noticias.length % 2) || noticias.length - i != 1) {
        this.noticiasArrayListado.push([noticias[i], noticias[i + 1]]);
      } else {
        this.noticiasArrayListado.push([noticias[i], {}]);
      }
    }
  }

  public filtrarNoticias(noticias: INoticia[]): INoticia[][] {
    const noticiasTemp = noticias.filter(noti => noti.id !== this.ultimaNoticia.id);
    this.agruparNoticiasInArray(noticiasTemp);
    return this.noticiasArrayListado;
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
    this.consultarNoticias();
  }

  private downloadImageProfesorPerfil(noticia: INoticia): void {
    if (noticia.id && noticia.urlFoto) {
      this.googleStorageService()
        .downloadFileByOnlyFileName(this.$store.getters.authenticated, noticia.urlFoto)
        .then(res => {
          this.utilsService().agregarFileToList(noticia.urlFoto, res);
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
