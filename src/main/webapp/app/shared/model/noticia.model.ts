import { IFacultad } from '@/shared/model/facultad.model';

export interface INoticia {
  id?: number;
  titulo?: string;
  sintesis?: string;
  cuerpoDescripcion?: string;
  fecha?: Date;
  urlFoto?: string | null;
  facultad?: IFacultad;
}

export class Noticia implements INoticia {
  constructor(
    public id?: number,
    public titulo?: string,
    public sintesis?: string,
    public cuerpoDescripcion?: string,
    public fecha?: Date,
    public urlFoto?: string | null,
    public facultad?: IFacultad
  ) {}
}
