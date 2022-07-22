<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.facultad.home.createOrEditLabel"
          data-cy="FacultadCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.home.createOrEditLabel')"
        >
          Create or edit a Facultad
        </h2>
        <div>
          <div class="form-group" v-if="facultad.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="facultad.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.nombre')" for="facultad-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="facultad-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.facultad.nombre.$invalid, invalid: $v.facultad.nombre.$invalid }"
              v-model="$v.facultad.nombre.$model"
              required
            />
            <div v-if="$v.facultad.nombre.$anyDirty && $v.facultad.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.facultad.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.mision')" for="facultad-mision"
              >Mision</label
            >
            <input
              type="text"
              class="form-control"
              name="mision"
              id="facultad-mision"
              data-cy="mision"
              :class="{ valid: !$v.facultad.mision.$invalid, invalid: $v.facultad.mision.$invalid }"
              v-model="$v.facultad.mision.$model"
              required
            />
            <div v-if="$v.facultad.mision.$anyDirty && $v.facultad.mision.$invalid">
              <small class="form-text text-danger" v-if="!$v.facultad.mision.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.facultad.vision')" for="facultad-vision"
              >Vision</label
            >
            <input
              type="text"
              class="form-control"
              name="vision"
              id="facultad-vision"
              data-cy="vision"
              :class="{ valid: !$v.facultad.vision.$invalid, invalid: $v.facultad.vision.$invalid }"
              v-model="$v.facultad.vision.$model"
              required
            />
            <div v-if="$v.facultad.vision.$anyDirty && $v.facultad.vision.$invalid">
              <small class="form-text text-danger" v-if="!$v.facultad.vision.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.facultad.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./facultad-update.component.ts"></script>
