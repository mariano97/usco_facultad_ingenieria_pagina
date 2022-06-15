<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paises">
        <h2 class="jh-entity-heading" data-cy="paisesDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.detail.title')">Paises</span> {{ paises.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.nombrePais')">Nombre Pais</span>
          </dt>
          <dd>
            <span>{{ paises.nombrePais }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.codigoPais')">Codigo Pais</span>
          </dt>
          <dd>
            <span>{{ paises.codigoPais }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="paises.id" :to="{ name: 'PaisesEdit', params: { paisesId: paises.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./paises-details.component.ts"></script>
