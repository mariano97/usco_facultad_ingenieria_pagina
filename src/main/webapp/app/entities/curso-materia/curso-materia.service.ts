import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { ICursoMateria } from '@/shared/model/curso-materia.model';

const baseApiUrl = 'api/curso-materias';
const openBaseApiUrl = 'api/open/curso-materias';

export default class CursoMateriaService {
  public findAllByProfesorId(isAutenticate: boolean, profesorId: number): Promise<ICursoMateria[]> {
    const url = isAutenticate ? baseApiUrl : openBaseApiUrl;
    return new Promise<ICursoMateria[]>((resolve, reject) => {
      axios
        .get(`${url}/by-profesor-id/${profesorId}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public findAllMateriasPagination(isAutenticate: boolean, pagination?: any): Promise<any> {
    const url = isAutenticate ? baseApiUrl : openBaseApiUrl;
    return new Promise<any>((resolve, reject) => {
      axios
        .get(url + `?${buildPaginationQueryOpts(pagination)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public find(id: number): Promise<ICursoMateria> {
    return new Promise<ICursoMateria>((resolve, reject) => {
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

  public create(entity: ICursoMateria): Promise<ICursoMateria> {
    return new Promise<ICursoMateria>((resolve, reject) => {
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

  public update(entity: ICursoMateria): Promise<ICursoMateria> {
    return new Promise<ICursoMateria>((resolve, reject) => {
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

  public partialUpdate(entity: ICursoMateria): Promise<ICursoMateria> {
    return new Promise<ICursoMateria>((resolve, reject) => {
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
