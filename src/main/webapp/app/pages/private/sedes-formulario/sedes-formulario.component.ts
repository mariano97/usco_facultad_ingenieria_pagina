import { required, decimal, maxLength, email, helpers } from 'vuelidate/lib/validators';
import { ISede, Sede } from './../../../shared/model/sede.model';
import SedeService from '@/entities/sede/sede.service';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './sedes-formulario.scss';
import AlertService from '@/shared/alert/alert.service';
import * as validators from '@/shared/validators/validators.component';
import { LMap, LTileLayer, LMarker } from "vue2-leaflet";
import 'leaflet/dist/leaflet.css';
import CiudadService from '@/entities/ciudad/ciudad.service';
import { ICiudad } from '@/shared/model/ciudad.model';
import identificadoresConstants from '@/shared/constants/identificadores.constants';
import OpenStreetMapService from '@/shared/services/open-street-map.service';

const validations: any = {
  sede: {
    nombre: {
      required,
      maxLength: maxLength(250),
    },
    latitud: {
      required,
      decimal,
    },
    longitud: {
      required,
      decimal,
    },
    direccion: {
      required,
      maxLength: maxLength(250),
    },
    estado: {
      required,
    },
    telefonoFijo: {
      maxLength: maxLength(7),
    },
    telefonoCelular: {
      maxLength: maxLength(10),
    },
    correoElectronico: {
      required,
      validEmail: validators.validEmail,
    },
    codigoIndicativo: {
      required,
      maxLength: maxLength(20),
    },
    ciudad: {
      required,
    },
  },
};

@Component({
  validations,
  components: {
    LMap,
    LTileLayer,
    LMarker,
  },
})
export default class SedesFormulario extends Vue {
  @Inject('sedeService') private sedeService: () => SedeService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('ciudadService') private ciudadService: () => CiudadService;
  @Inject('openStreetMapService') private openStreetMapService: () => OpenStreetMapService;

  public copyrightMap = '&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors';
  public urlMap = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';

  public sede: ISede = new Sede();

  public ciudadesLista: ICiudad[] = [];
  public markerLatLng: number[] = [6.00304, -74.48776];
  public zoomMap = 6;
  public showMapMarker = true;

  public isSaving = false;
  public isModeEdit = false;
  public enableEdit = true;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.sedeId) {
        vm.consultarSede(to.params.sedeId);
        vm.isModeEdit = true;
        vm.enableEdit = false;
      }
      vm.consultarCiudades();
    });
  }

  private consultarSede(codigoIndicativo: string): void {
    this.sede = {};
    this.sedeService()
      .findByCodigoIndicativo(codigoIndicativo)
      .then(res => {
        this.sede = res;
        setTimeout(() => {
          this.posicionarMap(null);
        }, 5000);

      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  private consultarCiudades(): void {
    this.ciudadesLista = [];
    this.ciudadService()
      .findAllByEstadoId(this.$store.getters.authenticated, identificadoresConstants.IDENTIFICADOR_ID_ESTADO_HUILA_COLOMBIA)
      .then(res => {
        this.ciudadesLista = res;
      })
      .catch(err => {
        this.alertService().showHttpError(this, err.response);
      });
  }

  public posicionarMap(event): void {
    console.log('dento de posicionarMap');
    console.log(event);
    if (<any>this.$refs.map) {
      console.log(this.sede);
      (<any>this.$refs.map).setZoom(17);
      (<any>this.$refs.map).setCenter({ lat: this.sede.latitud, lng: this.sede.longitud }, { lat: 0, lng: 0 });
      this.markerLatLng = [this.sede.latitud, this.sede.longitud];
      this.showMapMarker = true;
    }
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.sede.id) {
      this.sedeService()
        .update(this.sede)
        .then(res => {
          this.isSaving = false;
          this.enableFormularioEditar();
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
          this.enableFormularioEditar();
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.sede.estado = true;
      this.sedeService()
        .create(this.sede)
        .then(res => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('paginaFacultadIngenieriaProyectoApp.sede.created', { param: res.id });
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

  public clickMap(event): void {
    if (event.latlng && this.enableEdit) {
      this.markerLatLng = [event.latlng.lat, event.latlng.lng];
      this.showMapMarker = true;
      this.sede.latitud = this.markerLatLng[0];
      this.sede.longitud = this.markerLatLng[1];
      this.openStreetMapService()
        .consultarReverseData('jsonv2', event.latlng.lat, event.latlng.lng)
        .then(res => {
          this.sede.direccion = res.display_name;
        })
        .catch(err => {
          console.error('error openstreetmap');
          console.error(err);
        });
    }
  }

  public changeSelectCiudad(event): void {
    this.showMapMarker = true;
    if (<any>this.$refs.map) {
      (<any>this.$refs.map).setZoom(15);
      (<any>this.$refs.map).setCenter({ lat: this.sede.ciudad.latitud, lng: this.sede.ciudad.longitud }, { lat: 0, lng: 0 });
    }
  }

  public changeCoordenadas(event): void {
    this.showMapMarker = false;
    console.log(event);
    /* this.coordenadas = {
      lat: 0,
      lon: 0,
    }; */
    if (Number(event.target.value) === 2) {
      /*this.coordenadas = {
        lat: 2.38942,
        lon: -75.8926236,
      };*/
      <any>this.$refs.map.setCenter({ lat: 2.38942, lng: -75.8926236 }, {lat: 0, lng:0});
    } else if (Number(event.target.value) === 1) {
      /* this.coordenadas = {
        lat: 3.3758991,
        lon: -74.8019303,
      }; */
      <any>this.$refs.map.setCenter({ lat: 3.3758991, lng: -74.8019303 }, {lat: 0, lng:0});
    } else {
      /* this.coordenadas = {
        lat: 0,
        lon: 0,
      }; */
      <any>this.$refs.map.setCenter({lat: 0, lng:0}, {lat: 0, lng:0});
    }
    this.zoomMap = 16;
    this.showMap = true;
    console.log(<any>this.$refs.map);
  }
}
