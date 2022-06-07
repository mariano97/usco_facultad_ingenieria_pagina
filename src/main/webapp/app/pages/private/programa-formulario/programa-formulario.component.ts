import { IPrograma, Programa } from '@/shared/model/programa.model';
import { required, numeric, decimal, maxLength } from 'vuelidate/lib/validators';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './programa-formulario.scss';
import ProgramaService from '@/entities/programa/programa.service';
import AlertService from '@/shared/alert/alert.service';
import DatePicker from 'vue2-datepicker';
import 'vue2-datepicker/index.css';
import dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';
import carpetasarchivosConstants from '@/shared/constants/carpetasarchivos.constants';
import GoogleStorageService from '@/shared/services/google-storage.service';
import ArchivosProgramaService from '@/entities/archivos-programa/archivos-programa.service';
import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';

const validations: any = {
  programa: {
    nombre: {
      required,
    },
    codigoSnies: {
      required,
      numeric,
    },
    codigoRegistroCalificado: {
      required,
      numeric,
    },
    fechaRegistroCalificado: {
      required,
    },
    nombreTituloOtorgado: {
      required,
    },
    numeroCreditos: {
      required,
      numeric,
    },
    duracionPrograma: {},
    presentacionPrograma: {
      required,
      maxLength: maxLength(255),
    },
    mision: {
      required,
      maxLength: maxLength(255),
    },
    vision: {
      required,
      maxLength: maxLength(255),
    },
    perfilEstudiante: {
      maxLength: maxLength(255),
    },
    perfilEgresado: {
      required,
      maxLength: maxLength(255),
    },
    perfilOcupacional: {
      maxLength: maxLength(255),
    },
    urlFotoPrograma: {
      maxLength: maxLength(255),
    },
    dirigidoAQuien: {
      maxLength: maxLength(255),
    },
    costoPrograma: {
      required,
      decimal,
    },
    nivelFormacion: {
      required,
    },
    tipoFormacion: {
      required,
    },
  },
};

@Component({
  components: {
    DatePicker,
  },
  validations,
})
export default class ProgramaFormulario extends Vue {
  @Inject('programaService') private programaService: () => ProgramaService;
  @Inject('tablaElementoCatalogoService') private tablaElementoCatalogoService: () => TablaElementoCatalogoService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('googleStorageService') private googleStorageService: () => GoogleStorageService;
  @Inject('archivosProgramaService') private archivosProgramaService: () => ArchivosProgramaService;

  public programa: IPrograma = new Programa();

  private listTiposPrograma: ITablaElementoCatalogo[] = [];
  private listTiposFormacion: ITablaElementoCatalogo[] = [];
  private archivosProgramaList: IArchivosPrograma[] = [];
  private archvivoProgramaImageProfile: IArchivosPrograma = {};
  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;
  public dateMax = dayjs().format(DATE_FORMAT);
  private carpetaImagen = '';
  private file: any = null;
  public imageProfilePrograma: any;
  public showImage = false;

  /* public created(): void {
    this.consultarListaProgramas();
    this.consultarTipoFormacion();
  } */

  public beforeRouteEnter (to, from, next) {
    next(vm => {
      if (to.params.codigoSnies) {
        vm.consultarPrograma(to.params.codigoSnies);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
      vm.consultarListaProgramas();
      vm.consultarTipoFormacion();
      // vm.downloadFile();
    });
  }

  private consultarListaProgramas(): void {
    this.listTiposPrograma = [];
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_TIPO_PROGRAMA)
      .then(res => {
        this.listTiposPrograma = res;
      });
  }

  private consultarTipoFormacion(): void {
    this.listTiposFormacion = [];
    this.tablaElementoCatalogoService()
      .getAllByTipoCatalogoKeyIdentificador(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_TIPO_FORMACION)
      .then(res => {
        this.listTiposFormacion = res;
      });
  }

