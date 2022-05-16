export interface ITablaTiposCatalogo {
  id?: number;
  nombre?: string;
  estado?: boolean;
  keyIdentificador?: string;
}

export class TablaTiposCatalogo implements ITablaTiposCatalogo {
  constructor(public id?: number, public nombre?: string, public estado?: boolean, public keyIdentificador?: string) {
    this.estado = this.estado ?? false;
  }
}
