<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.home.createOrEditLabel"
          data-cy="TituloAcademicoProfesorCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.home.createOrEditLabel')"
        >
          Create or edit a TituloAcademicoProfesor
        </h2>
        <div>
          <div class="form-group" v-if="tituloAcademicoProfesor.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tituloAcademicoProfesor.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.nombreTitulo')"
              for="titulo-academico-profesor-nombreTitulo"
              >Nombre Titulo</label
            >
            <input
              type="text"
              class="form-control"
              name="nombreTitulo"
              id="titulo-academico-profesor-nombreTitulo"
              data-cy="nombreTitulo"
              :class="{
                valid: !$v.tituloAcademicoProfesor.nombreTitulo.$invalid,
                invalid: $v.tituloAcademicoProfesor.nombreTitulo.$invalid,
              }"
              v-model="$v.tituloAcademicoProfesor.nombreTitulo.$model"
              required
            />
            <div v-if="$v.tituloAcademicoProfesor.nombreTitulo.$anyDirty && $v.tituloAcademicoProfesor.nombreTitulo.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.tituloAcademicoProfesor.nombreTitulo.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.nombreUniversidadTitulo')"
              for="titulo-academico-profesor-nombreUniversidadTitulo"
              >Nombre Universidad Titulo</label
            >
            <input
              type="text"
              class="form-control"
              name="nombreUniversidadTitulo"
              id="titulo-academico-profesor-nombreUniversidadTitulo"
              data-cy="nombreUniversidadTitulo"
              :class="{
                valid: !$v.tituloAcademicoProfesor.nombreUniversidadTitulo.$invalid,
                invalid: $v.tituloAcademicoProfesor.nombreUniversidadTitulo.$invalid,
              }"
              v-model="$v.tituloAcademicoProfesor.nombreUniversidadTitulo.$model"
              required
            />
            <div
              v-if="
                $v.tituloAcademicoProfesor.nombreUniversidadTitulo.$anyDirty && $v.tituloAcademicoProfesor.nombreUniversidadTitulo.$invalid
              "
            >
              <small
                class="form-text text-danger"
                v-if="!$v.tituloAcademicoProfesor.nombreUniversidadTitulo.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.yearTitulo')"
              for="titulo-academico-profesor-yearTitulo"
              >Year Titulo</label
            >
            <div class="d-flex">
              <input
                id="titulo-academico-profesor-yearTitulo"
                data-cy="yearTitulo"
                type="datetime-local"
                class="form-control"
                name="yearTitulo"
                :class="{ valid: !$v.tituloAcademicoProfesor.yearTitulo.$invalid, invalid: $v.tituloAcademicoProfesor.yearTitulo.$invalid }"
                required
                :value="convertDateTimeFromServer($v.tituloAcademicoProfesor.yearTitulo.$model)"
                @change="updateInstantField('yearTitulo', $event)"
              />
            </div>
            <div v-if="$v.tituloAcademicoProfesor.yearTitulo.$anyDirty && $v.tituloAcademicoProfesor.yearTitulo.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.tituloAcademicoProfesor.yearTitulo.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.tituloAcademicoProfesor.yearTitulo.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.tablaElementoCatalogo')"
              for="titulo-academico-profesor-tablaElementoCatalogo"
              >Tabla Elemento Catalogo</label
            >
            <select
              class="form-control"
              id="titulo-academico-profesor-tablaElementoCatalogo"
              data-cy="tablaElementoCatalogo"
              name="tablaElementoCatalogo"
              v-model="tituloAcademicoProfesor.tablaElementoCatalogo"
              required
            >
              <option v-if="!tituloAcademicoProfesor.tablaElementoCatalogo" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  tituloAcademicoProfesor.tablaElementoCatalogo &&
                  tablaElementoCatalogoOption.id === tituloAcademicoProfesor.tablaElementoCatalogo.id
                    ? tituloAcademicoProfesor.tablaElementoCatalogo
                    : tablaElementoCatalogoOption
                "
                v-for="tablaElementoCatalogoOption in tablaElementoCatalogos"
                :key="tablaElementoCatalogoOption.id"
              >
                {{ tablaElementoCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div
            v-if="$v.tituloAcademicoProfesor.tablaElementoCatalogo.$anyDirty && $v.tituloAcademicoProfesor.tablaElementoCatalogo.$invalid"
          >
            <small
              class="form-text text-danger"
              v-if="!$v.tituloAcademicoProfesor.tablaElementoCatalogo.required"
              v-text="$t('entity.validation.required')"
            >
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.profesor')"
              for="titulo-academico-profesor-profesor"
              >Profesor</label
            >
            <select
              class="form-control"
              id="titulo-academico-profesor-profesor"
              data-cy="profesor"
              name="profesor"
              v-model="tituloAcademicoProfesor.profesor"
              required
            >
              <option v-if="!tituloAcademicoProfesor.profesor" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  tituloAcademicoProfesor.profesor && profesorOption.id === tituloAcademicoProfesor.profesor.id
                    ? tituloAcademicoProfesor.profesor
                    : profesorOption
                "
                v-for="profesorOption in profesors"
                :key="profesorOption.id"
              >
                {{ profesorOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.tituloAcademicoProfesor.profesor.$anyDirty && $v.tituloAcademicoProfesor.profesor.$invalid">
            <small
              class="form-text text-danger"
              v-if="!$v.tituloAcademicoProfesor.profesor.required"
              v-text="$t('entity.validation.required')"
            >
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.tituloAcademicoProfesor.paises')"
              for="titulo-academico-profesor-paises"
              >Paises</label
            >
            <select
              class="form-control"
              id="titulo-academico-profesor-paises"
              data-cy="paises"
              name="paises"
              v-model="tituloAcademicoProfesor.paises"
              required
            >
              <option v-if="!tituloAcademicoProfesor.paises" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  tituloAcademicoProfesor.paises && paisesOption.id === tituloAcademicoProfesor.paises.id
                    ? tituloAcademicoProfesor.paises
                    : paisesOption
                "
                v-for="paisesOption in paises"
                :key="paisesOption.id"
              >
                {{ paisesOption.nombrePais }}
              </option>
            </select>
          </div>
          <div v-if="$v.tituloAcademicoProfesor.paises.$anyDirty && $v.tituloAcademicoProfesor.paises.$invalid">
            <small
              class="form-text text-danger"
              v-if="!$v.tituloAcademicoProfesor.paises.required"
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
            :disabled="$v.tituloAcademicoProfesor.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./titulo-academico-profesor-update.component.ts"></script>
