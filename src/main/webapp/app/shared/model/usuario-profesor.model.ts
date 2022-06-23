import { IProfesor, Profesor } from '@/shared/model/profesor.model';
import { IUser, User } from '@/shared/model/user.model';

export interface IUsuarioProfesorFull {
  adminUserDTO?: IUser;
  profesorDTO?: IProfesor;
}

export class UsuarioProfesorFull implements IUsuarioProfesorFull {
  constructor(public adminUserDTO?: IUser, public profesorDTO?: IProfesor) {
  }
}
