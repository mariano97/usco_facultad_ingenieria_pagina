<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.sede.home.createOrEditLabel"
          data-cy="SedeCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.home.createOrEditLabel')"
        >
          Create or edit a Sede
        </h2>
        <div>
          <div class="form-group" v-if="sede.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="sede.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.nombre')" for="sede-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="sede-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.sede.nombre.$invalid, invalid: $v.sede.nombre.$invalid }"
              v-model="$v.sede.nombre.$model"
              required
            />
            <div v-if="$v.sede.nombre.$anyDirty && $v.sede.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.sede.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.latitud')" for="sede-latitud"
              >Latitud</label
            >
            <input
              type="number"
              class="form-control"
              name="latitud"
              id="sede-latitud"
              data-cy="latitud"
              :class="{ valid: !$v.sede.latitud.$invalid, invalid: $v.sede.latitud.$invalid }"
              v-model.number="$v.sede.latitud.$model"
              required
            />
            <div v-if="$v.sede.latitud.$anyDirty && $v.sede.latitud.$invalid">
              <small class="form-text text-danger" v-if="!$v.sede.latitud.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.sede.latitud.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.longitud')" for="sede-longitud"
              >Longitud</label
            >
            <input
              type="number"
              class="form-control"
              name="longitud"
              id="sede-longitud"
              data-cy="longitud"
              :class="{ valid: !$v.sede.longitud.$invalid, invalid: $v.sede.longitud.$invalid }"
              v-model.number="$v.sede.longitud.$model"
              required
            />
            <div v-if="$v.sede.longitud.$anyDirty && $v.sede.longitud.$invalid">
              <small class="form-text text-danger" v-if="!$v.sede.longitud.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.sede.longitud.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.direccion')" for="sede-direccion"
              >Direccion</label
            >
            <input
              type="text"
              class="form-control"
              name="direccion"
              id="sede-direccion"
              data-cy="direccion"
              :class="{ valid: !$v.sede.direccion.$invalid, invalid: $v.sede.direccion.$invalid }"
              v-model="$v.sede.direccion.$model"
              required
            />
            <div v-if="$v.sede.direccion.$anyDirty && $v.sede.direccion.$invalid">
              <small class="form-text text-danger" v-if="!$v.sede.direccion.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.sede.estado')" for="sede-estado"
              >Estado</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="estado"
              id="sede-estado"
              data-cy="estado"
              :class="{ valid: !$v.sede.estado.$invalid, invalid: $v.sede.estado.$invalid }"
              v-model="$v.sede.estado.$model"
              required
            />
            <div v-if="$v.sede.estado.$anyDirty && $v.sede.estado.$invalid">
              <small class="form-text text-danger" v-if="!$v.sede.estado.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.sede.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./sede-update.component.ts"></script>
