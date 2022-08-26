import NoticiaService from '@/entities/noticia/noticia.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_FORMAT, DATE_TIME_LONG_FORMAT_UTC } from '@/shared/date/filters';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { INoticia } from '@/shared/model/noticia.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';
import { SwiperOptions } from 'swiper-vue2/types/swiper/swiper-options';
import { Component, Inject, Vue } from 'vue-property-decorator';
import './noticia-info.scss';
import 'swiper/swiper-bundle.css';

SwiperCore.use([Navigation, Pagination]);

@Component({
  components: {
    Swiper,
    SwiperSlide,
  },
})
export default class NoticiaInfo extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('noticiaService') private noticiaService: () => NoticiaService;
  @Inject('alertService') private alertService: () => AlertService;

  public noticia: INoticia = {};
  public otrasNoticias: INoticia[] = [];
  public showAnotherNoticias = false;

  public swiperOptions: SwiperOptions = {
    loop: false,
    navigation: true,
    spaceBetween: 30,
    slidesPerView: 3.5,
    breakpoints: {
      100: {
        slidesPerView: 1,
        spaceBetween: 10,
      },
      1000: {
        slidesPerView: 3.5,
        spaceBetween: 30,
      },
    },
  };

  public beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.noticiaId) {
        vm.consultarNoticia(to.params.noticiaId);
      }
      vm.showAnotherNoticias = false;
      vm.otrasNoticias = [];
      vm.noticia = {};
    });
  }

  public beforeRouteUpdate(to, from) {
    // just use `this`
    console.log('dentro de beforeRouteUpdate');
    this.showAnotherNoticias = false;
    this.otrasNoticias = [];
    this.noticia = {};
    this.consultarNoticia(to.params.noticiaId);
  }

  private consultarNoticia(noticiaId: number): void {
    this.noticiaService()
      .findCustom(this.$store.getters.authenticated, noticiaId)
      .then(res => {
        res.fecha = new Date(res.fecha);
        this.noticia = res;
        this.downloadImageProfesorPerfil(this.noticia);
        this.consultarAnotherNoticias();
      })
      .catch(err => {
        this.$router.go(-1);
        this.alertService().showHttpError(this, err.response);
      });
  }

  private consultarAnotherNoticias(): void {
    const paginacionQuery = {
      page: 0,
      size: 5,
      sort: ['fecha,desc'],
    };
    /* const date = new Date();
    if (date.getMonth() === 0) {
      date.setFullYear(date.getFullYear() - 1, 11, 1);
    } else {
      date.setMonth(date.getMonth() - 1);
    }
    const fechaBase = dayjs(date).format(DATE_TIME_LONG_FORMAT_UTC).concat(':00Z');*/
    this.noticiaService()
      .retrieveCustom(this.$store.getters.authenticated, paginacionQuery)
      .then(res => {
        this.otrasNoticias = res.data;
        const noticiasTemp = this.otrasNoticias.filter(noti => noti.id !== this.noticia.id);
        this.otrasNoticias = [];
        this.otrasNoticias = noticiasTemp;
        this.otrasNoticias.map(event => {
          this.downloadImageProfesorPerfil(event);
        });
        setTimeout(() => {
          this.showAnotherNoticiasMethod();
        }, 2000);

      })
      .catch(err => {
        this.otrasNoticias = [];
      });
  }

  private showAnotherNoticiasMethod(): void {
    console.log('dentro de metodo showAnotherNoticiasMethod');
    this.showAnotherNoticias = true;
    console.log(this.showAnotherNoticias);
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
