import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import FacultadService from './facultad/facultad.service';
import TablaTiposCatalogoService from './tabla-tipos-catalogo/tabla-tipos-catalogo.service';
import TablaElementoCatalogoService from './tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import ProgramaService from './programa/programa.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('facultadService') private facultadService = () => new FacultadService();
  @Provide('tablaTiposCatalogoService') private tablaTiposCatalogoService = () => new TablaTiposCatalogoService();
  @Provide('tablaElementoCatalogoService') private tablaElementoCatalogoService = () => new TablaElementoCatalogoService();
  @Provide('programaService') private programaService = () => new ProgramaService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
