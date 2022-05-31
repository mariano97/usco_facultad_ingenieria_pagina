<template>
  <div>
    <h2 id="page-heading" data-cy="ProfesorHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.home.title')" id="profesor-heading">Profesors</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProfesorCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-profesor"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.home.createLabel')"> Create a new Profesor </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && profesors && profesors.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.home.notFound')">No profesors found</span>
    </div>
    <div class="table-responsive" v-if="profesors && profesors.length > 0">
      <table class="table table-striped" aria-describedby="profesors">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('segundoNombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.segundoNombre')">Segundo Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'segundoNombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('segundoApellido')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.segundoApellido')">Segundo Apellido</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'segundoApellido'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('emailAlternativo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.emailAlternativo')">Email Alternativo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'emailAlternativo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('activo')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.activo')">Activo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('perfil')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.perfil')">Perfil</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'perfil'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('telefonoCelular')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.telefonoCelular')">Telefono Celular</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'telefonoCelular'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('oficina')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.oficina')">Oficina</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'oficina'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('userId')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.userId')">User Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'userId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tablaElementoCatalogo.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.tablaElementoCatalogo')">Tabla Elemento Catalogo</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'tablaElementoCatalogo.nombre'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('facultad.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.facultad')">Facultad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'facultad.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="profesor in profesors" :key="profesor.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProfesorView', params: { profesorId: profesor.id } }">{{ profesor.id }}</router-link>
            </td>
            <td>{{ profesor.segundoNombre }}</td>
            <td>{{ profesor.segundoApellido }}</td>
            <td>{{ profesor.emailAlternativo }}</td>
            <td>{{ profesor.activo }}</td>
            <td>{{ profesor.perfil }}</td>
            <td>{{ profesor.telefonoCelular }}</td>
            <td>{{ profesor.oficina }}</td>
            <td>{{ profesor.userId }}</td>
            <td>
              <div v-if="profesor.tablaElementoCatalogo">
                <router-link
                  :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: profesor.tablaElementoCatalogo.id } }"
                  >{{ profesor.tablaElementoCatalogo.nombre }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="profesor.facultad">
                <router-link :to="{ name: 'FacultadView', params: { facultadId: profesor.facultad.id } }">{{
                  profesor.facultad.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProfesorView', params: { profesorId: profesor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProfesorEdit', params: { profesorId: profesor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(profesor)"
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
          id="paginaFacultadIngenieriaProyectoApp.profesor.delete.question"
          data-cy="profesorDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-profesor-heading" v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.delete.question', { id: removeId })">
          Are you sure you want to delete this Profesor?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-profesor"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeProfesor()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="profesors && profesors.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./profesor.component.ts"></script>
