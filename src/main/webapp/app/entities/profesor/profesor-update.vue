<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.profesor.home.createOrEditLabel"
          data-cy="ProfesorCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.home.createOrEditLabel')"
        >
          Create or edit a Profesor
        </h2>
        <div>
          <div class="form-group" v-if="profesor.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="profesor.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.segundoNombre')"
              for="profesor-segundoNombre"
              >Segundo Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="segundoNombre"
              id="profesor-segundoNombre"
              data-cy="segundoNombre"
              :class="{ valid: !$v.profesor.segundoNombre.$invalid, invalid: $v.profesor.segundoNombre.$invalid }"
              v-model="$v.profesor.segundoNombre.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.segundoApellido')"
              for="profesor-segundoApellido"
              >Segundo Apellido</label
            >
            <input
              type="text"
              class="form-control"
              name="segundoApellido"
              id="profesor-segundoApellido"
              data-cy="segundoApellido"
              :class="{ valid: !$v.profesor.segundoApellido.$invalid, invalid: $v.profesor.segundoApellido.$invalid }"
              v-model="$v.profesor.segundoApellido.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.emailAlternativo')"
              for="profesor-emailAlternativo"
              >Email Alternativo</label
            >
            <input
              type="text"
              class="form-control"
              name="emailAlternativo"
              id="profesor-emailAlternativo"
              data-cy="emailAlternativo"
              :class="{ valid: !$v.profesor.emailAlternativo.$invalid, invalid: $v.profesor.emailAlternativo.$invalid }"
              v-model="$v.profesor.emailAlternativo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.activo')" for="profesor-activo"
              >Activo</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="profesor-activo"
              data-cy="activo"
              :class="{ valid: !$v.profesor.activo.$invalid, invalid: $v.profesor.activo.$invalid }"
              v-model="$v.profesor.activo.$model"
              required
            />
            <div v-if="$v.profesor.activo.$anyDirty && $v.profesor.activo.$invalid">
              <small class="form-text text-danger" v-if="!$v.profesor.activo.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.perfil')" for="profesor-perfil"
              >Perfil</label
            >
            <input
              type="text"
              class="form-control"
              name="perfil"
              id="profesor-perfil"
              data-cy="perfil"
              :class="{ valid: !$v.profesor.perfil.$invalid, invalid: $v.profesor.perfil.$invalid }"
              v-model="$v.profesor.perfil.$model"
              required
            />
            <div v-if="$v.profesor.perfil.$anyDirty && $v.profesor.perfil.$invalid">
              <small class="form-text text-danger" v-if="!$v.profesor.perfil.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.telefonoCelular')"
              for="profesor-telefonoCelular"
              >Telefono Celular</label
            >
            <input
              type="text"
              class="form-control"
              name="telefonoCelular"
              id="profesor-telefonoCelular"
              data-cy="telefonoCelular"
              :class="{ valid: !$v.profesor.telefonoCelular.$invalid, invalid: $v.profesor.telefonoCelular.$invalid }"
              v-model="$v.profesor.telefonoCelular.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.oficina')" for="profesor-oficina"
              >Oficina</label
            >
            <input
              type="text"
              class="form-control"
              name="oficina"
              id="profesor-oficina"
              data-cy="oficina"
              :class="{ valid: !$v.profesor.oficina.$invalid, invalid: $v.profesor.oficina.$invalid }"
              v-model="$v.profesor.oficina.$model"
            />
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
            :disabled="$v.profesor.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./profesor-update.component.ts"></script>
