import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';

export interface IFileDocumentoNuevo {
  file?: File;
  nombre?: string;
  size?: number;
  tipoDocumento?: string;
  fileBase64?: string | ArrayBuffer;
  isValidDoc?: boolean;
  archivoDocumentoPrograma?: IArchivosPrograma;
}

export interface IFileDownloaded {
  fileName?: string;
  file?: any;
}
