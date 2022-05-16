import { Authority } from '@/shared/security/authority';

// prettier-ignore

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

const ProgramasLista = () => import('@/core/programas-lista/programas-lista.vue');

export default [
  {
    path: '/programas-lista',
    name: 'Programas',
    component: ProgramasLista,
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here

]
