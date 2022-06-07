import { IPrograma } from '@/shared/model/programa.model';

export interface IArchivosPrograma {
  id?: number;
  urlName?: string;
  generationStorage?: number;
  storageContentType?: string | null;
  tipoDocumento?: string | null;
  planEstudio?: boolean;
  programa?: IPrograma;
}

export class ArchivosPrograma implements IArchivosPrograma {
  constructor(
    public id?: number,
    public urlName?: string,
    public generationStorage?: number,
    public storageContentType?: string | null,
    public tipoDocumento?: string | null,
    public planEstudio?: boolean,
    public programa?: IPrograma
  ) {
    this.planEstudio = this.planEstudio ?? false;
  }
}
