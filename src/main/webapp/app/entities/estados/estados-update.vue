<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.estados.home.createOrEditLabel"
          data-cy="EstadosCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.estados.home.createOrEditLabel')"
        >
          Create or edit a Estados
        </h2>
        <div>
          <div class="form-group" v-if="estados.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="estados.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.estados.nombre')" for="estados-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="estados-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.estados.nombre.$invalid, invalid: $v.estados.nombre.$invalid }"
              v-model="$v.estados.nombre.$model"
              required
            />
            <div v-if="$v.estados.nombre.$anyDirty && $v.estados.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.estados.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.estados.codigo')" for="estados-codigo"
              >Codigo</label
            >
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="estados-codigo"
              data-cy="codigo"
              :class="{ valid: !$v.estados.codigo.$invalid, invalid: $v.estados.codigo.$invalid }"
              v-model="$v.estados.codigo.$model"
              required
            />
            <div v-if="$v.estados.codigo.$anyDirty && $v.estados.codigo.$invalid">
              <small class="form-text text-danger" v-if="!$v.estados.codigo.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.estados.paises')" for="estados-paises"
              >Paises</label
            >
            <select class="form-control" id="estados-paises" data-cy="paises" name="paises" v-model="estados.paises" required>
              <option v-if="!estados.paises" v-bind:value="null" selected></option>
              <option
                v-bind:value="estados.paises && paisesOption.id === estados.paises.id ? estados.paises : paisesOption"
                v-for="paisesOption in paises"
                :key="paisesOption.id"
              >
                {{ paisesOption.nombrePais }}
              </option>
            </select>
          </div>
          <div v-if="$v.estados.paises.$anyDirty && $v.estados.paises.$invalid">
            <small class="form-text text-danger" v-if="!$v.estados.paises.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.estados.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./estados-update.component.ts"></script>
