<template>
  <div>
    <h2 id="page-heading" data-cy="LaboratorioHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.home.title')" id="laboratorio-heading">Laboratorios</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'LaboratorioCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-laboratorio"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.home.createLabel')"> Create a new Laboratorio </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && laboratorios && laboratorios.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.home.notFound')">No laboratorios found</span>
    </div>
    <div class="table-responsive" v-if="laboratorios && laboratorios.length > 0">
      <table class="table table-striped" aria-describedby="laboratorios">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('informacionGeneral')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.informacionGeneral')">Informacion General</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'informacionGeneral'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('urlFoto')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.urlFoto')">Url Foto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'urlFoto'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('latitud')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.latitud')">Latitud</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'latitud'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('longitud')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.longitud')">Longitud</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'longitud'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('correoContacto')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.correoContacto')">Correo Contacto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'correoContacto'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('direccion')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.direccion')">Direccion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'direccion'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="laboratorio in laboratorios" :key="laboratorio.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'LaboratorioView', params: { laboratorioId: laboratorio.id } }">{{ laboratorio.id }}</router-link>
            </td>
            <td>{{ laboratorio.nombre }}</td>
            <td>{{ laboratorio.informacionGeneral }}</td>
            <td>{{ laboratorio.urlFoto }}</td>
            <td>{{ laboratorio.latitud }}</td>
            <td>{{ laboratorio.longitud }}</td>
            <td>{{ laboratorio.correoContacto }}</td>
            <td>{{ laboratorio.direccion }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'LaboratorioView', params: { laboratorioId: laboratorio.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'LaboratorioEdit', params: { laboratorioId: laboratorio.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(laboratorio)"
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
          id="paginaFacultadIngenieriaProyectoApp.laboratorio.delete.question"
          data-cy="laboratorioDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-laboratorio-heading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Laboratorio?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-laboratorio"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeLaboratorio()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="laboratorios && laboratorios.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./laboratorio.component.ts"></script>