  public changeImage(event): void {
    console.log(event.target.files);
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      const allowedImageTypes = ['image/png', 'image/jpeg', 'image/jpg'];
      if (!allowedImageTypes.includes(file.type)) {
        console.log("tipo no permitido");
        return;
      }
      this.file = file;
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = () => {
        this.imageProfilePrograma = fileReader.result;
        this.showImage = true;
      };
      if (this.programa.id) {
        this.showImage = false;
        if (this.archvivoProgramaImageProfile.id) {
          this.updateFileToStorage(
            this.generateUrlFolderUpload(this.programa.codigoSnies + '', this.programa.nombre),
            this.archvivoProgramaImageProfile.id,
            this.file
          );
        } else {
          this.uploadFileToStorage(
            this.programa.id,
            5,
            this.file,
            this.generateUrlFolderUpload(this.programa.codigoSnies + '', this.programa.nombre)
          );
        }
      }
    }
  }

  public countCharacter(maxTamano: number, value: string): number {
    return maxTamano - (value ? value.trim().length : 0);
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.programa.id) {
      this.programaService()
        .update(this.programa)
        .then(res => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.updated', { param: res.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.programa.facultad = {
        id: 1,
      };
      this.programaService()
        .create(this.programa)
        .then(res => {
          this.isSaving = false;
          if (this.file !== null) {
            this.uploadFileToStorage(
              res.id,
              5,
              this.file,
              this.generateUrlFolderUpload(this.programa.codigoSnies + '', this.programa.nombre)
            );
          }
          this.$router.push({ name: 'usuario_programas_lista' });
          // this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.programa.created', { param: res.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  private uploadFileToStorage(programaId: number, elementoCatalogoId: number, file: File, nameCarpeta: string): void {
    this.googleStorageService()
      .uploadProgramaFile(programaId, elementoCatalogoId, nameCarpeta, file)
      .then(res1 => {
        console.log(res1);
      })
      .catch(err => {
        console.error(err);
      });
  }

  public updateFileToStorage(carpetaName: string, archivoProgramaId: number, file: File): void {
    this.googleStorageService()
      .updateFileArchivoPrograma(carpetaName, archivoProgramaId, file)
      .then(res => {
        this.archivosProgramaList.map(archivo => {
          if (archivo.id === archivoProgramaId) {
            archivo.generationStorage = res.generationStorage;
            archivo.urlName = res.urlName;
          }
        });
        this.consultarArchivosPrograma(this.programa.id);
      })
      .catch(err => {
        console.error("error actualizando archivo");
        console.error(err);
      });
  }

  private generateUrlFolderUpload(codigoSnies: string, namePrograma: string): string {
    namePrograma = namePrograma.replace(' ', '-');
    return carpetasarchivosConstants.CARPETA_BASE_PROGRAMA_IMAGES.replace('{{snies}}', codigoSnies + '').replace(
      '{{name_programa}}',
      namePrograma
    );
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.programa[field] = dayjs(event.target.value, DATE_FORMAT);
    } else {
      this.programa[field] = null;
    }
  }

  public consultarPrograma(programaId: number): void {
    this.programaService()
      .findByCodigoSnies(this.$store.getters.authenticated, Number(programaId))
      .then(res => {
        res.fechaRegistroCalificado = new Date(res.fechaRegistroCalificado);
        this.programa = res;
        this.consultarArchivosPrograma(this.programa.id);
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public consultarArchivosPrograma(programaId: number): void {
    this.archivosProgramaService()
      .getAllByProgramaId(programaId)
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
        .downloadFile(archivoProgramaImage[0].urlName, archivoProgramaImage[0].generationStorage)
        .then(res => {
          this.imageProfilePrograma = res;
          this.showImage = true;
        })
        .catch(err => {
          console.log(err);
        });
    }
  }

  public checkHabilitacionCampos(): boolean {
    return !this.enableEdit;
  }

  public enableFormularioEditar(): void {
    this.enableEdit = !this.enableEdit;
  }

  public cancelarAccion(): void {
    if (this.isModeEdit) {
      this.enableEdit = !this.enableEdit;
    } else {
      this.$router.go(-1);
    }
  }
}
