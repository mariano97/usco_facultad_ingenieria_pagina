export interface ILaboratorio {
  id?: number;
  nombre?: string;
  informacionGeneral?: string;
  urlFoto?: string | null;
  latitud?: number | null;
  longitud?: number | null;
  correoContacto?: string;
  direccion?: string | null;
}

export class Laboratorio implements ILaboratorio {
  constructor(
    public id?: number,
    public nombre?: string,
    public informacionGeneral?: string,
    public urlFoto?: string | null,
    public latitud?: number | null,
    public longitud?: number | null,
    public correoContacto?: string,
    public direccion?: string | null
  ) {}
}
