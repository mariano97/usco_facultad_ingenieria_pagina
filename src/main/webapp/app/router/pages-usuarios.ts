import { Authority } from '@/shared/security/authority';

const UsuarioSidbarPage = () => import('@/pages/private/usuario-sidbar.vue');
const Inicio = () => import('@/pages/private/inicio/inicio.vue');
const ProgramasLista = () => import('@/pages/private/programas-lista/programas-lista.vue');
const ProgramaFormulario = () => import('@/pages/private/programa-formulario/programa-formulario.vue');
const SedesLista = () => import('@/pages/private/sedes-lista/sedes-lista.vue');
const SedesFormulario = () => import('@/pages/private/sedes-formulario/sedes-formulario.vue');
const ProfesoresLista = () => import('@/pages/private/profesores-lista/profesores-lista.vue');
const ProfesorFormulario = () => import('@/pages/private/profesor-formulario/profesor-formulario.vue');
const CursoMateriaLista = () => import('@/pages/private/curso-materias-lista/curso-materias-lista.vue');
const CursosMateriasFormulario = () => import('@/pages/private/curos-materias-formulario/curos-materias-formulario.vue');
const NoticiasFormulario = () => import('@/pages/private/noticias-formulario/noticias-formulario.vue');
const NoticiasLista = () => import('@/pages/private/noticias-lista/noticias-lista.vue');
const EventoFormulario = () => import('@/pages/private/evento-formulario/evento-formulario.vue');
const EventoLista = () => import('@/pages/private/eventos-lista/eventos-lista.vue');
const SemilleroFormulario = () => import('@/pages/private/semillero-formulario/semillero-formulario.vue');
const SemilleroLista = () => import('@/pages/private/semilleros-lista/semilleros-lista.vue');
const LaboratoriosLista = () => import('@/pages/private/laboratorios-lista/laboratorios-lista.vue');
const LaboratorioFormulario = () => import('@/pages/private/laboratorio-formulario/laboratorio-formulario.vue');


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
      path: 'programa-descripcion/:codigoSnies/:accion',
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
    {
      path: 'profesores-lista',
      name: 'usuario_profesores_lista',
      component: ProfesoresLista,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'profesores-descripcion/:userLogin',
      name: 'usuario_profesores_descripcion',
      component: ProfesorFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'profesores-crear',
      name: 'usuario_profesores_crear',
      component: ProfesorFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'materias-lista',
      name: 'curso_materias_lista',
      component: CursoMateriaLista,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'materias-formulario',
      name: 'curso_materias_descripcion',
      component: CursosMateriasFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'materias-formulario/:materiaId',
      name: 'curso_materias_descripcion_updated',
      component: CursosMateriasFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'noticias-lista',
      name: 'noticias_lista',
      component: NoticiasLista,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'noticias-formulario',
      name: 'noticias_formulario',
      component: NoticiasFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'noticias-formulario/:noticiaId',
      name: 'noticias_formulario_descripcion',
      component: NoticiasFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'evento-lista',
      name: 'evento_lista',
      component: EventoLista,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'evento-formulario',
      name: 'evento_formulario',
      component: EventoFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'evento-formulario/:eventoId',
      name: 'evento_formulario_descripcion',
      component: EventoFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'semillero-lista',
      name: 'semillero_lista',
      component: SemilleroLista,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'semillero-formulario',
      name: 'semillero_formulario',
      component: SemilleroFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'semillero-formulario/:semilleroId',
      name: 'semillero_formulario_descripcion',
      component: SemilleroFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'laboratorios-lista',
      name: 'laboratorios_lista',
      component: LaboratoriosLista,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
      alias: '/laboratorios',
    },
    {
      path: 'laboratorios-formulario',
      name: 'laboratorio_formulario',
      component: LaboratorioFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
    {
      path: 'laboratorios-formulario/:laboratorioId',
      name: 'laboratorio_formulario_descripcion',
      component: LaboratorioFormulario,
      meta: { authorities: [Authority.DECANO, Authority.JEFE_PROGRAMA, Authority.PROFESOR] },
    },
  ],
};
