import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';
import { IFacultad } from '@/shared/model/facultad.model';
import { ISede } from '@/shared/model/sede.model';
import { IProfesor } from '@/shared/model/profesor.model';

export interface IPrograma {
  id?: number;
  nombre?: string;
  codigoSnies?: number;
  codigoRegistroCalificado?: number;
  fechaRegistroCalificado?: Date;
  nombreTituloOtorgado?: string;
  numeroCreditos?: number;
  duracionPrograma?: number | null;
  presentacionPrograma?: string;
  mision?: string;
  vision?: string;
  perfilEstudiante?: string | null;
  perfilEgresado?: string;
  perfilOcupacional?: string | null;
  urlFotoPrograma?: string | null;
  dirigidoAQuien?: string | null;
  costoPrograma?: number;
  estado?: boolean;
  nivelFormacion?: ITablaElementoCatalogo;
  tipoFormacion?: ITablaElementoCatalogo;
  facultad?: IFacultad;
  sedes?: ISede[] | null;
  profesors?: IProfesor[] | null;
}

export class Programa implements IPrograma {
  constructor(
    public id?: number,
    public nombre?: string,
    public codigoSnies?: number,
    public codigoRegistroCalificado?: number,
    public fechaRegistroCalificado?: Date,
    public nombreTituloOtorgado?: string,
    public numeroCreditos?: number,
    public duracionPrograma?: number | null,
    public presentacionPrograma?: string,
    public mision?: string,
    public vision?: string,
    public perfilEstudiante?: string | null,
    public perfilEgresado?: string,
    public perfilOcupacional?: string | null,
    public urlFotoPrograma?: string | null,
    public dirigidoAQuien?: string | null,
    public costoPrograma?: number,
    public estado?: boolean,
    public nivelFormacion?: ITablaElementoCatalogo,
    public tipoFormacion?: ITablaElementoCatalogo,
    public facultad?: IFacultad,
    public sedes?: ISede[] | null,
    public profesors?: IProfesor[] | null
  ) {
    this.estado = this.estado ?? false;
  }
}
