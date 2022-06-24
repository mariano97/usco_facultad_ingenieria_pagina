<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.cursoMateria.home.createOrEditLabel"
          data-cy="CursoMateriaCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.home.createOrEditLabel')"
        >
          Create or edit a CursoMateria
        </h2>
        <div>
          <div class="form-group" v-if="cursoMateria.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="cursoMateria.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.nombre')"
              for="curso-materia-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="curso-materia-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.cursoMateria.nombre.$invalid, invalid: $v.cursoMateria.nombre.$invalid }"
              v-model="$v.cursoMateria.nombre.$model"
              required
            />
            <div v-if="$v.cursoMateria.nombre.$anyDirty && $v.cursoMateria.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.cursoMateria.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.numeroCreditos')"
              for="curso-materia-numeroCreditos"
              >Numero Creditos</label
            >
            <input
              type="number"
              class="form-control"
              name="numeroCreditos"
              id="curso-materia-numeroCreditos"
              data-cy="numeroCreditos"
              :class="{ valid: !$v.cursoMateria.numeroCreditos.$invalid, invalid: $v.cursoMateria.numeroCreditos.$invalid }"
              v-model.number="$v.cursoMateria.numeroCreditos.$model"
              required
            />
            <div v-if="$v.cursoMateria.numeroCreditos.$anyDirty && $v.cursoMateria.numeroCreditos.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.cursoMateria.numeroCreditos.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.cursoMateria.numeroCreditos.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.semestreImpartida')"
              for="curso-materia-semestreImpartida"
              >Semestre Impartida</label
            >
            <input
              type="number"
              class="form-control"
              name="semestreImpartida"
              id="curso-materia-semestreImpartida"
              data-cy="semestreImpartida"
              :class="{ valid: !$v.cursoMateria.semestreImpartida.$invalid, invalid: $v.cursoMateria.semestreImpartida.$invalid }"
              v-model.number="$v.cursoMateria.semestreImpartida.$model"
              required
            />
            <div v-if="$v.cursoMateria.semestreImpartida.$anyDirty && $v.cursoMateria.semestreImpartida.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.cursoMateria.semestreImpartida.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.cursoMateria.semestreImpartida.numeric"
                v-text="$t('entity.validation.number')"
              >
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.nivelAcademico')"
              for="curso-materia-nivelAcademico"
              >Nivel Academico</label
            >
            <select
              class="form-control"
              id="curso-materia-nivelAcademico"
              data-cy="nivelAcademico"
              name="nivelAcademico"
              v-model="cursoMateria.nivelAcademico"
              required
            >
              <option v-if="!cursoMateria.nivelAcademico" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  cursoMateria.nivelAcademico && tablaElementoCatalogoOption.id === cursoMateria.nivelAcademico.id
                    ? cursoMateria.nivelAcademico
                    : tablaElementoCatalogoOption
                "
                v-for="tablaElementoCatalogoOption in tablaElementoCatalogos"
                :key="tablaElementoCatalogoOption.id"
              >
                {{ tablaElementoCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.cursoMateria.nivelAcademico.$anyDirty && $v.cursoMateria.nivelAcademico.$invalid">
            <small class="form-text text-danger" v-if="!$v.cursoMateria.nivelAcademico.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.cursoMateria.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./curso-materia-update.component.ts"></script>
