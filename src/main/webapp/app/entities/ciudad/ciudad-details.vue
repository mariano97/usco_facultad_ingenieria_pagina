<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="ciudad">
        <h2 class="jh-entity-heading" data-cy="ciudadDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.detail.title')">Ciudad</span> {{ ciudad.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ ciudad.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.codigo')">Codigo</span>
          </dt>
          <dd>
            <span>{{ ciudad.codigo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.latitud')">Latitud</span>
          </dt>
          <dd>
            <span>{{ ciudad.latitud }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.longitud')">Longitud</span>
          </dt>
          <dd>
            <span>{{ ciudad.longitud }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.estados')">Estados</span>
          </dt>
          <dd>
            <div v-if="ciudad.estados">
              <router-link :to="{ name: 'EstadosView', params: { estadosId: ciudad.estados.id } }">{{ ciudad.estados.nombre }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="ciudad.id" :to="{ name: 'CiudadEdit', params: { ciudadId: ciudad.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ciudad-details.component.ts"></script>
