<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.home.createOrEditLabel"
          data-cy="TablaElementoCatalogoCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.home.createOrEditLabel')"
        >
          Create or edit a TablaElementoCatalogo
        </h2>
        <div>
          <div class="form-group" v-if="tablaElementoCatalogo.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tablaElementoCatalogo.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.nombre')"
              for="tabla-elemento-catalogo-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="tabla-elemento-catalogo-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.tablaElementoCatalogo.nombre.$invalid, invalid: $v.tablaElementoCatalogo.nombre.$invalid }"
              v-model="$v.tablaElementoCatalogo.nombre.$model"
              required
            />
            <div v-if="$v.tablaElementoCatalogo.nombre.$anyDirty && $v.tablaElementoCatalogo.nombre.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.tablaElementoCatalogo.nombre.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.abreviatura')"
              for="tabla-elemento-catalogo-abreviatura"
              >Abreviatura</label
            >
            <input
              type="text"
              class="form-control"
              name="abreviatura"
              id="tabla-elemento-catalogo-abreviatura"
              data-cy="abreviatura"
              :class="{ valid: !$v.tablaElementoCatalogo.abreviatura.$invalid, invalid: $v.tablaElementoCatalogo.abreviatura.$invalid }"
              v-model="$v.tablaElementoCatalogo.abreviatura.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.estado')"
              for="tabla-elemento-catalogo-estado"
              >Estado</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="estado"
              id="tabla-elemento-catalogo-estado"
              data-cy="estado"
              :class="{ valid: !$v.tablaElementoCatalogo.estado.$invalid, invalid: $v.tablaElementoCatalogo.estado.$invalid }"
              v-model="$v.tablaElementoCatalogo.estado.$model"
              required
            />
            <div v-if="$v.tablaElementoCatalogo.estado.$anyDirty && $v.tablaElementoCatalogo.estado.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.tablaElementoCatalogo.estado.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tablaElementoCatalogo.tablaTiposCatalogo')"
              for="tabla-elemento-catalogo-tablaTiposCatalogo"
              >Tabla Tipos Catalogo</label
            >
            <select
              class="form-control"
              id="tabla-elemento-catalogo-tablaTiposCatalogo"
              data-cy="tablaTiposCatalogo"
              name="tablaTiposCatalogo"
              v-model="tablaElementoCatalogo.tablaTiposCatalogo"
              required
            >
              <option v-if="!tablaElementoCatalogo.tablaTiposCatalogo" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  tablaElementoCatalogo.tablaTiposCatalogo && tablaTiposCatalogoOption.id === tablaElementoCatalogo.tablaTiposCatalogo.id
                    ? tablaElementoCatalogo.tablaTiposCatalogo
                    : tablaTiposCatalogoOption
                "
                v-for="tablaTiposCatalogoOption in tablaTiposCatalogos"
                :key="tablaTiposCatalogoOption.id"
              >
                {{ tablaTiposCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.tablaElementoCatalogo.tablaTiposCatalogo.$anyDirty && $v.tablaElementoCatalogo.tablaTiposCatalogo.$invalid">
            <small
              class="form-text text-danger"
              v-if="!$v.tablaElementoCatalogo.tablaTiposCatalogo.required"
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
            :disabled="$v.tablaElementoCatalogo.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tabla-elemento-catalogo-update.component.ts"></script>
