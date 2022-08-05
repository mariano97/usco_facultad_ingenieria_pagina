import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { IEscalafonProfesor } from '@/shared/model/escalafon-profesor.model';

const baseApiUrl = 'api/escalafon-profesors';
const baseApiUrlOpen = 'api/open/escalafon-profesors';

export default class EscalafonProfesorService {
  public find(id: number): Promise<IEscalafonProfesor> {
    return new Promise<IEscalafonProfesor>((resolve, reject) => {
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

  public findByProfesorId(isAutenticate: boolean, profesorId: number): Promise<IEscalafonProfesor[]> {
    const url = isAutenticate ? baseApiUrl : baseApiUrlOpen;
    return new Promise<IEscalafonProfesor[]>((resolve, reject) => {
      axios
        .get(`${url}/by-profesor-id`, {
          params: {
            profesorId: profesorId,
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

  public create(entity: IEscalafonProfesor): Promise<IEscalafonProfesor> {
    return new Promise<IEscalafonProfesor>((resolve, reject) => {
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

  public update(entity: IEscalafonProfesor): Promise<IEscalafonProfesor> {
    return new Promise<IEscalafonProfesor>((resolve, reject) => {
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

  public partialUpdate(entity: IEscalafonProfesor): Promise<IEscalafonProfesor> {
    return new Promise<IEscalafonProfesor>((resolve, reject) => {
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
