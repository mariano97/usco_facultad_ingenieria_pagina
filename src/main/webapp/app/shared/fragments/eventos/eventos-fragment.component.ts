import { Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';
import { Component, Inject, Vue } from 'vue-property-decorator';
import 'swiper/swiper-bundle.css';
import './eventos-fragment.scss';
import { SwiperOptions } from 'swiper-vue2/types/swiper/swiper-options';
import EventoService from '@/entities/evento/evento.service';
import { IEvento } from '@/shared/model/evento.model';
import dayjs from 'dayjs';
import { DATE_TIME_FORMAT, DATE_FORMAT, DATE_TIME_LONG_FORMAT, DATE_TIME_LONG_FORMAT_UTC } from '@/shared/date/filters';
import UtilsService from '@/shared/services/utils.service';
import GoogleStorageService from '@/shared/services/google-storage.service';
import { IFileDownloaded } from '@/shared/model/file-documento-nuevo.model';

SwiperCore.use([Navigation, Pagination]);

@Component({
  components: {
    Swiper,
    SwiperSlide,
  },
})
export default class EventosFragment extends Vue {
  @Inject('eventoService') private eventoService: () => EventoService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('utilsService') private utilsService: () => UtilsService;

  public eventos: IEvento[] = [];
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

  public mounted() {
    this.consultarEventos();
  }

  private consultarEventos(): void {
    const paginacionQuery = {
      page: 0,
      size: 20,
      sort: ['fecha,asc'],
    };
    const fechaBase = dayjs(new Date()).format(DATE_TIME_LONG_FORMAT_UTC).concat(':00Z');
    // const fechaBase = dayjs(new Date()).utcOffset().toLocaleString();
    this.eventoService()
      .findAllFechaMayorQue(this.$store.getters.authenticated, fechaBase, paginacionQuery)
      .then(res => {
        this.eventos = res;
        this.eventos.sort((a, b) => (a.fecha < b.fecha ? 1 : -1));
        this.eventos.map(event => {
          this.downloadImageProfesorPerfil(event);
        })
      })
      .catch(err => {
        this.eventos = [];
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

  public getImageUrl (imageId) {
    return `https://picsum.photos/600/400/?image=${imageId}`;
  }
}
