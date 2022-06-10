<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.archivosPrograma.home.createOrEditLabel"
          data-cy="ArchivosProgramaCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.home.createOrEditLabel')"
        >
          Create or edit a ArchivosPrograma
        </h2>
        <div>
          <div class="form-group" v-if="archivosPrograma.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="archivosPrograma.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.urlName')"
              for="archivos-programa-urlName"
              >Url Name</label
            >
            <input
              type="text"
              class="form-control"
              name="urlName"
              id="archivos-programa-urlName"
              data-cy="urlName"
              :class="{ valid: !$v.archivosPrograma.urlName.$invalid, invalid: $v.archivosPrograma.urlName.$invalid }"
              v-model="$v.archivosPrograma.urlName.$model"
              required
            />
            <div v-if="$v.archivosPrograma.urlName.$anyDirty && $v.archivosPrograma.urlName.$invalid">
              <small class="form-text text-danger" v-if="!$v.archivosPrograma.urlName.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.generationStorage')"
              for="archivos-programa-generationStorage"
              >Generation Storage</label
            >
            <input
              type="number"
              class="form-control"
              name="generationStorage"
              id="archivos-programa-generationStorage"
              data-cy="generationStorage"
              :class="{ valid: !$v.archivosPrograma.generationStorage.$invalid, invalid: $v.archivosPrograma.generationStorage.$invalid }"
              v-model.number="$v.archivosPrograma.generationStorage.$model"
              required
            />
            <div v-if="$v.archivosPrograma.generationStorage.$anyDirty && $v.archivosPrograma.generationStorage.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.archivosPrograma.generationStorage.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.archivosPrograma.generationStorage.numeric"
                v-text="$t('entity.validation.number')"
              >
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.storageContentType')"
              for="archivos-programa-storageContentType"
              >Storage Content Type</label
            >
            <input
              type="text"
              class="form-control"
              name="storageContentType"
              id="archivos-programa-storageContentType"
              data-cy="storageContentType"
              :class="{ valid: !$v.archivosPrograma.storageContentType.$invalid, invalid: $v.archivosPrograma.storageContentType.$invalid }"
              v-model="$v.archivosPrograma.storageContentType.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.tipoDocumento')"
              for="archivos-programa-tipoDocumento"
              >Tipo Documento</label
            >
            <input
              type="text"
              class="form-control"
              name="tipoDocumento"
              id="archivos-programa-tipoDocumento"
              data-cy="tipoDocumento"
              :class="{ valid: !$v.archivosPrograma.tipoDocumento.$invalid, invalid: $v.archivosPrograma.tipoDocumento.$invalid }"
              v-model="$v.archivosPrograma.tipoDocumento.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.planEstudio')"
              for="archivos-programa-planEstudio"
              >Plan Estudio</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="planEstudio"
              id="archivos-programa-planEstudio"
              data-cy="planEstudio"
              :class="{ valid: !$v.archivosPrograma.planEstudio.$invalid, invalid: $v.archivosPrograma.planEstudio.$invalid }"
              v-model="$v.archivosPrograma.planEstudio.$model"
              required
            />
            <div v-if="$v.archivosPrograma.planEstudio.$anyDirty && $v.archivosPrograma.planEstudio.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.archivosPrograma.planEstudio.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.nombreArchivo')"
              for="archivos-programa-nombreArchivo"
              >Nombre Archivo</label
            >
            <input
              type="text"
              class="form-control"
              name="nombreArchivo"
              id="archivos-programa-nombreArchivo"
              data-cy="nombreArchivo"
              :class="{ valid: !$v.archivosPrograma.nombreArchivo.$invalid, invalid: $v.archivosPrograma.nombreArchivo.$invalid }"
              v-model="$v.archivosPrograma.nombreArchivo.$model"
              required
            />
            <div v-if="$v.archivosPrograma.nombreArchivo.$anyDirty && $v.archivosPrograma.nombreArchivo.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.archivosPrograma.nombreArchivo.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.programa')"
              for="archivos-programa-programa"
              >Programa</label
            >
            <select
              class="form-control"
              id="archivos-programa-programa"
              data-cy="programa"
              name="programa"
              v-model="archivosPrograma.programa"
              required
            >
              <option v-if="!archivosPrograma.programa" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  archivosPrograma.programa && programaOption.id === archivosPrograma.programa.id
                    ? archivosPrograma.programa
                    : programaOption
                "
                v-for="programaOption in programas"
                :key="programaOption.id"
              >
                {{ programaOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.archivosPrograma.programa.$anyDirty && $v.archivosPrograma.programa.$invalid">
            <small class="form-text text-danger" v-if="!$v.archivosPrograma.programa.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.tablaElementoCatalogo')"
              for="archivos-programa-tablaElementoCatalogo"
              >Tabla Elemento Catalogo</label
            >
            <select
              class="form-control"
              id="archivos-programa-tablaElementoCatalogo"
              data-cy="tablaElementoCatalogo"
              name="tablaElementoCatalogo"
              v-model="archivosPrograma.tablaElementoCatalogo"
              required
            >
              <option v-if="!archivosPrograma.tablaElementoCatalogo" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  archivosPrograma.tablaElementoCatalogo && tablaElementoCatalogoOption.id === archivosPrograma.tablaElementoCatalogo.id
                    ? archivosPrograma.tablaElementoCatalogo
                    : tablaElementoCatalogoOption
                "
                v-for="tablaElementoCatalogoOption in tablaElementoCatalogos"
                :key="tablaElementoCatalogoOption.id"
              >
                {{ tablaElementoCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.archivosPrograma.tablaElementoCatalogo.$anyDirty && $v.archivosPrograma.tablaElementoCatalogo.$invalid">
            <small
              class="form-text text-danger"
              v-if="!$v.archivosPrograma.tablaElementoCatalogo.required"
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
            :disabled="$v.archivosPrograma.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./archivos-programa-update.component.ts"></script>
