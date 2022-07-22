<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="escalafonProfesor">
        <h2 class="jh-entity-heading" data-cy="escalafonProfesorDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.detail.title')">EscalafonProfesor</span>
          {{ escalafonProfesor.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.puntucacionPromedio')">Puntucacion Promedio</span>
          </dt>
          <dd>
            <span>{{ escalafonProfesor.puntucacionPromedio }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.fecha')">Fecha</span>
          </dt>
          <dd>
            <span v-if="escalafonProfesor.fecha">{{ $d(Date.parse(escalafonProfesor.fecha), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.profesor')">Profesor</span>
          </dt>
          <dd>
            <div v-if="escalafonProfesor.profesor">
              <router-link :to="{ name: 'ProfesorView', params: { profesorId: escalafonProfesor.profesor.id } }">{{
                escalafonProfesor.profesor.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="escalafonProfesor.id"
          :to="{ name: 'EscalafonProfesorEdit', params: { escalafonProfesorId: escalafonProfesor.id } }"
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

<script lang="ts" src="./escalafon-profesor-details.component.ts"></script>
