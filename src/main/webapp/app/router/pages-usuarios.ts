import { Authority } from '@/shared/security/authority';

const UsuarioSidbarPage = () => import('@/pages/private/usuario-sidbar.vue');
const Inicio = () => import('@/pages/private/inicio/inicio.vue');

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
  ],
};
