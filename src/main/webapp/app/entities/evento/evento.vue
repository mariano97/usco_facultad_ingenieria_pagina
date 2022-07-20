<template>
  <div>
    <h2 id="page-heading" data-cy="EventoHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.home.title')" id="evento-heading">Eventos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'EventoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-evento"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.home.createLabel')"> Create a new Evento </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && eventos && eventos.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.home.notFound')">No eventos found</span>
    </div>
    <div class="table-responsive" v-if="eventos && eventos.length > 0">
      <table class="table table-striped" aria-describedby="eventos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('titulo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.titulo')">Titulo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'titulo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('cuerpo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.cuerpo')">Cuerpo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cuerpo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fecha')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.fecha')">Fecha</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('hora')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.hora')">Hora</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'hora'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('lugar')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.lugar')">Lugar</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lugar'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('urlFoto')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.urlFoto')">Url Foto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'urlFoto'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('facultad.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.facultad')">Facultad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'facultad.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="evento in eventos" :key="evento.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EventoView', params: { eventoId: evento.id } }">{{ evento.id }}</router-link>
            </td>
            <td>{{ evento.titulo }}</td>
            <td>{{ evento.cuerpo }}</td>
            <td>{{ evento.fecha ? $d(Date.parse(evento.fecha), 'short') : '' }}</td>
            <td>{{ evento.hora ? $d(Date.parse(evento.hora), 'short') : '' }}</td>
            <td>{{ evento.lugar }}</td>
            <td>{{ evento.urlFoto }}</td>
            <td>
              <div v-if="evento.facultad">
                <router-link :to="{ name: 'FacultadView', params: { facultadId: evento.facultad.id } }">{{
                  evento.facultad.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'EventoView', params: { eventoId: evento.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EventoEdit', params: { eventoId: evento.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(evento)"
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
          id="paginaFacultadIngenieriaProyectoApp.evento.delete.question"
          data-cy="eventoDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-evento-heading" v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.delete.question', { id: removeId })">
          Are you sure you want to delete this Evento?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-evento"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeEvento()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="eventos && eventos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./evento.component.ts"></script>
