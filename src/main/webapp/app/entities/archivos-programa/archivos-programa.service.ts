import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IArchivosPrograma } from '@/shared/model/archivos-programa.model';

const baseApiUrl = 'api/archivos-programas';

export default class ArchivosProgramaService {
  public find(id: number): Promise<IArchivosPrograma> {
    return new Promise<IArchivosPrograma>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl + `?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getAllByProgramaId(programaId: number): Promise<IArchivosPrograma[]> {
    return new Promise<IArchivosPrograma[]>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/by-programa-id/${programaId}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: IArchivosPrograma): Promise<IArchivosPrograma> {
    return new Promise<IArchivosPrograma>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IArchivosPrograma): Promise<IArchivosPrograma> {
    return new Promise<IArchivosPrograma>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IArchivosPrograma): Promise<IArchivosPrograma> {
    return new Promise<IArchivosPrograma>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
