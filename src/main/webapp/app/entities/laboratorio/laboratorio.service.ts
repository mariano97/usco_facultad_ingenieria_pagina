import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { ILaboratorio } from '@/shared/model/laboratorio.model';

const baseApiUrl = 'api/laboratorios';
const baseOpenApiUrl = 'api/open/laboratorios';

export default class LaboratorioService {
  public find(id: number): Promise<ILaboratorio> {
    return new Promise<ILaboratorio>((resolve, reject) => {
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

  public findCustom(isAutenticado: boolean, id: number): Promise<ILaboratorio> {
    const url = isAutenticado ? baseApiUrl : baseOpenApiUrl;
    return new Promise<ILaboratorio>((resolve, reject) => {
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

  public hasTipoLaboratorio(isAutenticado: boolean, tipoLaboratorioId: number): Promise<boolean> {
    const url = isAutenticado ? baseApiUrl : baseOpenApiUrl;
    return new Promise<boolean>((resolve, reject) => {
      axios
        .get(`${url}/has-laboratorio/${tipoLaboratorioId}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getAllByTipoLaboratorio(isAutenticado: boolean, tipoLaboratorioId: number): Promise<ILaboratorio[]> {
    const url = isAutenticado ? baseApiUrl : baseOpenApiUrl;
    return new Promise<ILaboratorio[]>((resolve, reject) => {
      axios
        .get(`${url}/by-tipo-laboratorio/${tipoLaboratorioId}`)
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

  public retrieveReales(isAutenticado: boolean, paginationQuery?: any): Promise<any> {
    const url = isAutenticado ? baseApiUrl : baseOpenApiUrl;
    return new Promise<any>((resolve, reject) => {
      axios
        .get(url + `/reales?${buildPaginationQueryOpts(paginationQuery)}`)
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

  public create(entity: ILaboratorio): Promise<ILaboratorio> {
    return new Promise<ILaboratorio>((resolve, reject) => {
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

  public update(entity: ILaboratorio): Promise<ILaboratorio> {
    return new Promise<ILaboratorio>((resolve, reject) => {
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

  public partialUpdate(entity: ILaboratorio): Promise<ILaboratorio> {
    return new Promise<ILaboratorio>((resolve, reject) => {
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
