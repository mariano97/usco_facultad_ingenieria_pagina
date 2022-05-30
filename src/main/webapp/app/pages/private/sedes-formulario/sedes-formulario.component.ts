import { required, decimal, maxLength } from 'vuelidate/lib/validators';
import { ISede, Sede } from './../../../shared/model/sede.model';
import SedeService from '@/entities/sede/sede.service';
import { Vue, Component, Inject } from 'vue-property-decorator';

import { LMap, LTileLayer, LMarker  } from "vue2-leaflet";
import { OpenStreetMapProvider } from 'leaflet-geosearch';
import VGeosearch from 'vue2-leaflet-geosearch';
import 'leaflet/dist/leaflet.css';

import './sedes-formulario.scss';

const validations: any = {
  sede: {
    nombre: {
      required,
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
    },
    codigoIndicativo: {
      required,
    },
  },
};

@Component({
  validations,
  components: {
    LMap,
    LTileLayer,
    VGeosearch,
  },
})
export default class SedesFormulario extends Vue {
  @Inject('sedeService') private sedeService: () => SedeService;

  public sede: ISede = new Sede();

  //Doc
  /**
   * https://vue2-leaflet.netlify.app/quickstart/#in-a-webpack-rollup-build-system
   * https://codesandbox.io/s/uzh4k4?file=/App.vue
   * https://vue2-leaflet.netlify.app/plugins/#vue2-leaflet-geosearch
   * https://github.com/fega/vue2-leaflet-geosearch
   * https://github.com/smeijer/leaflet-geosearch
   * https://github.com/smeijer/leaflet-geosearch
   * https://www.youtube.com/watch?v=1ZxMxLiu0i4&t=326s
   * https://medium.com/swlh/create-an-interactive-location-selector-with-vue-js-and-leaflet-5808c55b4636
   * https://github.com/eyuelberga/vue-leaflet-geolocation-selector
   * https://www.npmjs.com/package/leaflet-geosearch
   * https://wiki.openstreetmap.org/wiki/API
   * https://www.latlong.net/Show-Latitude-Longitude.html
   *
   */
  geosearchOptions = {
    provider: new OpenStreetMapProvider(),

  };

  public copyrightMap = '&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors';
  public urlMap = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';

  private consultarSede(codigoIndicativo: string): void {
    this.sede = {};
    this.sedeService()
      .findByCodigoIndicativo(codigoIndicativo)
      .then(res => {
        this.sede = res;
      });
  }

}
