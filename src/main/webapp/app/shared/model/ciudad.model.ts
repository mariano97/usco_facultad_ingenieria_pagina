import { IEstados } from '@/shared/model/estados.model';

export interface ICiudad {
  id?: number;
  nombre?: string;
  codigo?: string;
  latitud?: number | null;
  longitud?: number | null;
  estados?: IEstados;
}

export class Ciudad implements ICiudad {
  constructor(
    public id?: number,
    public nombre?: string,
    public codigo?: string,
    public latitud?: number | null,
    public longitud?: number | null,
    public estados?: IEstados
  ) {}
}
