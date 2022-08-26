import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { IFacultad } from '@/shared/model/facultad.model';

export interface ILaboratorio {
  id?: number;
  nombre?: string;
  informacionGeneral?: string;
  urlFoto?: string | null;
  latitud?: number | null;
  longitud?: number | null;
  correoContacto?: string;
  direccion?: string | null;
  tipoLaboratorio?: ITablaElementoCatalogo;
  facultad?: IFacultad;
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
    public direccion?: string | null,
    public tipoLaboratorio?: ITablaElementoCatalogo,
    public facultad?: IFacultad
  ) {}
}
