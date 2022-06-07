<template>
  <div>
    <h2 id="page-heading" data-cy="ArchivosProgramaHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.home.title')" id="archivos-programa-heading"
        >Archivos Programas</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ArchivosProgramaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-archivos-programa"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.home.createLabel')">
              Create a new Archivos Programa
            </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && archivosProgramas && archivosProgramas.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.home.notFound')">No archivosProgramas found</span>
    </div>
    <div class="table-responsive" v-if="archivosProgramas && archivosProgramas.length > 0">
      <table class="table table-striped" aria-describedby="archivosProgramas">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('urlName')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.urlName')">Url Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'urlName'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('generationStorage')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.generationStorage')">Generation Storage</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'generationStorage'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('storageContentType')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.storageContentType')">Storage Content Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'storageContentType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tipoDocumento')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.tipoDocumento')">Tipo Documento</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipoDocumento'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('planEstudio')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.planEstudio')">Plan Estudio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'planEstudio'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('programa.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.programa')">Programa</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'programa.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tablaElementoCatalogo.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.tablaElementoCatalogo')">Tabla Elemento Catalogo</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'tablaElementoCatalogo.nombre'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="archivosPrograma in archivosProgramas" :key="archivosPrograma.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ArchivosProgramaView', params: { archivosProgramaId: archivosPrograma.id } }">{{
                archivosPrograma.id
              }}</router-link>
            </td>
            <td>{{ archivosPrograma.urlName }}</td>
            <td>{{ archivosPrograma.generationStorage }}</td>
            <td>{{ archivosPrograma.storageContentType }}</td>
            <td>{{ archivosPrograma.tipoDocumento }}</td>
            <td>{{ archivosPrograma.planEstudio }}</td>
            <td>
              <div v-if="archivosPrograma.programa">
                <router-link :to="{ name: 'ProgramaView', params: { programaId: archivosPrograma.programa.id } }">{{
                  archivosPrograma.programa.nombre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="archivosPrograma.tablaElementoCatalogo">
                <router-link
                  :to="{
                    name: 'TablaElementoCatalogoView',
                    params: { tablaElementoCatalogoId: archivosPrograma.tablaElementoCatalogo.id },
                  }"
                  >{{ archivosPrograma.tablaElementoCatalogo.nombre }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ArchivosProgramaView', params: { archivosProgramaId: archivosPrograma.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ArchivosProgramaEdit', params: { archivosProgramaId: archivosPrograma.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(archivosPrograma)"
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
        <infinite-loading
          ref="infiniteLoading"
          v-if="totalItems > itemsPerPage"
          :identifier="infiniteId"
          slot="append"
          @infinite="loadMore"
          force-use-infinite-wrapper=".el-table__body-wrapper"
          :distance="20"
        >
        </infinite-loading>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span
          id="paginaFacultadIngenieriaProyectoApp.archivosPrograma.delete.question"
          data-cy="archivosProgramaDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-archivosPrograma-heading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Archivos Programa?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-archivosPrograma"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeArchivosPrograma()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./archivos-programa.component.ts"></script>
