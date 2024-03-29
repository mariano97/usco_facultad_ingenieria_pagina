import NoticiaService from '@/entities/noticia/noticia.service';
import { DATE_FORMAT, DATE_TIME_FORMAT, DATE_TIME_LONG_FORMAT, DATE_TIME_LONG_FORMAT_UTC } from '@/shared/date/filters';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';
import { INoticia } from '@/shared/model/noticia.model';
import GoogleStorageService from '@/shared/services/google-storage.service';
import UtilsService from '@/shared/services/utils.service';
import dayjs from 'dayjs';
import { Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';
import { SwiperOptions } from 'swiper-vue2/types/swiper/swiper-options';
import { Component, Inject, Prop, Vue } from 'vue-property-decorator';

import './noticias-fragment.scss';

SwiperCore.use([Navigation, Pagination]);

@Component({
  components: {
    Swiper,
    SwiperSlide,
  },
})
export default class NoticiasFragment extends Vue {
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('noticiaService') private noticiaService: () => NoticiaService;

  @Prop(String)
  nombre: string;

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

  public noticias: INoticia[] = [];

  public mounted() {
    this.consultarEventos();
  }

  private consultarEventos(): void {
    const paginacionQuery = {
      page: 0,
      size: 20,
      sort: ['fecha,desc'],
    };
    const date = new Date();
    if (date.getMonth() === 0) {
      date.setFullYear(date.getFullYear() - 1, 11, 1);
    } else {
      date.setMonth(date.getMonth() - 1);
    }

    const fechaBase = dayjs(date).format(DATE_TIME_LONG_FORMAT_UTC).concat(':00Z');
    // const fechaBase = dayjs(new Date()).utcOffset().toLocaleString();
    this.noticiaService()
      .findAllFechaMayorQue(this.$store.getters.authenticated, fechaBase, paginacionQuery)
      .then(res => {
        this.noticias = res;
        this.noticias.map(event => {
          this.downloadImageProfesorPerfil(event);
        });
      })
      .catch(err => {
        this.noticias = [];
      });
  }

  private downloadImageProfesorPerfil(evento: INoticia): void {
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

  public getImageUrl (imageId) {
    return `https://picsum.photos/600/400/?image=${imageId}`;
  }

}
