import { IRedesPrograma } from './../../../shared/model/redes-programa.model';
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
import UtilsService from '@/shared/services/utils.service';
import { IFileDocumentoNuevo } from '@/shared/model/file-documento-nuevo.model';
import AlertService from '@/shared/alert/alert.service';
import RedesProgramaService from '@/entities/redes-programa/redes-programa.service';
import SedeService from '@/entities/sede/sede.service';
import { LMap, LTileLayer, LMarker, LPopup } from "vue2-leaflet";
import 'leaflet/dist/leaflet.css';
import { IOpenStreetMap } from '@/shared/model/open-street-map.model';

SwiperCore.use([Navigation, Pagination, Autoplay]);

@Component({
  components: {
    Swiper,
    SwiperSlide,
    LMap,
    LTileLayer,
    LMarker,
    LPopup,
  },
})
export default class Programa extends Vue {

  private codigoSniesTemp = this.$route.params.codigo_snies;

  @Inject('programaService') private programaService: () => ProgramaService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('archivosProgramaService') private archivosProgramaService: () => ArchivosProgramaService;
  @Inject('utilsService') private utilsService: () => UtilsService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('redesProgramaService') private redesProgramaService: () => RedesProgramaService;
  @Inject('sedeService') private sedeService: () => SedeService;

  public copyrightMap = '&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors';
  public urlMap = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
  public marksSedes: IOpenStreetMap[] = [{ lat: '6.00304', lon: '-74.48776', name: 'Colombia' }];

