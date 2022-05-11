export interface ITablaTiposCatalogo {
  id?: number;
  nombre?: string;
  estado?: boolean;
}

export class TablaTiposCatalogo implements ITablaTiposCatalogo {
  constructor(public id?: number, public nombre?: string, public estado?: boolean) {
    this.estado = this.estado ?? false;
  }
}
