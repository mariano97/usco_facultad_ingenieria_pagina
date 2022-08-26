import { ILaboratorio } from '@/shared/model/laboratorio.model';
import { ISemillero } from '@/shared/model/semillero.model';
import { IEvento } from '@/shared/model/evento.model';
import { INoticia } from '@/shared/model/noticia.model';
import { IProfesor } from '@/shared/model/profesor.model';
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

  public downloadFileByOnlyFileName(isAutenticate: boolean, fileName: string): Promise<any> {
    const url = isAutenticate ? baseApiUrl : baseApiUrlOpen;
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${url}/download/by-file-name`, {
          params: {
            fileName: fileName,
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

  public uploadFotoPerfilProfesor(contentType: string, profesorId: number, nameCarpeta: string, file: File): Promise<IProfesor> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<IProfesor>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/profesor-upload-files`, formdata, {
          params: {
            contentType: contentType,
            profesorId: profesorId,
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

  public uploadFotoNoticia(contentType: string, noticiaId: number, nameCarpeta: string, file: File): Promise<INoticia> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<INoticia>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/noticia-upload-files`, formdata, {
          params: {
            contentType: contentType,
            noticiaId: noticiaId,
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

  public uploadFotoEvento(contentType: string, eventoId: number, nameCarpeta: string, file: File): Promise<IEvento> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<IEvento>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/evento-upload-files`, formdata, {
          params: {
            contentType: contentType,
            eventoId: eventoId,
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

  public uploadFotoSemillero(contentType: string, semilleroId: number, nameCarpeta: string, file: File): Promise<ISemillero> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<ISemillero>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/semillero-upload-files`, formdata, {
          params: {
            contentType: contentType,
            semilleroId: semilleroId,
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

  public uploadFotoLaboratorio(contentType: string, laboratorioId: number, nameCarpeta: string, file: File): Promise<ILaboratorio> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<ILaboratorio>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/laboratorio-upload-files`, formdata, {
          params: {
            contentType: contentType,
            laboratorioId: laboratorioId,
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
