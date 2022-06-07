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
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  @Provide('googleStorageService') private googleStorageService = () => new GoogleStorageService();
}
