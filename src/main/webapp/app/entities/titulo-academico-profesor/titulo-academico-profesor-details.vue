<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tituloAcademicoProfesor">
        <h2 class="jh-entity-heading" data-cy="tituloAcademicoProfesorDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.detail.title')">TituloAcademicoProfesor</span>
          {{ tituloAcademicoProfesor.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.nombreTitulo')">Nombre Titulo</span>
          </dt>
          <dd>
            <span>{{ tituloAcademicoProfesor.nombreTitulo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.nombreUniversidadTitulo')"
              >Nombre Universidad Titulo</span
            >
          </dt>
          <dd>
            <span>{{ tituloAcademicoProfesor.nombreUniversidadTitulo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.yearTitulo')">Year Titulo</span>
          </dt>
          <dd>
            <span v-if="tituloAcademicoProfesor.yearTitulo">{{ $d(Date.parse(tituloAcademicoProfesor.yearTitulo), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.tablaElementoCatalogo')"
              >Tabla Elemento Catalogo</span
            >
          </dt>
          <dd>
            <div v-if="tituloAcademicoProfesor.tablaElementoCatalogo">
              <router-link
                :to="{
                  name: 'TablaElementoCatalogoView',
                  params: { tablaElementoCatalogoId: tituloAcademicoProfesor.tablaElementoCatalogo.id },
                }"
                >{{ tituloAcademicoProfesor.tablaElementoCatalogo.nombre }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.profesor')">Profesor</span>
          </dt>
          <dd>
            <div v-if="tituloAcademicoProfesor.profesor">
              <router-link :to="{ name: 'ProfesorView', params: { profesorId: tituloAcademicoProfesor.profesor.id } }">{{
                tituloAcademicoProfesor.profesor.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.paises')">Paises</span>
          </dt>
          <dd>
            <div v-if="tituloAcademicoProfesor.paises">
              <router-link :to="{ name: 'PaisesView', params: { paisesId: tituloAcademicoProfesor.paises.id } }">{{
                tituloAcademicoProfesor.paises.nombrePais
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="tituloAcademicoProfesor.id"
          :to="{ name: 'TituloAcademicoProfesorEdit', params: { tituloAcademicoProfesorId: tituloAcademicoProfesor.id } }"
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

<script lang="ts" src="./titulo-academico-profesor-details.component.ts"></script>
