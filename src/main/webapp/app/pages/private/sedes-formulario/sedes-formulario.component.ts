import { required, decimal, maxLength } from 'vuelidate/lib/validators';
import { ISede, Sede } from './../../../shared/model/sede.model';
import SedeService from '@/entities/sede/sede.service';
import { Vue, Component, Inject } from 'vue-property-decorator';
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
})
export default class SedesFormulario extends Vue {
  @Inject('sedeService') private sedeService: () => SedeService;

  public sede: ISede = new Sede();

  private consultarSede(codigoIndicativo: string): void {
    this.sede = {};
    this.sedeService()
      .findByCodigoIndicativo(codigoIndicativo)
      .then(res => {
        this.sede = res;
      });
  }

}
