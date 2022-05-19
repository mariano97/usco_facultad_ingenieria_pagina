import { Authority } from '@/shared/security/authority';

const UsuarioSidbarPage = () => import('@/pages/private/usuario-sidbar.vue');
const Inicio = () => import('@/pages/private/inicio/inicio.vue');
const ProgramasLista = () => import('@/pages/private/programas-lista/programas-lista.vue');

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
    }
  ],
};
