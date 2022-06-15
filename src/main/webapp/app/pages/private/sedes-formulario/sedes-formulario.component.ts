import { required, decimal, maxLength, email, helpers } from 'vuelidate/lib/validators';
import { ISede, Sede } from './../../../shared/model/sede.model';
import SedeService from '@/entities/sede/sede.service';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './sedes-formulario.scss';
import AlertService from '@/shared/alert/alert.service';
import * as validators from '@/shared/validators/validators.component';
import { LMap, LTileLayer, LMarker  } from "vue2-leaflet";
import 'leaflet/dist/leaflet.css';

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
  },
};

@Component({
  validations,
  components: {
    LMap,
    LTileLayer,
  },
})
export default class SedesFormulario extends Vue {
  @Inject('sedeService') private sedeService: () => SedeService;
  @Inject('alertService') private alertService: () => AlertService;

  public copyrightMap = '&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors';
  public urlMap = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';

  public sede: ISede = new Sede();

  public coordenadas = {
    lat: 5.41322,
    lon: -70.219482,
  };

  public zoomMap = 6;
  public showMap = true;

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
    });
  }

  private consultarSede(codigoIndicativo: string): void {
    this.sede = {};
    this.sedeService()
      .findByCodigoIndicativo(codigoIndicativo)
      .then(res => {
        this.sede = res;
      });
  }

  public guardar(): void {
    this.isSaving = true;
    if (this.sede.id) {
      this.sedeService()
        .update(this.sede)
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
    console.log('dentro de clickMap');
    console.log(event);
  }

  public changeCoordenadas(event): void {
    this.showMap = false;
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
