export interface IFacultad {
  id?: number;
  nombre?: string;
}

export class Facultad implements IFacultad {
  constructor(public id?: number, public nombre?: string) {}
}
