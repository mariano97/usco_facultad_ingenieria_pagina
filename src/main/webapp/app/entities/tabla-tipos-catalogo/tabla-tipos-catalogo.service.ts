import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { ITablaTiposCatalogo } from '@/shared/model/tabla-tipos-catalogo.model';

const baseApiUrl = 'api/tabla-tipos-catalogos';

export default class TablaTiposCatalogoService {
  public find(id: number): Promise<ITablaTiposCatalogo> {
    return new Promise<ITablaTiposCatalogo>((resolve, reject) => {
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

  public create(entity: ITablaTiposCatalogo): Promise<ITablaTiposCatalogo> {
    return new Promise<ITablaTiposCatalogo>((resolve, reject) => {
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

  public update(entity: ITablaTiposCatalogo): Promise<ITablaTiposCatalogo> {
    return new Promise<ITablaTiposCatalogo>((resolve, reject) => {
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

  public partialUpdate(entity: ITablaTiposCatalogo): Promise<ITablaTiposCatalogo> {
    return new Promise<ITablaTiposCatalogo>((resolve, reject) => {
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
