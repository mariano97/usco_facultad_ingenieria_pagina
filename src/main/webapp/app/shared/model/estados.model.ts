import { IPaises } from '@/shared/model/paises.model';

export interface IEstados {
  id?: number;
  nombre?: string;
  codigo?: string;
  paises?: IPaises;
}

export class Estados implements IEstados {
  constructor(public id?: number, public nombre?: string, public codigo?: string, public paises?: IPaises) {}
}
