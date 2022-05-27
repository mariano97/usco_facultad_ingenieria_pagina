import { IPrograma } from '@/shared/model/programa.model';

export interface ISede {
  id?: number;
  nombre?: string;
  latitud?: number;
  longitud?: number;
  direccion?: string;
  estado?: boolean;
  telefonoFijo?: string | null;
  telefonoCelular?: string | null;
  correoElectronico?: string;
  codigoIndicativo?: string;
  programas?: IPrograma[] | null;
}

export class Sede implements ISede {
  constructor(
    public id?: number,
    public nombre?: string,
    public latitud?: number,
    public longitud?: number,
    public direccion?: string,
    public estado?: boolean,
    public telefonoFijo?: string | null,
    public telefonoCelular?: string | null,
    public correoElectronico?: string,
    public codigoIndicativo?: string,
    public programas?: IPrograma[] | null
  ) {
    this.estado = this.estado ?? false;
  }
}
