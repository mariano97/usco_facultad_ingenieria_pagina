<template>
  <div>
    <h2 id="page-heading" data-cy="CiudadHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.home.title')" id="ciudad-heading">Ciudads</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'CiudadCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-ciudad"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.home.createLabel')"> Create a new Ciudad </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && ciudads && ciudads.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.home.notFound')">No ciudads found</span>
    </div>
    <div class="table-responsive" v-if="ciudads && ciudads.length > 0">
      <table class="table table-striped" aria-describedby="ciudads">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('codigo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.codigo')">Codigo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('latitud')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.latitud')">Latitud</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'latitud'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('longitud')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.longitud')">Longitud</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'longitud'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('estados.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.estados')">Estados</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estados.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ciudad in ciudads" :key="ciudad.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CiudadView', params: { ciudadId: ciudad.id } }">{{ ciudad.id }}</router-link>
            </td>
            <td>{{ ciudad.nombre }}</td>
            <td>{{ ciudad.codigo }}</td>
            <td>{{ ciudad.latitud }}</td>
            <td>{{ ciudad.longitud }}</td>
            <td>
              <div v-if="ciudad.estados">
                <router-link :to="{ name: 'EstadosView', params: { estadosId: ciudad.estados.id } }">{{
                  ciudad.estados.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CiudadView', params: { ciudadId: ciudad.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CiudadEdit', params: { ciudadId: ciudad.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(ciudad)"
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
          id="paginaFacultadIngenieriaProyectoApp.ciudad.delete.question"
          data-cy="ciudadDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-ciudad-heading" v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.delete.question', { id: removeId })">
          Are you sure you want to delete this Ciudad?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-ciudad"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeCiudad()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="ciudads && ciudads.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ciudad.component.ts"></script>
