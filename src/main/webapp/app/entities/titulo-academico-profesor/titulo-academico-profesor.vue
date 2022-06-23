<template>
  <div>
    <h2 id="page-heading" data-cy="TituloAcademicoProfesorHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.home.title')" id="titulo-academico-profesor-heading"
        >Titulo Academico Profesors</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'TituloAcademicoProfesorCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-titulo-academico-profesor"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.home.createLabel')">
              Create a new Titulo Academico Profesor
            </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tituloAcademicoProfesors && tituloAcademicoProfesors.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.home.notFound')"
        >No tituloAcademicoProfesors found</span
      >
    </div>
    <div class="table-responsive" v-if="tituloAcademicoProfesors && tituloAcademicoProfesors.length > 0">
      <table class="table table-striped" aria-describedby="tituloAcademicoProfesors">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombreTitulo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.nombreTitulo')">Nombre Titulo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombreTitulo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombreUniversidadTitulo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.nombreUniversidadTitulo')"
                >Nombre Universidad Titulo</span
              >
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'nombreUniversidadTitulo'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('yearTitulo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.yearTitulo')">Year Titulo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'yearTitulo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tablaElementoCatalogo.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.tablaElementoCatalogo')"
                >Tabla Elemento Catalogo</span
              >
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'tablaElementoCatalogo.nombre'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('profesor.id')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.profesor')">Profesor</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'profesor.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paises.nombrePais')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.paises')">Paises</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paises.nombrePais'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tituloAcademicoProfesor in tituloAcademicoProfesors" :key="tituloAcademicoProfesor.id" data-cy="entityTable">
            <td>
              <router-link
                :to="{ name: 'TituloAcademicoProfesorView', params: { tituloAcademicoProfesorId: tituloAcademicoProfesor.id } }"
                >{{ tituloAcademicoProfesor.id }}</router-link
              >
            </td>
            <td>{{ tituloAcademicoProfesor.nombreTitulo }}</td>
            <td>{{ tituloAcademicoProfesor.nombreUniversidadTitulo }}</td>
            <td>{{ tituloAcademicoProfesor.yearTitulo ? $d(Date.parse(tituloAcademicoProfesor.yearTitulo), 'short') : '' }}</td>
            <td>
              <div v-if="tituloAcademicoProfesor.tablaElementoCatalogo">
                <router-link
                  :to="{
                    name: 'TablaElementoCatalogoView',
                    params: { tablaElementoCatalogoId: tituloAcademicoProfesor.tablaElementoCatalogo.id },
                  }"
                  >{{ tituloAcademicoProfesor.tablaElementoCatalogo.nombre }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="tituloAcademicoProfesor.profesor">
                <router-link :to="{ name: 'ProfesorView', params: { profesorId: tituloAcademicoProfesor.profesor.id } }">{{
                  tituloAcademicoProfesor.profesor.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="tituloAcademicoProfesor.paises">
                <router-link :to="{ name: 'PaisesView', params: { paisesId: tituloAcademicoProfesor.paises.id } }">{{
                  tituloAcademicoProfesor.paises.nombrePais
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'TituloAcademicoProfesorView', params: { tituloAcademicoProfesorId: tituloAcademicoProfesor.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'TituloAcademicoProfesorEdit', params: { tituloAcademicoProfesorId: tituloAcademicoProfesor.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(tituloAcademicoProfesor)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span
          id="paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.delete.question"
          data-cy="tituloAcademicoProfesorDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-tituloAcademicoProfesor-heading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Titulo Academico Profesor?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-tituloAcademicoProfesor"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeTituloAcademicoProfesor()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="tituloAcademicoProfesors && tituloAcademicoProfesors.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./titulo-academico-profesor.component.ts"></script>
