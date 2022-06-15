export interface IPaises {
  id?: number;
  nombrePais?: string;
  codigoPais?: string;
}

export class Paises implements IPaises {
  constructor(public id?: number, public nombrePais?: string, public codigoPais?: string) {}
}
