<template>
  <div>
    <h2 id="page-heading" data-cy="SedeHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.home.title')" id="sede-heading">Sedes</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'SedeCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-sede">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.home.createLabel')"> Create a new Sede </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && sedes && sedes.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.home.notFound')">No sedes found</span>
    </div>
    <div class="table-responsive" v-if="sedes && sedes.length > 0">
      <table class="table table-striped" aria-describedby="sedes">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('latitud')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.latitud')">Latitud</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'latitud'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('longitud')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.longitud')">Longitud</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'longitud'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('direccion')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.direccion')">Direccion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'direccion'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('estado')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.estado')">Estado</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estado'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="sede in sedes" :key="sede.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SedeView', params: { sedeId: sede.id } }">{{ sede.id }}</router-link>
            </td>
            <td>{{ sede.nombre }}</td>
            <td>{{ sede.latitud }}</td>
            <td>{{ sede.longitud }}</td>
            <td>{{ sede.direccion }}</td>
            <td>{{ sede.estado }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'SedeView', params: { sedeId: sede.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SedeEdit', params: { sedeId: sede.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(sede)"
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
          id="paginaFacultadIngenieriaProyectoApp.sede.delete.question"
          data-cy="sedeDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-sede-heading" v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.delete.question', { id: removeId })">
          Are you sure you want to delete this Sede?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-sede"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeSede()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="sedes && sedes.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./sede.component.ts"></script>
