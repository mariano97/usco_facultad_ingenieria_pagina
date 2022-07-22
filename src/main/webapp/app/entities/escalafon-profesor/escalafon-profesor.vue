<template>
  <div>
    <h2 id="page-heading" data-cy="EscalafonProfesorHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.home.title')" id="escalafon-profesor-heading"
        >Escalafon Profesors</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'EscalafonProfesorCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-escalafon-profesor"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.home.createLabel')">
              Create a new Escalafon Profesor
            </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && escalafonProfesors && escalafonProfesors.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.home.notFound')">No escalafonProfesors found</span>
    </div>
    <div class="table-responsive" v-if="escalafonProfesors && escalafonProfesors.length > 0">
      <table class="table table-striped" aria-describedby="escalafonProfesors">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('puntucacionPromedio')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.puntucacionPromedio')">Puntucacion Promedio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'puntucacionPromedio'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fecha')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.fecha')">Fecha</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('profesor.id')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.profesor')">Profesor</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'profesor.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="escalafonProfesor in escalafonProfesors" :key="escalafonProfesor.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EscalafonProfesorView', params: { escalafonProfesorId: escalafonProfesor.id } }">{{
                escalafonProfesor.id
              }}</router-link>
            </td>
            <td>{{ escalafonProfesor.puntucacionPromedio }}</td>
            <td>{{ escalafonProfesor.fecha ? $d(Date.parse(escalafonProfesor.fecha), 'short') : '' }}</td>
            <td>
              <div v-if="escalafonProfesor.profesor">
                <router-link :to="{ name: 'ProfesorView', params: { profesorId: escalafonProfesor.profesor.id } }">{{
                  escalafonProfesor.profesor.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'EscalafonProfesorView', params: { escalafonProfesorId: escalafonProfesor.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'EscalafonProfesorEdit', params: { escalafonProfesorId: escalafonProfesor.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(escalafonProfesor)"
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
          id="paginaFacultadIngenieriaProyectoApp.escalafonProfesor.delete.question"
          data-cy="escalafonProfesorDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-escalafonProfesor-heading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Escalafon Profesor?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-escalafonProfesor"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeEscalafonProfesor()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="escalafonProfesors && escalafonProfesors.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./escalafon-profesor.component.ts"></script>
