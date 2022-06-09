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
import ArchivosProgramaService from '@/entities/archivos-programa/archivos-programa.service';
import GoogleStorageService from '@/shared/services/google-storage.service';
import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';
import identificadoresConstants from '@/shared/constants/identificadores.constants';

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
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('archivosProgramaService') private archivosProgramaService: () => ArchivosProgramaService;

  public programa: IPrograma = {};
  public pesentacionBasico: IPresentacionBasico[] = [];
  public isPresentacionBasicaLoaded = false;
  public archivosProgramaList: IArchivosPrograma[] = [];
  private archvivoProgramaImageProfile: IArchivosPrograma = {};
  public imageProfilePrograma: any;
  public showImage = false;

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
        this.consultarArchivosPrograma(this.programa.id);
        this.llenarListaPresentacion(this.programa);
      })
      .catch(err => {
        console.error(err);
      });
  }

  public consultarArchivosPrograma(programaId: number): void {
    this.archivosProgramaService()
      .getAllByProgramaId(this.$store.getters.authenticated, programaId)
      .then(res => {
        this.archivosProgramaList = res;
        this.downloadImageProgramaPerfil();
      })
      .catch(err => {
        console.error("Errore obteniendo archivos");
        console.error(err);
      });
  }

  private downloadImageProgramaPerfil(): void {
    const archivoProgramaImage = this.archivosProgramaList.filter(
      archivo => archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_IMAGE_NUMBER
    );
    if (archivoProgramaImage.length > 0) {
      this.archvivoProgramaImageProfile = archivoProgramaImage[0];
      this.googleStorageService()
        .downloadFile(this.$store.getters.authenticated, archivoProgramaImage[0].urlName, archivoProgramaImage[0].generationStorage)
        .then(res => {
          this.imageProfilePrograma = res;
          this.showImage = true;
        })
        .catch(err => {
          console.log(err);
        });
    }
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
