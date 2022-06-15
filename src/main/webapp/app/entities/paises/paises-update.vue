<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.paises.home.createOrEditLabel"
          data-cy="PaisesCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.home.createOrEditLabel')"
        >
          Create or edit a Paises
        </h2>
        <div>
          <div class="form-group" v-if="paises.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="paises.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.nombrePais')" for="paises-nombrePais"
              >Nombre Pais</label
            >
            <input
              type="text"
              class="form-control"
              name="nombrePais"
              id="paises-nombrePais"
              data-cy="nombrePais"
              :class="{ valid: !$v.paises.nombrePais.$invalid, invalid: $v.paises.nombrePais.$invalid }"
              v-model="$v.paises.nombrePais.$model"
              required
            />
            <div v-if="$v.paises.nombrePais.$anyDirty && $v.paises.nombrePais.$invalid">
              <small class="form-text text-danger" v-if="!$v.paises.nombrePais.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.paises.codigoPais')" for="paises-codigoPais"
              >Codigo Pais</label
            >
            <input
              type="text"
              class="form-control"
              name="codigoPais"
              id="paises-codigoPais"
              data-cy="codigoPais"
              :class="{ valid: !$v.paises.codigoPais.$invalid, invalid: $v.paises.codigoPais.$invalid }"
              v-model="$v.paises.codigoPais.$model"
              required
            />
            <div v-if="$v.paises.codigoPais.$anyDirty && $v.paises.codigoPais.$invalid">
              <small class="form-text text-danger" v-if="!$v.paises.codigoPais.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.paises.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./paises-update.component.ts"></script>
