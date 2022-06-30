import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { IFacultad } from '@/shared/model/facultad.model';
import { IPrograma } from '@/shared/model/programa.model';
import { ICursoMateria } from '@/shared/model/curso-materia.model';

export interface IProfesor {
  id?: number;
  emailAlternativo?: string | null;
  activo?: boolean;
  perfil?: string;
  telefonoCelular?: string | null;
  oficina?: string | null;
  userId?: number;
  urlFotoProfesor?: string | null;
  tablaElementoCatalogo?: ITablaElementoCatalogo;
  facultad?: IFacultad;
  programas?: IPrograma[] | null;
  cursoMaterias?: ICursoMateria[] | null;
}

export class Profesor implements IProfesor {
  constructor(
    public id?: number,
    public emailAlternativo?: string | null,
    public activo?: boolean,
    public perfil?: string,
    public telefonoCelular?: string | null,
    public oficina?: string | null,
    public userId?: number,
    public urlFotoProfesor?: string | null,
    public tablaElementoCatalogo?: ITablaElementoCatalogo,
    public facultad?: IFacultad,
    public programas?: IPrograma[] | null,
    public cursoMaterias?: ICursoMateria[] | null
  ) {
    this.activo = this.activo ?? false;
  }
}
