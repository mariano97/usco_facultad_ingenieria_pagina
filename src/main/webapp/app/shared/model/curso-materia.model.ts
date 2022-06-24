import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

export interface ICursoMateria {
  id?: number;
  nombre?: string;
  numeroCreditos?: number;
  semestreImpartida?: number;
  nivelAcademico?: ITablaElementoCatalogo;
}

export class CursoMateria implements ICursoMateria {
  constructor(
    public id?: number,
    public nombre?: string,
    public numeroCreditos?: number,
    public semestreImpartida?: number,
    public nivelAcademico?: ITablaElementoCatalogo
  ) {}
}
