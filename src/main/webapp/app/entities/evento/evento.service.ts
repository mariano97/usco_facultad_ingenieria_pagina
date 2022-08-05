import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IEvento } from '@/shared/model/evento.model';

const baseApiUrl = 'api/eventos';
const baseOpenApiUrl = 'api/open/eventos';

export default class EventoService {
  public find(id: number): Promise<IEvento> {
    return new Promise<IEvento>((resolve, reject) => {
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

  public findAllFechaMayorQue(isAutenticado: boolean, fecha: string, paginationQuery?: any): Promise<IEvento[]> {
    const url = isAutenticado ? baseApiUrl : baseOpenApiUrl;
    let query = buildPaginationQueryOpts(paginationQuery);
    if (query.length > 0) {
      query = query.concat('&fechaBase=').concat(fecha);
    }
    return new Promise<IEvento[]>((resolve, reject) => {
      axios
        .get(`${url}/find-by-fecha-mayor?${query}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public findCustom(isAutenticado: boolean, id: number): Promise<IEvento> {
    const url = isAutenticado ? baseApiUrl : baseOpenApiUrl;
    return new Promise<IEvento>((resolve, reject) => {
      axios
        .get(`${url}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieveCustom(isAutenticado: boolean, paginationQuery?: any): Promise<any> {
    const url = isAutenticado ? baseApiUrl : baseOpenApiUrl;
    return new Promise<any>((resolve, reject) => {
      axios
        .get(url + `?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
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

  public create(entity: IEvento): Promise<IEvento> {
    return new Promise<IEvento>((resolve, reject) => {
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

  public update(entity: IEvento): Promise<IEvento> {
    return new Promise<IEvento>((resolve, reject) => {
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

  public partialUpdate(entity: IEvento): Promise<IEvento> {
    return new Promise<IEvento>((resolve, reject) => {
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
