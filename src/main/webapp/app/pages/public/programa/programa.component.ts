import { IPresentacionBasico } from '@/shared/model/presentacion-basico.model';
import ProgramaService from '@/entities/programa/programa.service';
import { Navigation, Pagination, Autoplay } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';
import { DATE_FORMAT_FULL_MONTH } from '@/shared/date/filters';
import { IPrograma } from '@/shared/model/programa.model';
import dayjs from 'dayjs';
import { Vue, Component, Inject } from 'vue-property-decorator';
import 'swiper/swiper-bundle.css';
import './programa.scss';

SwiperCore.use([Navigation, Pagination, Autoplay]);

@Component({
  components: {
    Swiper,
    SwiperSlide,
  },
})
export default class Programa extends Vue {

  private codigoSniesTemp = this.$route.params.codigo_snies;

  @Inject('programaService') private programaService: () => ProgramaService;

  public programa: IPrograma = {};
  public pesentacionBasico: IPresentacionBasico[] = [];
  public isPresentacionBasicaLoaded = false;

  public created(): void {
    this.consultarPrograma();
  }

  private consultarPrograma(): void {
    this.programa = {};
    this.programaService()
      .findByCodigoSnies(this.$store.getters.authenticated, Number(this.codigoSniesTemp))
      .then(res => {
        res.fechaRegistroCalificado = new Date(res.fechaRegistroCalificado);
        this.programa = res;
        this.llenarListaPresentacion(this.programa);
      })
      .catch(err => {
        console.error(err);
      });
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_FORMAT_FULL_MONTH);
    }
    return null;
  }

  private llenarListaPresentacion(programa: IPrograma): void {
    this.isPresentacionBasicaLoaded = false;
    this.pesentacionBasico = [];
    if (programa.presentacionPrograma && programa.presentacionPrograma.trim().length > 0) {
      this.pesentacionBasico.push({
        title: this.$t('programa.otros.que-ofrecemos').toString(),
        descripcion: programa.presentacionPrograma,
      });
    }
    if (programa.mision && programa.mision.trim().length > 0) {
      this.pesentacionBasico.push({
        title: this.$t('programa.otros.mision').toString(),
        descripcion: programa.mision,
      });
    }
    if (programa.vision && programa.vision.trim().length > 0) {
      this.pesentacionBasico.push({
        title: this.$t('programa.otros.vision').toString(),
        descripcion: programa.vision,
      });
    }
    if (programa.dirigidoAQuien && programa.dirigidoAQuien.trim().length > 0) {
      this.pesentacionBasico.push({
        title: this.$t('programa.otros.dirigido-a-quien').toString(),
        descripcion: programa.dirigidoAQuien,
      });
    }
    console.log(this.pesentacionBasico);
    this.isPresentacionBasicaLoaded = true;
  }

  public getImageUrl (imageId) {
    return `https://picsum.photos/600/400/?image=${imageId}`;
  }
}
