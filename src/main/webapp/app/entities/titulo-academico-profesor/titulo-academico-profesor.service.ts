import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { ITituloAcademicoProfesor } from '@/shared/model/titulo-academico-profesor.model';

const baseApiUrl = 'api/titulo-academico-profesors';

export default class TituloAcademicoProfesorService {
  public find(id: number): Promise<ITituloAcademicoProfesor> {
    return new Promise<ITituloAcademicoProfesor>((resolve, reject) => {
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

  public create(entity: ITituloAcademicoProfesor): Promise<ITituloAcademicoProfesor> {
    return new Promise<ITituloAcademicoProfesor>((resolve, reject) => {
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

  public update(entity: ITituloAcademicoProfesor): Promise<ITituloAcademicoProfesor> {
    return new Promise<ITituloAcademicoProfesor>((resolve, reject) => {
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

  public partialUpdate(entity: ITituloAcademicoProfesor): Promise<ITituloAcademicoProfesor> {
    return new Promise<ITituloAcademicoProfesor>((resolve, reject) => {
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
