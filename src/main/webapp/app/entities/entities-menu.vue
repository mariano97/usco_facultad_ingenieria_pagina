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
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
