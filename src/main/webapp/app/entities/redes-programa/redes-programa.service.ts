import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IRedesPrograma } from '@/shared/model/redes-programa.model';

const baseApiUrl = 'api/redes-programas';
const baseOpenApiUrl = 'api/open/redes-programas';

export default class RedesProgramaService {
  public findAllByProgramaId(isAutenticate: boolean, programaId: number): Promise<IRedesPrograma[]> {
    const url = isAutenticate ? `${baseApiUrl}` : `${baseOpenApiUrl}`;
    return new Promise<IRedesPrograma[]>((resolve, reject) => {
      axios
        .get(`${url}/by-programa-id/${programaId}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public find(id: number): Promise<IRedesPrograma> {
    return new Promise<IRedesPrograma>((resolve, reject) => {
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

  public create(entity: IRedesPrograma): Promise<IRedesPrograma> {
    return new Promise<IRedesPrograma>((resolve, reject) => {
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

  public update(entity: IRedesPrograma): Promise<IRedesPrograma> {
    return new Promise<IRedesPrograma>((resolve, reject) => {
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

  public partialUpdate(entity: IRedesPrograma): Promise<IRedesPrograma> {
    return new Promise<IRedesPrograma>((resolve, reject) => {
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
