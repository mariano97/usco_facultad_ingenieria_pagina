<template>
  <div>
    <h2 id="page-heading" data-cy="CursoMateriaHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.home.title')" id="curso-materia-heading">Curso Materias</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'CursoMateriaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-curso-materia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.home.createLabel')"> Create a new Curso Materia </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && cursoMaterias && cursoMaterias.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.home.notFound')">No cursoMaterias found</span>
    </div>
    <div class="table-responsive" v-if="cursoMaterias && cursoMaterias.length > 0">
      <table class="table table-striped" aria-describedby="cursoMaterias">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('numeroCreditos')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.numeroCreditos')">Numero Creditos</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numeroCreditos'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('semestreImpartida')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.semestreImpartida')">Semestre Impartida</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'semestreImpartida'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nivelAcademico.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.nivelAcademico')">Nivel Academico</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nivelAcademico.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cursoMateria in cursoMaterias" :key="cursoMateria.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CursoMateriaView', params: { cursoMateriaId: cursoMateria.id } }">{{
                cursoMateria.id
              }}</router-link>
            </td>
            <td>{{ cursoMateria.nombre }}</td>
            <td>{{ cursoMateria.numeroCreditos }}</td>
            <td>{{ cursoMateria.semestreImpartida }}</td>
            <td>
              <div v-if="cursoMateria.nivelAcademico">
                <router-link
                  :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: cursoMateria.nivelAcademico.id } }"
                  >{{ cursoMateria.nivelAcademico.nombre }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CursoMateriaView', params: { cursoMateriaId: cursoMateria.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CursoMateriaEdit', params: { cursoMateriaId: cursoMateria.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(cursoMateria)"
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
          id="paginaFacultadIngenieriaProyectoApp.cursoMateria.delete.question"
          data-cy="cursoMateriaDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-cursoMateria-heading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Curso Materia?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-cursoMateria"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeCursoMateria()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="cursoMaterias && cursoMaterias.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./curso-materia.component.ts"></script>
