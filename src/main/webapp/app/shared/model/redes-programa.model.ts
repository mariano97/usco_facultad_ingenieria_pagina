import { IPrograma } from '@/shared/model/programa.model';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

export interface IRedesPrograma {
  id?: number;
  urlRedSocial?: string;
  programa?: IPrograma;
  tablaElementoCatalogo?: ITablaElementoCatalogo;
}

export class RedesPrograma implements IRedesPrograma {
  constructor(
    public id?: number,
    public urlRedSocial?: string,
    public programa?: IPrograma,
    public tablaElementoCatalogo?: ITablaElementoCatalogo
  ) {}
}
