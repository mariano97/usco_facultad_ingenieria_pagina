<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.laboratorio.home.createOrEditLabel"
          data-cy="LaboratorioCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.home.createOrEditLabel')"
        >
          Create or edit a Laboratorio
        </h2>
        <div>
          <div class="form-group" v-if="laboratorio.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="laboratorio.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.nombre')" for="laboratorio-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="laboratorio-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.laboratorio.nombre.$invalid, invalid: $v.laboratorio.nombre.$invalid }"
              v-model="$v.laboratorio.nombre.$model"
              required
            />
            <div v-if="$v.laboratorio.nombre.$anyDirty && $v.laboratorio.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.laboratorio.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.informacionGeneral')"
              for="laboratorio-informacionGeneral"
              >Informacion General</label
            >
            <input
              type="text"
              class="form-control"
              name="informacionGeneral"
              id="laboratorio-informacionGeneral"
              data-cy="informacionGeneral"
              :class="{ valid: !$v.laboratorio.informacionGeneral.$invalid, invalid: $v.laboratorio.informacionGeneral.$invalid }"
              v-model="$v.laboratorio.informacionGeneral.$model"
              required
            />
            <div v-if="$v.laboratorio.informacionGeneral.$anyDirty && $v.laboratorio.informacionGeneral.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.laboratorio.informacionGeneral.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.urlFoto')"
              for="laboratorio-urlFoto"
              >Url Foto</label
            >
            <input
              type="text"
              class="form-control"
              name="urlFoto"
              id="laboratorio-urlFoto"
              data-cy="urlFoto"
              :class="{ valid: !$v.laboratorio.urlFoto.$invalid, invalid: $v.laboratorio.urlFoto.$invalid }"
              v-model="$v.laboratorio.urlFoto.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.latitud')"
              for="laboratorio-latitud"
              >Latitud</label
            >
            <input
              type="number"
              class="form-control"
              name="latitud"
              id="laboratorio-latitud"
              data-cy="latitud"
              :class="{ valid: !$v.laboratorio.latitud.$invalid, invalid: $v.laboratorio.latitud.$invalid }"
              v-model.number="$v.laboratorio.latitud.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.longitud')"
              for="laboratorio-longitud"
              >Longitud</label
            >
            <input
              type="number"
              class="form-control"
              name="longitud"
              id="laboratorio-longitud"
              data-cy="longitud"
              :class="{ valid: !$v.laboratorio.longitud.$invalid, invalid: $v.laboratorio.longitud.$invalid }"
              v-model.number="$v.laboratorio.longitud.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.correoContacto')"
              for="laboratorio-correoContacto"
              >Correo Contacto</label
            >
            <input
              type="text"
              class="form-control"
              name="correoContacto"
              id="laboratorio-correoContacto"
              data-cy="correoContacto"
              :class="{ valid: !$v.laboratorio.correoContacto.$invalid, invalid: $v.laboratorio.correoContacto.$invalid }"
              v-model="$v.laboratorio.correoContacto.$model"
              required
            />
            <div v-if="$v.laboratorio.correoContacto.$anyDirty && $v.laboratorio.correoContacto.$invalid">
              <small class="form-text text-danger" v-if="!$v.laboratorio.correoContacto.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.direccion')"
              for="laboratorio-direccion"
              >Direccion</label
            >
            <input
              type="text"
              class="form-control"
              name="direccion"
              id="laboratorio-direccion"
              data-cy="direccion"
              :class="{ valid: !$v.laboratorio.direccion.$invalid, invalid: $v.laboratorio.direccion.$invalid }"
              v-model="$v.laboratorio.direccion.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.tipoLaboratorio')"
              for="laboratorio-tipoLaboratorio"
              >Tipo Laboratorio</label
            >
            <select
              class="form-control"
              id="laboratorio-tipoLaboratorio"
              data-cy="tipoLaboratorio"
              name="tipoLaboratorio"
              v-model="laboratorio.tipoLaboratorio"
              required
            >
              <option v-if="!laboratorio.tipoLaboratorio" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  laboratorio.tipoLaboratorio && tablaElementoCatalogoOption.id === laboratorio.tipoLaboratorio.id
                    ? laboratorio.tipoLaboratorio
                    : tablaElementoCatalogoOption
                "
                v-for="tablaElementoCatalogoOption in tablaElementoCatalogos"
                :key="tablaElementoCatalogoOption.id"
              >
                {{ tablaElementoCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.laboratorio.tipoLaboratorio.$anyDirty && $v.laboratorio.tipoLaboratorio.$invalid">
            <small class="form-text text-danger" v-if="!$v.laboratorio.tipoLaboratorio.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.laboratorio.facultad')"
              for="laboratorio-facultad"
              >Facultad</label
            >
            <select
              class="form-control"
              id="laboratorio-facultad"
              data-cy="facultad"
              name="facultad"
              v-model="laboratorio.facultad"
              required
            >
              <option v-if="!laboratorio.facultad" v-bind:value="null" selected></option>
              <option
                v-bind:value="laboratorio.facultad && facultadOption.id === laboratorio.facultad.id ? laboratorio.facultad : facultadOption"
                v-for="facultadOption in facultads"
                :key="facultadOption.id"
              >
                {{ facultadOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.laboratorio.facultad.$anyDirty && $v.laboratorio.facultad.$invalid">
            <small class="form-text text-danger" v-if="!$v.laboratorio.facultad.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.laboratorio.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./laboratorio-update.component.ts"></script>
