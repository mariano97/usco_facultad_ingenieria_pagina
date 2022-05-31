import pages from '@/router/pages';
import { Authority } from '@/shared/security/authority';
import pagesUsuarios from './pages-usuarios';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Facultad = () => import('@/entities/facultad/facultad.vue');
// prettier-ignore
const FacultadUpdate = () => import('@/entities/facultad/facultad-update.vue');
// prettier-ignore
const FacultadDetails = () => import('@/entities/facultad/facultad-details.vue');
// prettier-ignore
const TablaTiposCatalogo = () => import('@/entities/tabla-tipos-catalogo/tabla-tipos-catalogo.vue');
// prettier-ignore
const TablaTiposCatalogoUpdate = () => import('@/entities/tabla-tipos-catalogo/tabla-tipos-catalogo-update.vue');
// prettier-ignore
const TablaTiposCatalogoDetails = () => import('@/entities/tabla-tipos-catalogo/tabla-tipos-catalogo-details.vue');
// prettier-ignore
const TablaElementoCatalogo = () => import('@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.vue');
// prettier-ignore
const TablaElementoCatalogoUpdate = () => import('@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo-update.vue');
// prettier-ignore
const TablaElementoCatalogoDetails = () => import('@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo-details.vue');
// prettier-ignore
const Programa = () => import('@/entities/programa/programa.vue');
// prettier-ignore
const ProgramaUpdate = () => import('@/entities/programa/programa-update.vue');
// prettier-ignore
const ProgramaDetails = () => import('@/entities/programa/programa-details.vue');
// prettier-ignore
const Sede = () => import('@/entities/sede/sede.vue');
// prettier-ignore
const SedeUpdate = () => import('@/entities/sede/sede-update.vue');
// prettier-ignore
const SedeDetails = () => import('@/entities/sede/sede-details.vue');
// prettier-ignore
const Profesor = () => import('@/entities/profesor/profesor.vue');
// prettier-ignore
const ProfesorUpdate = () => import('@/entities/profesor/profesor-update.vue');
// prettier-ignore
const ProfesorDetails = () => import('@/entities/profesor/profesor-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    pages,
    pagesUsuarios,
    {
      path: 'facultad',
      name: 'Facultad',
      component: Facultad,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'facultad/new',
      name: 'FacultadCreate',
      component: FacultadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'facultad/:facultadId/edit',
      name: 'FacultadEdit',
      component: FacultadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'facultad/:facultadId/view',
      name: 'FacultadView',
      component: FacultadDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-tipos-catalogo',
      name: 'TablaTiposCatalogo',
      component: TablaTiposCatalogo,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-tipos-catalogo/new',
      name: 'TablaTiposCatalogoCreate',
      component: TablaTiposCatalogoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-tipos-catalogo/:tablaTiposCatalogoId/edit',
      name: 'TablaTiposCatalogoEdit',
      component: TablaTiposCatalogoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-tipos-catalogo/:tablaTiposCatalogoId/view',
      name: 'TablaTiposCatalogoView',
      component: TablaTiposCatalogoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-elemento-catalogo',
      name: 'TablaElementoCatalogo',
      component: TablaElementoCatalogo,
      // meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-elemento-catalogo/new',
      name: 'TablaElementoCatalogoCreate',
      component: TablaElementoCatalogoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-elemento-catalogo/:tablaElementoCatalogoId/edit',
      name: 'TablaElementoCatalogoEdit',
      component: TablaElementoCatalogoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-elemento-catalogo/:tablaElementoCatalogoId/view',
      name: 'TablaElementoCatalogoView',
      component: TablaElementoCatalogoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'programa',
      name: 'Programa',
      component: Programa,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'programa/new',
      name: 'ProgramaCreate',
      component: ProgramaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'programa/:programaId/edit',
      name: 'ProgramaEdit',
      component: ProgramaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'programa/:programaId/view',
      name: 'ProgramaView',
      component: ProgramaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sede',
      name: 'Sede',
      component: Sede,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sede/new',
      name: 'SedeCreate',
      component: SedeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sede/:sedeId/edit',
      name: 'SedeEdit',
      component: SedeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sede/:sedeId/view',
      name: 'SedeView',
      component: SedeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'profesor',
      name: 'Profesor',
      component: Profesor,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'profesor/new',
      name: 'ProfesorCreate',
      component: ProfesorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'profesor/:profesorId/edit',
      name: 'ProfesorEdit',
      component: ProfesorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'profesor/:profesorId/view',
      name: 'ProfesorView',
      component: ProfesorDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
