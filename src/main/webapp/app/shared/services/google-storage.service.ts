import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';
import axios from 'axios';

const baseApiUrlOpen = 'api/open/google-cloud-storage';
const baseApiUrl = 'api/google-cloud-storage';

export default class GoogleStorageService {
  public downloadFile(fileName: string, generation: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrlOpen}`, {
          params: {
            fileName: fileName,
            generation: generation,
          },
        })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public uploadProgramaFile(programaId: number, elementoCatalogoId: number, nameCarpeta: string, file: File): Promise<IArchivosPrograma> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<IArchivosPrograma>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/programa-upload-files`, formdata, {
          params: {
            programaId: programaId,
            elementoCatalogoId: elementoCatalogoId,
            carpeta: nameCarpeta,
          },
        })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public updateFileArchivoPrograma(carpetaName: string, archivoProgramaId: number, file: File): Promise<IArchivosPrograma> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<IArchivosPrograma>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/programa-upload-files`, formdata, {
          params: {
            carpeta: carpetaName,
            archivosProgramaId: archivoProgramaId,
          },
        })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public uploadFile(file: File, nameCarpeta: string): Promise<string> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<string>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/upload`, formdata, {
          params: {
            carpeta: nameCarpeta,
          },
        })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
