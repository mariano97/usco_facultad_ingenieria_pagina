import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { IProfesor } from '@/shared/model/profesor.model';

export interface ICursoMateria {
  id?: number;
  nombre?: string;
  numeroCreditos?: number;
  semestreImpartida?: number;
  nivelAcademico?: ITablaElementoCatalogo;
  profesors?: IProfesor[] | null;
}

export class CursoMateria implements ICursoMateria {
  constructor(
    public id?: number,
    public nombre?: string,
    public numeroCreditos?: number,
    public semestreImpartida?: number,
    public nivelAcademico?: ITablaElementoCatalogo,
    public profesors?: IProfesor[] | null
  ) {}
}
