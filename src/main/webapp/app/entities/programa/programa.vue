<template>
  <div>
    <h2 id="page-heading" data-cy="ProgramaHeading">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.home.title')" id="programa-heading">Programas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProgramaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-programa"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.home.createLabel')"> Create a new Programa </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && programas && programas.length === 0">
      <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.home.notFound')">No programas found</span>
    </div>
    <div class="table-responsive" v-if="programas && programas.length > 0">
      <table class="table table-striped" aria-describedby="programas">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nombre')">Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('codigoSnies')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.codigoSnies')">Codigo Snies</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigoSnies'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('codigoRegistroCalificado')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.codigoRegistroCalificado')">Codigo Registro Calificado</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'codigoRegistroCalificado'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fechaRegistroCalificado')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.fechaRegistroCalificado')">Fecha Registro Calificado</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'fechaRegistroCalificado'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombreTituloOtorgado')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nombreTituloOtorgado')">Nombre Titulo Otorgado</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombreTituloOtorgado'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('numeroCreditos')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.numeroCreditos')">Numero Creditos</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numeroCreditos'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('duracionPrograma')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.duracionPrograma')">Duracion Programa</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'duracionPrograma'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('presentacionPrograma')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.presentacionPrograma')">Presentacion Programa</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'presentacionPrograma'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('mision')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.mision')">Mision</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mision'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('vision')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.vision')">Vision</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'vision'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('perfilEstudiante')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilEstudiante')">Perfil Estudiante</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'perfilEstudiante'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('perfilEgresado')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilEgresado')">Perfil Egresado</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'perfilEgresado'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('perfilOcupacional')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilOcupacional')">Perfil Ocupacional</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'perfilOcupacional'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('urlFotoPrograma')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.urlFotoPrograma')">Url Foto Programa</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'urlFotoPrograma'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('dirigidoAQuien')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.dirigidoAQuien')">Dirigido A Quien</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dirigidoAQuien'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('costoPrograma')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.costoPrograma')">Costo Programa</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'costoPrograma'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('estado')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.estado')">Estado</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estado'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('emailContacto')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.emailContacto')">Email Contacto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'emailContacto'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nivelFormacion.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nivelFormacion')">Nivel Formacion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nivelFormacion.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tipoFormacion.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.tipoFormacion')">Tipo Formacion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipoFormacion.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('facultad.nombre')">
              <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.facultad')">Facultad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'facultad.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="programa in programas" :key="programa.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProgramaView', params: { programaId: programa.id } }">{{ programa.id }}</router-link>
            </td>
            <td>{{ programa.nombre }}</td>
            <td>{{ programa.codigoSnies }}</td>
            <td>{{ programa.codigoRegistroCalificado }}</td>
            <td>{{ programa.fechaRegistroCalificado ? $d(Date.parse(programa.fechaRegistroCalificado), 'short') : '' }}</td>
            <td>{{ programa.nombreTituloOtorgado }}</td>
            <td>{{ programa.numeroCreditos }}</td>
            <td>{{ programa.duracionPrograma }}</td>
            <td>{{ programa.presentacionPrograma }}</td>
            <td>{{ programa.mision }}</td>
            <td>{{ programa.vision }}</td>
            <td>{{ programa.perfilEstudiante }}</td>
            <td>{{ programa.perfilEgresado }}</td>
            <td>{{ programa.perfilOcupacional }}</td>
            <td>{{ programa.urlFotoPrograma }}</td>
            <td>{{ programa.dirigidoAQuien }}</td>
            <td>{{ programa.costoPrograma }}</td>
            <td>{{ programa.estado }}</td>
            <td>{{ programa.emailContacto }}</td>
            <td>
              <div v-if="programa.nivelFormacion">
                <router-link :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: programa.nivelFormacion.id } }">{{
                  programa.nivelFormacion.nombre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="programa.tipoFormacion">
                <router-link :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: programa.tipoFormacion.id } }">{{
                  programa.tipoFormacion.nombre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="programa.facultad">
                <router-link :to="{ name: 'FacultadView', params: { facultadId: programa.facultad.id } }">{{
                  programa.facultad.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProgramaView', params: { programaId: programa.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProgramaEdit', params: { programaId: programa.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(programa)"
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
          id="paginaFacultadIngenieriaProyectoApp.programa.delete.question"
          data-cy="programaDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-programa-heading" v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.delete.question', { id: removeId })">
          Are you sure you want to delete this Programa?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-programa"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePrograma()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./programa.component.ts"></script>
