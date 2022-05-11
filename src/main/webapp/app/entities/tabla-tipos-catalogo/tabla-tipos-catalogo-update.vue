<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.home.createOrEditLabel"
          data-cy="TablaTiposCatalogoCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.home.createOrEditLabel')"
        >
          Create or edit a TablaTiposCatalogo
        </h2>
        <div>
          <div class="form-group" v-if="tablaTiposCatalogo.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tablaTiposCatalogo.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.nombre')"
              for="tabla-tipos-catalogo-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="tabla-tipos-catalogo-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.tablaTiposCatalogo.nombre.$invalid, invalid: $v.tablaTiposCatalogo.nombre.$invalid }"
              v-model="$v.tablaTiposCatalogo.nombre.$model"
              required
            />
            <div v-if="$v.tablaTiposCatalogo.nombre.$anyDirty && $v.tablaTiposCatalogo.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.tablaTiposCatalogo.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaTiposCatalogo.estado')"
              for="tabla-tipos-catalogo-estado"
              >Estado</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="estado"
              id="tabla-tipos-catalogo-estado"
              data-cy="estado"
              :class="{ valid: !$v.tablaTiposCatalogo.estado.$invalid, invalid: $v.tablaTiposCatalogo.estado.$invalid }"
              v-model="$v.tablaTiposCatalogo.estado.$model"
              required
            />
            <div v-if="$v.tablaTiposCatalogo.estado.$anyDirty && $v.tablaTiposCatalogo.estado.$invalid">
              <small class="form-text text-danger" v-if="!$v.tablaTiposCatalogo.estado.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.tablaTiposCatalogo.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tabla-tipos-catalogo-update.component.ts"></script>
