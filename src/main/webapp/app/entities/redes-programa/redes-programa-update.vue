<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.redesPrograma.home.createOrEditLabel"
          data-cy="RedesProgramaCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.home.createOrEditLabel')"
        >
          Create or edit a RedesPrograma
        </h2>
        <div>
          <div class="form-group" v-if="redesPrograma.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="redesPrograma.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.urlRedSocial')"
              for="redes-programa-urlRedSocial"
              >Url Red Social</label
            >
            <input
              type="text"
              class="form-control"
              name="urlRedSocial"
              id="redes-programa-urlRedSocial"
              data-cy="urlRedSocial"
              :class="{ valid: !$v.redesPrograma.urlRedSocial.$invalid, invalid: $v.redesPrograma.urlRedSocial.$invalid }"
              v-model="$v.redesPrograma.urlRedSocial.$model"
              required
            />
            <div v-if="$v.redesPrograma.urlRedSocial.$anyDirty && $v.redesPrograma.urlRedSocial.$invalid">
              <small class="form-text text-danger" v-if="!$v.redesPrograma.urlRedSocial.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.programa')"
              for="redes-programa-programa"
              >Programa</label
            >
            <select
              class="form-control"
              id="redes-programa-programa"
              data-cy="programa"
              name="programa"
              v-model="redesPrograma.programa"
              required
            >
              <option v-if="!redesPrograma.programa" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  redesPrograma.programa && programaOption.id === redesPrograma.programa.id ? redesPrograma.programa : programaOption
                "
                v-for="programaOption in programas"
                :key="programaOption.id"
              >
                {{ programaOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.redesPrograma.programa.$anyDirty && $v.redesPrograma.programa.$invalid">
            <small class="form-text text-danger" v-if="!$v.redesPrograma.programa.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.tablaElementoCatalogo')"
              for="redes-programa-tablaElementoCatalogo"
              >Tabla Elemento Catalogo</label
            >
            <select
              class="form-control"
              id="redes-programa-tablaElementoCatalogo"
              data-cy="tablaElementoCatalogo"
              name="tablaElementoCatalogo"
              v-model="redesPrograma.tablaElementoCatalogo"
              required
            >
              <option v-if="!redesPrograma.tablaElementoCatalogo" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  redesPrograma.tablaElementoCatalogo && tablaElementoCatalogoOption.id === redesPrograma.tablaElementoCatalogo.id
                    ? redesPrograma.tablaElementoCatalogo
                    : tablaElementoCatalogoOption
                "
                v-for="tablaElementoCatalogoOption in tablaElementoCatalogos"
                :key="tablaElementoCatalogoOption.id"
              >
                {{ tablaElementoCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.redesPrograma.tablaElementoCatalogo.$anyDirty && $v.redesPrograma.tablaElementoCatalogo.$invalid">
            <small
              class="form-text text-danger"
              v-if="!$v.redesPrograma.tablaElementoCatalogo.required"
              v-text="$t('entity.validation.required')"
            >
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
            :disabled="$v.redesPrograma.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./redes-programa-update.component.ts"></script>
