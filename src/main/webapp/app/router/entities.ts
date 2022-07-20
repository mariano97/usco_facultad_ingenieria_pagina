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
// prettier-ignore
const ArchivosPrograma = () => import('@/entities/archivos-programa/archivos-programa.vue');
// prettier-ignore
const ArchivosProgramaUpdate = () => import('@/entities/archivos-programa/archivos-programa-update.vue');
// prettier-ignore
const ArchivosProgramaDetails = () => import('@/entities/archivos-programa/archivos-programa-details.vue');
// prettier-ignore
const RedesPrograma = () => import('@/entities/redes-programa/redes-programa.vue');
// prettier-ignore
const RedesProgramaUpdate = () => import('@/entities/redes-programa/redes-programa-update.vue');
// prettier-ignore
const RedesProgramaDetails = () => import('@/entities/redes-programa/redes-programa-details.vue');
// prettier-ignore
const Paises = () => import('@/entities/paises/paises.vue');
// prettier-ignore
const PaisesUpdate = () => import('@/entities/paises/paises-update.vue');
// prettier-ignore
const PaisesDetails = () => import('@/entities/paises/paises-details.vue');
// prettier-ignore
const Estados = () => import('@/entities/estados/estados.vue');
// prettier-ignore
const EstadosUpdate = () => import('@/entities/estados/estados-update.vue');
// prettier-ignore
const EstadosDetails = () => import('@/entities/estados/estados-details.vue');
// prettier-ignore
const Ciudad = () => import('@/entities/ciudad/ciudad.vue');
// prettier-ignore
const CiudadUpdate = () => import('@/entities/ciudad/ciudad-update.vue');
// prettier-ignore
const CiudadDetails = () => import('@/entities/ciudad/ciudad-details.vue');
// prettier-ignore
const TituloAcademicoProfesor = () => import('@/entities/titulo-academico-profesor/titulo-academico-profesor.vue');
// prettier-ignore
const TituloAcademicoProfesorUpdate = () => import('@/entities/titulo-academico-profesor/titulo-academico-profesor-update.vue');
// prettier-ignore
const TituloAcademicoProfesorDetails = () => import('@/entities/titulo-academico-profesor/titulo-academico-profesor-details.vue');
// prettier-ignore
const CursoMateria = () => import('@/entities/curso-materia/curso-materia.vue');
// prettier-ignore
const CursoMateriaUpdate = () => import('@/entities/curso-materia/curso-materia-update.vue');
// prettier-ignore
const CursoMateriaDetails = () => import('@/entities/curso-materia/curso-materia-details.vue');
// prettier-ignore
const Noticia = () => import('@/entities/noticia/noticia.vue');
// prettier-ignore
const NoticiaUpdate = () => import('@/entities/noticia/noticia-update.vue');
// prettier-ignore
const NoticiaDetails = () => import('@/entities/noticia/noticia-details.vue');
// prettier-ignore
const Evento = () => import('@/entities/evento/evento.vue');
// prettier-ignore
const EventoUpdate = () => import('@/entities/evento/evento-update.vue');
// prettier-ignore
const EventoDetails = () => import('@/entities/evento/evento-details.vue');
// prettier-ignore
const Semillero = () => import('@/entities/semillero/semillero.vue');
// prettier-ignore
const SemilleroUpdate = () => import('@/entities/semillero/semillero-update.vue');
// prettier-ignore
const SemilleroDetails = () => import('@/entities/semillero/semillero-details.vue');
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
    {
      path: 'archivos-programa',
      name: 'ArchivosPrograma',
      component: ArchivosPrograma,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'archivos-programa/new',
      name: 'ArchivosProgramaCreate',
      component: ArchivosProgramaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'archivos-programa/:archivosProgramaId/edit',
      name: 'ArchivosProgramaEdit',
      component: ArchivosProgramaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'archivos-programa/:archivosProgramaId/view',
      name: 'ArchivosProgramaView',
      component: ArchivosProgramaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'redes-programa',
      name: 'RedesPrograma',
      component: RedesPrograma,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'redes-programa/new',
      name: 'RedesProgramaCreate',
      component: RedesProgramaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'redes-programa/:redesProgramaId/edit',
      name: 'RedesProgramaEdit',
      component: RedesProgramaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'redes-programa/:redesProgramaId/view',
      name: 'RedesProgramaView',
      component: RedesProgramaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paises',
      name: 'Paises',
      component: Paises,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paises/new',
      name: 'PaisesCreate',
      component: PaisesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paises/:paisesId/edit',
      name: 'PaisesEdit',
      component: PaisesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paises/:paisesId/view',
      name: 'PaisesView',
      component: PaisesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estados',
      name: 'Estados',
      component: Estados,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estados/new',
      name: 'EstadosCreate',
      component: EstadosUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estados/:estadosId/edit',
      name: 'EstadosEdit',
      component: EstadosUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estados/:estadosId/view',
      name: 'EstadosView',
      component: EstadosDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ciudad',
      name: 'Ciudad',
      component: Ciudad,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ciudad/new',
      name: 'CiudadCreate',
      component: CiudadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ciudad/:ciudadId/edit',
      name: 'CiudadEdit',
      component: CiudadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ciudad/:ciudadId/view',
      name: 'CiudadView',
      component: CiudadDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'titulo-academico-profesor',
      name: 'TituloAcademicoProfesor',
      component: TituloAcademicoProfesor,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'titulo-academico-profesor/new',
      name: 'TituloAcademicoProfesorCreate',
      component: TituloAcademicoProfesorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'titulo-academico-profesor/:tituloAcademicoProfesorId/edit',
      name: 'TituloAcademicoProfesorEdit',
      component: TituloAcademicoProfesorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'titulo-academico-profesor/:tituloAcademicoProfesorId/view',
      name: 'TituloAcademicoProfesorView',
      component: TituloAcademicoProfesorDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'curso-materia',
      name: 'CursoMateria',
      component: CursoMateria,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'curso-materia/new',
      name: 'CursoMateriaCreate',
      component: CursoMateriaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'curso-materia/:cursoMateriaId/edit',
      name: 'CursoMateriaEdit',
      component: CursoMateriaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'curso-materia/:cursoMateriaId/view',
      name: 'CursoMateriaView',
      component: CursoMateriaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'noticia',
      name: 'Noticia',
      component: Noticia,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'noticia/new',
      name: 'NoticiaCreate',
      component: NoticiaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'noticia/:noticiaId/edit',
      name: 'NoticiaEdit',
      component: NoticiaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'noticia/:noticiaId/view',
      name: 'NoticiaView',
      component: NoticiaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'evento',
      name: 'Evento',
      component: Evento,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'evento/new',
      name: 'EventoCreate',
      component: EventoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'evento/:eventoId/edit',
      name: 'EventoEdit',
      component: EventoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'evento/:eventoId/view',
      name: 'EventoView',
      component: EventoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'semillero',
      name: 'Semillero',
      component: Semillero,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'semillero/new',
      name: 'SemilleroCreate',
      component: SemilleroUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'semillero/:semilleroId/edit',
      name: 'SemilleroEdit',
      component: SemilleroUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'semillero/:semilleroId/view',
      name: 'SemilleroView',
      component: SemilleroDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
