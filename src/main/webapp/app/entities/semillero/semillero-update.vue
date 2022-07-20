<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.semillero.home.createOrEditLabel"
          data-cy="SemilleroCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.home.createOrEditLabel')"
        >
          Create or edit a Semillero
        </h2>
        <div>
          <div class="form-group" v-if="semillero.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="semillero.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.nombre')" for="semillero-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="semillero-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.semillero.nombre.$invalid, invalid: $v.semillero.nombre.$invalid }"
              v-model="$v.semillero.nombre.$model"
              required
            />
            <div v-if="$v.semillero.nombre.$anyDirty && $v.semillero.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.semillero.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.informacionGeneral')"
              for="semillero-informacionGeneral"
              >Informacion General</label
            >
            <input
              type="text"
              class="form-control"
              name="informacionGeneral"
              id="semillero-informacionGeneral"
              data-cy="informacionGeneral"
              :class="{ valid: !$v.semillero.informacionGeneral.$invalid, invalid: $v.semillero.informacionGeneral.$invalid }"
              v-model="$v.semillero.informacionGeneral.$model"
              required
            />
            <div v-if="$v.semillero.informacionGeneral.$anyDirty && $v.semillero.informacionGeneral.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.semillero.informacionGeneral.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.urlFoto')" for="semillero-urlFoto"
              >Url Foto</label
            >
            <input
              type="text"
              class="form-control"
              name="urlFoto"
              id="semillero-urlFoto"
              data-cy="urlFoto"
              :class="{ valid: !$v.semillero.urlFoto.$invalid, invalid: $v.semillero.urlFoto.$invalid }"
              v-model="$v.semillero.urlFoto.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.facultad')" for="semillero-facultad"
              >Facultad</label
            >
            <select class="form-control" id="semillero-facultad" data-cy="facultad" name="facultad" v-model="semillero.facultad" required>
              <option v-if="!semillero.facultad" v-bind:value="null" selected></option>
              <option
                v-bind:value="semillero.facultad && facultadOption.id === semillero.facultad.id ? semillero.facultad : facultadOption"
                v-for="facultadOption in facultads"
                :key="facultadOption.id"
              >
                {{ facultadOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.semillero.facultad.$anyDirty && $v.semillero.facultad.$invalid">
            <small class="form-text text-danger" v-if="!$v.semillero.facultad.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.semillero.profesor')" for="semillero-profesor"
              >Profesor</label
            >
            <select class="form-control" id="semillero-profesor" data-cy="profesor" name="profesor" v-model="semillero.profesor">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="semillero.profesor && profesorOption.id === semillero.profesor.id ? semillero.profesor : profesorOption"
                v-for="profesorOption in profesors"
                :key="profesorOption.id"
              >
                {{ profesorOption.id }}
              </option>
            </select>
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
            :disabled="$v.semillero.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./semillero-update.component.ts"></script>
