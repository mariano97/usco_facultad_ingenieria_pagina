<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="semillero">
        <h2 class="jh-entity-heading" data-cy="semilleroDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.detail.title')">Semillero</span> {{ semillero.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ semillero.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.informacionGeneral')">Informacion General</span>
          </dt>
          <dd>
            <span>{{ semillero.informacionGeneral }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.urlFoto')">Url Foto</span>
          </dt>
          <dd>
            <span>{{ semillero.urlFoto }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.facultad')">Facultad</span>
          </dt>
          <dd>
            <div v-if="semillero.facultad">
              <router-link :to="{ name: 'FacultadView', params: { facultadId: semillero.facultad.id } }">{{
                semillero.facultad.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.profesor')">Profesor</span>
          </dt>
          <dd>
            <div v-if="semillero.profesor">
              <router-link :to="{ name: 'ProfesorView', params: { profesorId: semillero.profesor.id } }">{{
                semillero.profesor.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="semillero.id"
          :to="{ name: 'SemilleroEdit', params: { semilleroId: semillero.id } }"
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

<script lang="ts" src="./semillero-details.component.ts"></script>
