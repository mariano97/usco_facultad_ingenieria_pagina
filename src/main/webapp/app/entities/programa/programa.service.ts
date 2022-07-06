import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IPrograma } from '@/shared/model/programa.model';

const baseApiUrl = 'api/programas';
const baseOpenApiUrl = 'api/open/programas';

export default class ProgramaService {
  public find(id: number): Promise<IPrograma> {
    return new Promise<IPrograma>((resolve, reject) => {
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

  public findAll(isAutenticate: boolean): Promise<IPrograma[]> {
    const url = isAutenticate ? baseApiUrl : baseOpenApiUrl;
    return new Promise<IPrograma[]>((resolve, reject) => {
      axios
        .get(url)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public findByCodigoSnies(isAutenticate: boolean, codigoSnies: number): Promise<IPrograma> {
    const url = isAutenticate ? `${baseApiUrl}/by-codigo-snies` : `${baseOpenApiUrl}/by-codigo-snies`;
    return new Promise<IPrograma>((resolve, reject) => {
      axios
        .get(url, {
          params: {
            codigo_snies: codigoSnies,
          }
        })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public findNameProgramaByCodigoSnies(isAutenticate: boolean, codigoSnies: number): Promise<string> {
    const url = isAutenticate ? `${baseApiUrl}` : `${baseOpenApiUrl}`;
    return new Promise<string>((resolve, reject) => {
      axios
        .get(`${url}/obtner-nombre`, {
          params: {
            codigo_snies: codigoSnies,
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

  public create(entity: IPrograma): Promise<IPrograma> {
    return new Promise<IPrograma>((resolve, reject) => {
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

  public update(entity: IPrograma): Promise<IPrograma> {
    return new Promise<IPrograma>((resolve, reject) => {
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

  public partialUpdate(entity: IPrograma): Promise<IPrograma> {
    return new Promise<IPrograma>((resolve, reject) => {
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
