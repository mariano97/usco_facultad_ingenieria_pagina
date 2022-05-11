<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tablaElementoCatalogo">
        <h2 class="jh-entity-heading" data-cy="tablaElementoCatalogoDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.detail.title')">TablaElementoCatalogo</span>
          {{ tablaElementoCatalogo.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ tablaElementoCatalogo.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.abreviatura')">Abreviatura</span>
          </dt>
          <dd>
            <span>{{ tablaElementoCatalogo.abreviatura }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.estado')">Estado</span>
          </dt>
          <dd>
            <span>{{ tablaElementoCatalogo.estado }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.tablaTiposCatalogo')">Tabla Tipos Catalogo</span>
          </dt>
          <dd>
            <div v-if="tablaElementoCatalogo.tablaTiposCatalogo">
              <router-link
                :to="{ name: 'TablaTiposCatalogoView', params: { tablaTiposCatalogoId: tablaElementoCatalogo.tablaTiposCatalogo.id } }"
                >{{ tablaElementoCatalogo.tablaTiposCatalogo.nombre }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="tablaElementoCatalogo.id"
          :to="{ name: 'TablaElementoCatalogoEdit', params: { tablaElementoCatalogoId: tablaElementoCatalogo.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tabla-elemento-catalogo-details.component.ts"></script>
