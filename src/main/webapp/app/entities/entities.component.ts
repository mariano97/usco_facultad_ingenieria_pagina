import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import FacultadService from './facultad/facultad.service';
import TablaTiposCatalogoService from './tabla-tipos-catalogo/tabla-tipos-catalogo.service';
import TablaElementoCatalogoService from './tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import ProgramaService from './programa/programa.service';
import SedeService from './sede/sede.service';
import ProfesorService from './profesor/profesor.service';
import GoogleStorageService from '@/shared/services/google-storage.service';
import ArchivosProgramaService from './archivos-programa/archivos-programa.service';
import UtilsService from '@/shared/services/utils.service';
import RedesProgramaService from './redes-programa/redes-programa.service';
import PaisesService from './paises/paises.service';
import EstadosService from './estados/estados.service';
import CiudadService from './ciudad/ciudad.service';
import OpenStreetMapService from '@/shared/services/open-street-map.service';
import UsuarioProfesorFullService from '@/shared/services/usuario-profesor.service';
import TituloAcademicoProfesorService from './titulo-academico-profesor/titulo-academico-profesor.service';
import CursoMateriaService from './curso-materia/curso-materia.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('facultadService') private facultadService = () => new FacultadService();
  @Provide('tablaTiposCatalogoService') private tablaTiposCatalogoService = () => new TablaTiposCatalogoService();
  @Provide('tablaElementoCatalogoService') private tablaElementoCatalogoService = () => new TablaElementoCatalogoService();
  @Provide('programaService') private programaService = () => new ProgramaService();
  @Provide('sedeService') private sedeService = () => new SedeService();
  @Provide('profesorService') private profesorService = () => new ProfesorService();
  @Provide('archivosProgramaService') private archivosProgramaService = () => new ArchivosProgramaService();
  @Provide('redesProgramaService') private redesProgramaService = () => new RedesProgramaService();
  @Provide('paisesService') private paisesService = () => new PaisesService();
  @Provide('estadosService') private estadosService = () => new EstadosService();
  @Provide('ciudadService') private ciudadService = () => new CiudadService();
  @Provide('tituloAcademicoProfesorService') private tituloAcademicoProfesorService = () => new TituloAcademicoProfesorService();
  @Provide('cursoMateriaService') private cursoMateriaService = () => new CursoMateriaService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  @Provide('googleStorageService') private googleStorageService = () => new GoogleStorageService();
  @Provide('utilsService') private utilsService = () => new UtilsService();
  @Provide('openStreetMapService') private openStreetMapService = () => new OpenStreetMapService();
  @Provide('usuarioProfesorFullService') private usuarioProfesorFullService = () => new UsuarioProfesorFullService();
}
