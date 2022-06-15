<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="estados">
        <h2 class="jh-entity-heading" data-cy="estadosDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.estados.detail.title')">Estados</span> {{ estados.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.estados.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ estados.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.estados.codigo')">Codigo</span>
          </dt>
          <dd>
            <span>{{ estados.codigo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.estados.paises')">Paises</span>
          </dt>
          <dd>
            <div v-if="estados.paises">
              <router-link :to="{ name: 'PaisesView', params: { paisesId: estados.paises.id } }">{{
                estados.paises.nombrePais
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="estados.id" :to="{ name: 'EstadosEdit', params: { estadosId: estados.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./estados-details.component.ts"></script>
