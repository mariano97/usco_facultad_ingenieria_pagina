import { Authority } from '@/shared/security/authority';

// prettier-ignore

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

const ProgramasLista = () => import('@/pages/public/programas-lista/programas-lista.vue');
const Programa = () => import('@/pages/public/programa/programa.vue');
const Pages = () => import('@/pages/pages.vue');
const ProfesoradoLista = () => import('@/pages/public/profesorado-lista/profesorado-lista.vue');
const ProfesorInfo = () => import('@/pages/public/profesor-info/profesor-info.vue');
const NoticiasLista = () => import('@/pages/public/noticias-lista/noticias-lista.vue');
const NoticiaInfo = () => import('@/pages/public/noticia-info/noticia-info.vue');
const SemillerosLista = () => import('@/pages/public/semilleros-lista/semilleros-lista.vue');
const SemilleroInfo = () => import('@/pages/public/semillero-info/semillero-info.vue');

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
    {
      path: 'profesor/:userLogin',
      name: 'profesor_informacion',
      component: ProfesorInfo,
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
    {
      path: 'noticias-lista',
      name: 'noticias_lista_public',
      component: NoticiasLista,
    },
    {
      path: 'noticia-info/:noticiaId',
      name: 'noticia_info_public',
      component: NoticiaInfo,
    },
    {
      path: 'semilleros-lista',
      name: 'semilleros_lista_public',
      component: SemillerosLista,
    },
    {
      path: 'semilleros-info/:semilleroId',
      name: 'semillero_info_public',
      component: SemilleroInfo,
    },
  ]
}

