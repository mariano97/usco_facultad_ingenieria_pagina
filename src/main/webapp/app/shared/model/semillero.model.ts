import { IFacultad } from '@/shared/model/facultad.model';
import { IProfesor } from '@/shared/model/profesor.model';

export interface ISemillero {
  id?: number;
  nombre?: string;
  informacionGeneral?: string;
  urlFoto?: string | null;
  facultad?: IFacultad;
  profesor?: IProfesor | null;
}

export class Semillero implements ISemillero {
  constructor(
    public id?: number,
    public nombre?: string,
    public informacionGeneral?: string,
    public urlFoto?: string | null,
    public facultad?: IFacultad,
    public profesor?: IProfesor | null
  ) {}
}
