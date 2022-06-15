<template>
  <div>
    <h2 id="page-heading" data-cy="PaisesHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.home.title')" id="paises-heading">Paises</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PaisesCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-paises"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.home.createLabel')"> Create a new Paises </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && paises && paises.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.home.notFound')">No paises found</span>
    </div>
    <div class="table-responsive" v-if="paises && paises.length > 0">
      <table class="table table-striped" aria-describedby="paises">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombrePais')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.nombrePais')">Nombre Pais</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombrePais'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('codigoPais')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.codigoPais')">Codigo Pais</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigoPais'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paises in paises" :key="paises.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PaisesView', params: { paisesId: paises.id } }">{{ paises.id }}</router-link>
            </td>
            <td>{{ paises.nombrePais }}</td>
            <td>{{ paises.codigoPais }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PaisesView', params: { paisesId: paises.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PaisesEdit', params: { paisesId: paises.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(paises)"
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
          id="paginaFacultadIngenieriaProyectoApp.paises.delete.question"
          data-cy="paisesDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-paises-heading" v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.delete.question', { id: removeId })">
          Are you sure you want to delete this Paises?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-paises"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePaises()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./paises.component.ts"></script>
