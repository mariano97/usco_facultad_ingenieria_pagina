import { IProfesor } from '@/shared/model/profesor.model';

export interface IEscalafonProfesor {
  id?: number;
  puntucacionPromedio?: number;
  fecha?: Date;
  profesor?: IProfesor;
}

export class EscalafonProfesor implements IEscalafonProfesor {
  constructor(public id?: number, public puntucacionPromedio?: number, public fecha?: Date, public profesor?: IProfesor) {}
}
