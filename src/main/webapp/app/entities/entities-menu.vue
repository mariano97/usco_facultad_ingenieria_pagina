<template>
  <div>
    <b-dropdown-item to="/facultad">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.facultad')">Facultad</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tabla-tipos-catalogo">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.tablaTiposCatalogo')">Tabla Tipos Catalogo</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tabla-elemento-catalogo">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.tablaElementoCatalogo')">Tabla Elemento Catalogo</span>
    </b-dropdown-item>
    <b-dropdown-item to="/programa">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.programa')">Programa</span>
    </b-dropdown-item>
    <b-dropdown-item to="/sede">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.sede')">Sede</span>
    </b-dropdown-item>
    <b-dropdown-item to="/profesor">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.profesor')">Profesor</span>
    </b-dropdown-item>
    <b-dropdown-item to="/archivos-programa">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.archivosPrograma')">Archivos Programa</span>
    </b-dropdown-item>
    <b-dropdown-item to="/redes-programa">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.redesPrograma')">Redes Programa</span>
    </b-dropdown-item>
    <b-dropdown-item to="/paises">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.paises')">Paises</span>
    </b-dropdown-item>
    <b-dropdown-item to="/estados">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.estados')">Estados</span>
    </b-dropdown-item>
    <b-dropdown-item to="/ciudad">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.ciudad')">Ciudad</span>
    </b-dropdown-item>
    <b-dropdown-item to="/titulo-academico-profesor">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.tituloAcademicoProfesor')">Titulo Academico Profesor</span>
    </b-dropdown-item>
    <b-dropdown-item to="/curso-materia">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.cursoMateria')">Curso Materia</span>
    </b-dropdown-item>
    <b-dropdown-item to="/noticia">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.noticia')">Noticia</span>
    </b-dropdown-item>
    <b-dropdown-item to="/evento">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.evento')">Evento</span>
    </b-dropdown-item>
    <b-dropdown-item to="/semillero">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.semillero')">Semillero</span>
    </b-dropdown-item>
    <b-dropdown-item to="/escalafon-profesor">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.escalafonProfesor')">Escalafon Profesor</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
