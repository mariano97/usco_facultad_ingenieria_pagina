<template>
  <div>
    <h2 id="page-heading" data-cy="NoticiaHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.home.title')" id="noticia-heading">Noticias</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'NoticiaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-noticia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.home.createLabel')"> Create a new Noticia </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && noticias && noticias.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.home.notFound')">No noticias found</span>
    </div>
    <div class="table-responsive" v-if="noticias && noticias.length > 0">
      <table class="table table-striped" aria-describedby="noticias">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('titulo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.titulo')">Titulo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'titulo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('sintesis')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.sintesis')">Sintesis</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sintesis'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('cuerpoDescripcion')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.cuerpoDescripcion')">Cuerpo Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cuerpoDescripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fecha')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.fecha')">Fecha</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('urlFoto')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.urlFoto')">Url Foto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'urlFoto'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('facultad.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.facultad')">Facultad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'facultad.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="noticia in noticias" :key="noticia.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'NoticiaView', params: { noticiaId: noticia.id } }">{{ noticia.id }}</router-link>
            </td>
            <td>{{ noticia.titulo }}</td>
            <td>{{ noticia.sintesis }}</td>
            <td>{{ noticia.cuerpoDescripcion }}</td>
            <td>{{ noticia.fecha ? $d(Date.parse(noticia.fecha), 'short') : '' }}</td>
            <td>{{ noticia.urlFoto }}</td>
            <td>
              <div v-if="noticia.facultad">
                <router-link :to="{ name: 'FacultadView', params: { facultadId: noticia.facultad.id } }">{{
                  noticia.facultad.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'NoticiaView', params: { noticiaId: noticia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'NoticiaEdit', params: { noticiaId: noticia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(noticia)"
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
          id="paginaFacultadIngenieriaProyectoApp.noticia.delete.question"
          data-cy="noticiaDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-noticia-heading" v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.delete.question', { id: removeId })">
          Are you sure you want to delete this Noticia?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-noticia"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeNoticia()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="noticias && noticias.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./noticia.component.ts"></script>
