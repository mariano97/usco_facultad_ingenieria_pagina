<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="laboratorio">
        <h2 class="jh-entity-heading" data-cy="laboratorioDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.detail.title')">Laboratorio</span> {{ laboratorio.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ laboratorio.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.informacionGeneral')">Informacion General</span>
          </dt>
          <dd>
            <span>{{ laboratorio.informacionGeneral }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.urlFoto')">Url Foto</span>
          </dt>
          <dd>
            <span>{{ laboratorio.urlFoto }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.latitud')">Latitud</span>
          </dt>
          <dd>
            <span>{{ laboratorio.latitud }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.longitud')">Longitud</span>
          </dt>
          <dd>
            <span>{{ laboratorio.longitud }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.correoContacto')">Correo Contacto</span>
          </dt>
          <dd>
            <span>{{ laboratorio.correoContacto }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.direccion')">Direccion</span>
          </dt>
          <dd>
            <span>{{ laboratorio.direccion }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="laboratorio.id"
          :to="{ name: 'LaboratorioEdit', params: { laboratorioId: laboratorio.id } }"
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

<script lang="ts" src="./laboratorio-details.component.ts"></script>
