export interface IFacultad {
  id?: number;
  nombre?: string;
  mision?: string;
  vision?: string;
}

export class Facultad implements IFacultad {
  constructor(public id?: number, public nombre?: string, public mision?: string, public vision?: string) {}
}