  public programa: IPrograma = {};
  public pesentacionBasico: IPresentacionBasico[] = [];
  public isPresentacionBasicaLoaded = false;
  public archivosProgramaList: IArchivosPrograma[] = [];
  public listArrayArchivosProgramas: IArchivosPrograma[][] = [];
  private archvivoProgramaImageProfile: IArchivosPrograma = {};
  public archivoProgramaPlanEstudio: IArchivosPrograma = {};
  public listRedesSocialesPrograma: IRedesPrograma[] = [];
  public imageProfilePrograma: any;
  public showImage = false;
  private archivosProgramaDescargados: IFileDocumentoNuevo[] = [];
  public showSpinnerPlanEstudio = false;

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
        this.consultarSedesPrograma(this.programa.id);
        this.consultarArchivosPrograma(this.programa.id);
        this.llenarListaPresentacion(this.programa);
        this.consultarRedesSocialesPrograma(this.programa.id);
      })
      .catch(err => {
        console.error(err);
      });
  }

  private consultarSedesPrograma(programaId: number): void {
    this.sedeService()
      .findAllByProgramaId(this.$store.getters.authenticated, programaId)
      .then(res => {
        this.programa.sedes = res;
        this.agruparCoordenadasSedes();
        if (this.mapaExsite()) {
          if (this.programa.sedes.length < 1) {
            (<any>this.$refs.map).setZoom(15);
          } else {
            (<any>this.$refs.map).setZoom(8);
          }
          (<any>this.$refs.map).setCenter({ lat: 2.4738993, lng: -75.5900113 }, { lat: 0, lng: 0 });
        }
      })
      .catch(err => {
        this.programa.sedes = [];
      });
  }

  public consultarArchivosPrograma(programaId: number): void {
    this.archivosProgramaService()
      .getAllByProgramaId(this.$store.getters.authenticated, programaId)
      .then(res => {
        this.archivosProgramaList = res;
        this.downloadImageProgramaPerfil();
        this.filterProgramaPlanEstudio();
        this.agruparArchivosPorgramas(this.archivosProgramaList);
      })
      .catch(err => {
        console.error("Errore obteniendo archivos");
        console.error(err);
      });
  }

  public consultarRedesSocialesPrograma(programaId: number): void {
    this.redesProgramaService()
      .findAllByProgramaId(this.$store.getters.authenticated, programaId)
      .then(res => {
        this.listRedesSocialesPrograma = res;
      })
      .catch(err => {
        console.error("Errore obteniendo redes");
        console.error(err);
      });
  }

  private agruparArchivosPorgramas(archivosPrograma: IArchivosPrograma[]): void {
    this.listArrayArchivosProgramas = [];
    const archivosOnly = this.filterOnlyArchivosPrograma(archivosPrograma);
    for (let i = 0; i < archivosOnly.length; i += 2) {
      if (!(archivosOnly.length % 2) || archivosOnly.length - i != 1) {
        this.listArrayArchivosProgramas.push([archivosOnly[i], archivosOnly[i + 1]]);
      } else {
        this.listArrayArchivosProgramas.push([archivosOnly[i], {}]);
      }
    }
  }

  private filterOnlyArchivosPrograma(archivosProgra: IArchivosPrograma[]): IArchivosPrograma[] {
    return archivosProgra.filter(
      archivo => archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_DOCUMENTO_NUMBER
    );
  }

  private filterProgramaPlanEstudio(): void {
    const archivosPlanEstudio = this.archivosProgramaList.filter(
      archivo => archivo.tablaElementoCatalogo.id === identificadoresConstants.IDENTIFICADOR_TIPO_DOCUEMNTO_PLAN_ESTUDIO_NUMBER
    );
    if (archivosPlanEstudio.length > 0) {
      this.archivoProgramaPlanEstudio = archivosPlanEstudio[0];
    }
  }

  public verArchivoPrograma(archivoProgra: IArchivosPrograma): void {
    if (archivoProgra.urlName) {
      this.showSpinnerPlanEstudio = true;
      const archivoDescargadoFiltrado = this.archivosProgramaDescargados.filter(
        archivo => archivo.archivoDocumentoPrograma.id === archivoProgra.id
      );
      if (archivoDescargadoFiltrado.length > 0) {
        this.utilsService().convertirFileDownloadedToBlobNewTab(archivoDescargadoFiltrado[0].fileBase64.toString());
        this.showSpinnerPlanEstudio = false;
      } else {
        this.googleStorageService()
          .downloadFile(this.$store.getters.authenticated, archivoProgra.urlName, archivoProgra.generationStorage)
          .then(res => {
            this.archivosProgramaDescargados.push({
              tipoDocumento: archivoProgra.tipoDocumento,
              fileBase64: res,
              archivoDocumentoPrograma: archivoProgra,
            });
            this.utilsService().convertirFileDownloadedToBlobNewTab(res);
            this.showSpinnerPlanEstudio = false;
          })
          .catch(err => {
            this.alertService().showHttpError(this, err.response);
            this.showSpinnerPlanEstudio = false;
          });
      }
    }
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
    this.isPresentacionBasicaLoaded = true;
  }

  public mapaExsite(): boolean {
    if (<any>this.$refs.map) {
      return true;
    }
    return false;
  }

  public generateTextLugarOferta(): string {
    let strLugar = '';
    if (this.programa.sedes && this.programa.sedes.length > 0) {
      this.programa.sedes.map((sede, index) => {
        strLugar = strLugar.concat(sede.ciudad.nombre);
        if (index + 1 < this.programa.sedes.length) {
          strLugar = strLugar.concat(', ');
        }
      });
    }
    return strLugar;
  }

  private agruparCoordenadasSedes(): void {
    if (this.programa.id) {
      this.programa.sedes.map(sede => {
        this.marksSedes.push({ lat: sede.latitud + '', lon: sede.longitud + '', name: sede.nombre });
      });
    }
  }

  public clickIntoMarks(event): void {
    console.log(event);
    if (this.mapaExsite()) {
      (<any>this.$refs.map).setZoom(15);
      (<any>this.$refs.map).setCenter({ lat: event.latlng.lat, lng: event.latlng.lng }, { lat: 0, lng: 0 });
    }
  }
}
