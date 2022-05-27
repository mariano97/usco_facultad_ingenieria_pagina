<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="sede">
        <h2 class="jh-entity-heading" data-cy="sedeDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.detail.title')">Sede</span> {{ sede.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ sede.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.latitud')">Latitud</span>
          </dt>
          <dd>
            <span>{{ sede.latitud }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.longitud')">Longitud</span>
          </dt>
          <dd>
            <span>{{ sede.longitud }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.direccion')">Direccion</span>
          </dt>
          <dd>
            <span>{{ sede.direccion }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.estado')">Estado</span>
          </dt>
          <dd>
            <span>{{ sede.estado }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.telefonoFijo')">Telefono Fijo</span>
          </dt>
          <dd>
            <span>{{ sede.telefonoFijo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.telefonoCelular')">Telefono Celular</span>
          </dt>
          <dd>
            <span>{{ sede.telefonoCelular }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.correoElectronico')">Correo Electronico</span>
          </dt>
          <dd>
            <span>{{ sede.correoElectronico }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.codigoIndicativo')">Codigo Indicativo</span>
          </dt>
          <dd>
            <span>{{ sede.codigoIndicativo }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="sede.id" :to="{ name: 'SedeEdit', params: { sedeId: sede.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./sede-details.component.ts"></script>
