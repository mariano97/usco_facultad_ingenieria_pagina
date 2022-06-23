import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { IProfesor } from '@/shared/model/profesor.model';
import { IPaises } from '@/shared/model/paises.model';

export interface ITituloAcademicoProfesor {
  id?: number;
  nombreTitulo?: string;
  nombreUniversidadTitulo?: string;
  yearTitulo?: Date;
  tablaElementoCatalogo?: ITablaElementoCatalogo;
  profesor?: IProfesor;
  paises?: IPaises;
}

export class TituloAcademicoProfesor implements ITituloAcademicoProfesor {
  constructor(
    public id?: number,
    public nombreTitulo?: string,
    public nombreUniversidadTitulo?: string,
    public yearTitulo?: Date,
    public tablaElementoCatalogo?: ITablaElementoCatalogo,
    public profesor?: IProfesor,
    public paises?: IPaises
  ) {}
}
