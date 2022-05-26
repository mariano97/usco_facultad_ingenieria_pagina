
export interface IPresentacionBasico {
  title?: string;
  descripcion?: string;
}

export class PresentacionBasico implements IPresentacionBasico {
  constructor(public title?: string, public descripcion?: string) {}
}
