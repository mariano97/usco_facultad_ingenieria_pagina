import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';
import axios from 'axios';

const baseApiUrlOpen = 'api/open/google-cloud-storage';
const baseApiUrl = 'api/google-cloud-storage';

export default class GoogleStorageService {
  public downloadFile(isAutenticate: boolean, fileName: string, generation: number): Promise<any> {
    const url = isAutenticate ? baseApiUrl : baseApiUrlOpen;
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${url}/download`, {
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

  public uploadProgramaFile(
    contentType: string,
    programaId: number,
    elementoCatalogoId: number,
    nameCarpeta: string,
    file: File
  ): Promise<IArchivosPrograma> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<IArchivosPrograma>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/programa-upload-files`, formdata, {
          params: {
            contentType: contentType,
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

  public updateFileArchivoPrograma(contentType: string, carpetaName: string, archivoProgramaId: number, file: File): Promise<IArchivosPrograma> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<IArchivosPrograma>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/programa-upload-files`, formdata, {
          params: {
            contentType: contentType,
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

  public deleteArchivoProgramaUploaded(archivoProgramaId: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}`, {
          params: {
            archivoProgramaId: archivoProgramaId,
          },
        })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
