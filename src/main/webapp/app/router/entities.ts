import { Authority } from '@/shared/security/authority';
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
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
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
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
