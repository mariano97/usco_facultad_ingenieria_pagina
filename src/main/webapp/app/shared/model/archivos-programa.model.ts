import { IPrograma } from '@/shared/model/programa.model';
import { ITablaElementoCatalogo } from '@/shared/model/tabla-elemento-catalogo.model';

export interface IArchivosPrograma {
  id?: number;
  urlName?: string;
  generationStorage?: number;
  storageContentType?: string | null;
  tipoDocumento?: string | null;
  planEstudio?: boolean;
  microDiseno?: boolean;
  nombreArchivo?: string;
  programa?: IPrograma;
  tablaElementoCatalogo?: ITablaElementoCatalogo;
}

export class ArchivosPrograma implements IArchivosPrograma {
  constructor(
    public id?: number,
    public urlName?: string,
    public generationStorage?: number,
    public storageContentType?: string | null,
    public tipoDocumento?: string | null,
    public planEstudio?: boolean,
    public microDiseno?: boolean,
    public nombreArchivo?: string,
    public programa?: IPrograma,
    public tablaElementoCatalogo?: ITablaElementoCatalogo
  ) {
    this.planEstudio = this.planEstudio ?? false;
    this.microDiseno = this.microDiseno ?? false;
  }
}
