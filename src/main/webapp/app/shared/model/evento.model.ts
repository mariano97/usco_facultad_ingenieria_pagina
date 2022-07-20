import { IFacultad } from '@/shared/model/facultad.model';

export interface IEvento {
  id?: number;
  titulo?: string;
  cuerpo?: string;
  fecha?: Date;
  hora?: Date;
  lugar?: string;
  urlFoto?: string | null;
  facultad?: IFacultad;
}

export class Evento implements IEvento {
  constructor(
    public id?: number,
    public titulo?: string,
    public cuerpo?: string,
    public fecha?: Date,
    public hora?: Date,
    public lugar?: string,
    public urlFoto?: string | null,
    public facultad?: IFacultad
  ) {}
}
