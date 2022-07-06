import { Authority } from '@/shared/security/authority';

// prettier-ignore

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

const ProgramasLista = () => import('@/pages/public/programas-lista/programas-lista.vue');
const Programa = () => import('@/pages/public/programa/programa.vue');
const Pages = () => import('@/pages/pages.vue');
const ProfesoradoLista = () => import('@/pages/public/profesorado-lista/profesorado-lista.vue');

export default {
  path: 'faultad-ingenieria',
  component: Pages,
  children: [
    {
      path: 'programas-lista',
      name: 'Programas_list',
      component: ProgramasLista,
    },
    {
      path: 'programa-descripcion/:codigo_snies',
      name: 'Programa_descripcion',
      component: Programa,
    },
    {
      path: 'programa-profesorado-lista',
      name: 'profesorado_lista_general',
      component: ProfesoradoLista,
    },
    {
      path: 'programa-profesorado-lista/:programa_codigo_snies',
      name: 'profesorado_lista_programa_codigo_snies',
      component: ProfesoradoLista,
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ]
}

