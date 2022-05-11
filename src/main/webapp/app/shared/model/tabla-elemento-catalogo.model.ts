import { ITablaTiposCatalogo } from '@/shared/model/tabla-tipos-catalogo.model';

export interface ITablaElementoCatalogo {
  id?: number;
  nombre?: string;
  abreviatura?: string | null;
  estado?: boolean;
  tablaTiposCatalogo?: ITablaTiposCatalogo;
}

export class TablaElementoCatalogo implements ITablaElementoCatalogo {
  constructor(
    public id?: number,
    public nombre?: string,
    public abreviatura?: string | null,
    public estado?: boolean,
    public tablaTiposCatalogo?: ITablaTiposCatalogo
  ) {
    this.estado = this.estado ?? false;
  }
}
