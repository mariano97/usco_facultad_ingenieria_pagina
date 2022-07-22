import SemilleroService from '@/entities/semillero/semillero.service';
import AlertService from '@/shared/alert/alert.service';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { ISemillero } from '@/shared/model/semillero.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './semilleros-lista.scss';

@Component
export default class SemillerosLista extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('semilleroService') private semilleroService: () => SemilleroService;
  @Inject('alertService') private alertService: () => AlertService;

  public semillerosList: ISemillero[] = [];
  public semillerosArrayLista: ISemillero[][] = [];

  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = true;
  public totalItems = 0;

  public mounted() {
    this.consultarSemilleros();
  }

  private consultarSemilleros(): void {
    const paginacionQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.semilleroService()
      .retrieveCustom(this.$store.getters.authenticated, paginacionQuery)
      .then(res => {
        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
        this.semillerosList = res.data;
        this.semillerosList.map(noti => {
          this.downloadImageProfesorPerfil(noti);
        });
        this.agruparSemillerosInArray(this.semillerosList);
      });
  }

  private agruparSemillerosInArray(semillero: ISemillero[]): void {
    this.semillerosArrayLista = [];
    for (let i = 0; i < semillero.length; i += 2) {
      if (!(semillero.length % 2) || semillero.length - i != 1) {
        this.semillerosArrayLista.push([semillero[i], semillero[i + 1]]);
      } else {
        this.semillerosArrayLista.push([semillero[i], {}]);
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
    this.consultarSemilleros();
  }

  private downloadImageProfesorPerfil(noticia: ISemillero): void {
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
}
