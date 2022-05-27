import { Authority } from '@/shared/security/authority';

const UsuarioSidbarPage = () => import('@/pages/private/usuario-sidbar.vue');
const Inicio = () => import('@/pages/private/inicio/inicio.vue');
const ProgramasLista = () => import('@/pages/private/programas-lista/programas-lista.vue');
const ProgramaFormulario = () => import('@/pages/private/programa-formulario/programa-formulario.vue');
const SedesLista = () => import('@/pages/private/sedes-lista/sedes-lista.vue');
const SedesFormulario = () => import('@/pages/private/sedes-formulario/sedes-formulario.vue');

export default {
  path: 'ingreso',
  component: UsuarioSidbarPage,
  children: [
    {
      path: 'inicio',
      name: 'inicio_usuario',
      component: Inicio,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'programas-lista',
      name: 'usuario_programas_lista',
      component: ProgramasLista,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'programa-descripcion/:programaId/:accion',
      name: 'usuario_programa_descripcion',
      component: ProgramaFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'programa-descripcion/:accion',
      name: 'usuario_crear_programa',
      component: ProgramaFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'sedes-lista',
      name: 'usuario_sedes_lista',
      component: SedesLista,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'sedes-descripcion/:sedeId/:accion',
      name: 'usuario_sedes_descripcion',
      component: SedesFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'sedes-descripcion/:accion',
      name: 'usuario_crear_sede',
      component: SedesFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
  ],
};
