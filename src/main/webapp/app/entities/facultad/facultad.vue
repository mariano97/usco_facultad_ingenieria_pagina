<template>
  <div>
    <h2 id="page-heading" data-cy="FacultadHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.home.title')" id="facultad-heading">Facultads</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'FacultadCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-facultad"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.home.createLabel')"> Create a new Facultad </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && facultads && facultads.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.home.notFound')">No facultads found</span>
    </div>
    <div class="table-responsive" v-if="facultads && facultads.length > 0">
      <table class="table table-striped" aria-describedby="facultads">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('mision')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.mision')">Mision</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mision'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('vision')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.vision')">Vision</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'vision'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="facultad in facultads" :key="facultad.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FacultadView', params: { facultadId: facultad.id } }">{{ facultad.id }}</router-link>
            </td>
            <td>{{ facultad.nombre }}</td>
            <td>{{ facultad.mision }}</td>
            <td>{{ facultad.vision }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FacultadView', params: { facultadId: facultad.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FacultadEdit', params: { facultadId: facultad.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(facultad)"
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
          id="paginaFacultadIngenieriaProyectoApp.facultad.delete.question"
          data-cy="facultadDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-facultad-heading" v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.delete.question', { id: removeId })">
          Are you sure you want to delete this Facultad?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-facultad"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeFacultad()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="facultads && facultads.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./facultad.component.ts"></script>
