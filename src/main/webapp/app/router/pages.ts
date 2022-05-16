import { Authority } from '@/shared/security/authority';

// prettier-ignore

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

const ProgramasLista = () => import('@/pages/programas-lista/programas-lista.vue');
const Pages = () => import('@/pages/pages.vue');

export default {
  path: 'faultad-ingenieria',
  component: Pages,
  children: [
    {
      path: 'programas-lista',
      name: 'Programas',
      component: ProgramasLista,
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ]
}

