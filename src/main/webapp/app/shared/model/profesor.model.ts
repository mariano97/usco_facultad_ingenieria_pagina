export interface IProfesor {
  id?: number;
  segundoNombre?: string | null;
  segundoApellido?: string | null;
  emailAlternativo?: string | null;
  activo?: boolean;
  perfil?: string;
  telefonoCelular?: string | null;
  oficina?: string | null;
}

export class Profesor implements IProfesor {
  constructor(
    public id?: number,
    public segundoNombre?: string | null,
    public segundoApellido?: string | null,
    public emailAlternativo?: string | null,
    public activo?: boolean,
    public perfil?: string,
    public telefonoCelular?: string | null,
    public oficina?: string | null
  ) {
    this.activo = this.activo ?? false;
  }
}
