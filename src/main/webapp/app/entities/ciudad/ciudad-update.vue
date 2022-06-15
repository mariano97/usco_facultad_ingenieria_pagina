<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.ciudad.home.createOrEditLabel"
          data-cy="CiudadCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.home.createOrEditLabel')"
        >
          Create or edit a Ciudad
        </h2>
        <div>
          <div class="form-group" v-if="ciudad.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="ciudad.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.nombre')" for="ciudad-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="ciudad-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.ciudad.nombre.$invalid, invalid: $v.ciudad.nombre.$invalid }"
              v-model="$v.ciudad.nombre.$model"
              required
            />
            <div v-if="$v.ciudad.nombre.$anyDirty && $v.ciudad.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.ciudad.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.codigo')" for="ciudad-codigo"
              >Codigo</label
            >
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="ciudad-codigo"
              data-cy="codigo"
              :class="{ valid: !$v.ciudad.codigo.$invalid, invalid: $v.ciudad.codigo.$invalid }"
              v-model="$v.ciudad.codigo.$model"
              required
            />
            <div v-if="$v.ciudad.codigo.$anyDirty && $v.ciudad.codigo.$invalid">
              <small class="form-text text-danger" v-if="!$v.ciudad.codigo.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.latitud')" for="ciudad-latitud"
              >Latitud</label
            >
            <input
              type="number"
              class="form-control"
              name="latitud"
              id="ciudad-latitud"
              data-cy="latitud"
              :class="{ valid: !$v.ciudad.latitud.$invalid, invalid: $v.ciudad.latitud.$invalid }"
              v-model.number="$v.ciudad.latitud.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.longitud')" for="ciudad-longitud"
              >Longitud</label
            >
            <input
              type="number"
              class="form-control"
              name="longitud"
              id="ciudad-longitud"
              data-cy="longitud"
              :class="{ valid: !$v.ciudad.longitud.$invalid, invalid: $v.ciudad.longitud.$invalid }"
              v-model.number="$v.ciudad.longitud.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.ciudad.estados')" for="ciudad-estados"
              >Estados</label
            >
            <select class="form-control" id="ciudad-estados" data-cy="estados" name="estados" v-model="ciudad.estados" required>
              <option v-if="!ciudad.estados" v-bind:value="null" selected></option>
              <option
                v-bind:value="ciudad.estados && estadosOption.id === ciudad.estados.id ? ciudad.estados : estadosOption"
                v-for="estadosOption in estados"
                :key="estadosOption.id"
              >
                {{ estadosOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.ciudad.estados.$anyDirty && $v.ciudad.estados.$invalid">
            <small class="form-text text-danger" v-if="!$v.ciudad.estados.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
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
            :disabled="$v.ciudad.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./ciudad-update.component.ts"></script>
