import { IUsuarioProfesorFull } from './../model/usuario-profesor.model';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
import { IUser } from '@/shared/model/user.model';
import axios from 'axios';

const baseApiUrl = '/api/usuario-profesor-full';
const openAaseApiUrl = '/api/open/usuario-profesor-full';

export default class UsuarioProfesorFullService {
  public getOneUsuarioProfesorByUserLogin(isAutenticate: boolean, userLogin: string): Promise<IUsuarioProfesorFull> {
    const url = isAutenticate ? baseApiUrl : openAaseApiUrl;
    return new Promise<IUsuarioProfesorFull>((resolve, reject) => {
      axios
        .get(`${url}/by-user-login`, {
          params: {
            userLogin: userLogin,
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

  public getAllUsuariosProfesor(isAutenticate: boolean, requ?: any, auths?: string[], nameCompleteFilter?: string): Promise<any> {
    const url = isAutenticate ? baseApiUrl : openAaseApiUrl;
    let query = buildPaginationQueryOpts(requ);
    if (auths !== null && auths !== undefined) {
      if (query.length > 0) {
        query += '&';
      }
      query += 'auths=';
      auths.map((auth, index) => {
        query += auth;
        if (index + 1 < auths.length) {
          query += ',';
        }
      });
    }
    if (nameCompleteFilter !== null && nameCompleteFilter !== undefined) {
      if (query.length > 0) {
        query += '&';
      }
      query += 'nameCompleteFilter=';
      query += nameCompleteFilter.trim();
    }
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${url}?${query}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public crearUsuarioProfesor(usuarioProfesorFull: IUsuarioProfesorFull): Promise<IUsuarioProfesorFull> {
    return new Promise<IUsuarioProfesorFull>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, usuarioProfesorFull)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public updateUsuarioProfesor(usuarioProfesorFull: IUsuarioProfesorFull): Promise<IUsuarioProfesorFull> {
    return new Promise<IUsuarioProfesorFull>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}`, usuarioProfesorFull)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
