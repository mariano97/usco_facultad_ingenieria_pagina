export interface ISede {
  id?: number;
  nombre?: string;
  latitud?: number;
  longitud?: number;
  direccion?: string;
  estado?: boolean;
}

export class Sede implements ISede {
  constructor(
    public id?: number,
    public nombre?: string,
    public latitud?: number,
    public longitud?: number,
    public direccion?: string,
    public estado?: boolean
  ) {
    this.estado = this.estado ?? false;
  }
}
