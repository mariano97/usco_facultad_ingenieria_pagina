<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tablaTiposCatalogo">
        <h2 class="jh-entity-heading" data-cy="tablaTiposCatalogoDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.detail.title')">TablaTiposCatalogo</span>
          {{ tablaTiposCatalogo.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ tablaTiposCatalogo.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.estado')">Estado</span>
          </dt>
          <dd>
            <span>{{ tablaTiposCatalogo.estado }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.keyIdentificador')">Key Identificador</span>
          </dt>
          <dd>
            <span>{{ tablaTiposCatalogo.keyIdentificador }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="tablaTiposCatalogo.id"
          :to="{ name: 'TablaTiposCatalogoEdit', params: { tablaTiposCatalogoId: tablaTiposCatalogo.id } }"
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

<script lang="ts" src="./tabla-tipos-catalogo-details.component.ts"></script>
