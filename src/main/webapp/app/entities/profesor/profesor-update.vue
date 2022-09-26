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
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.userId')" for="profesor-userId"
              >User Id</label
            >
            <input
              type="number"
              class="form-control"
              name="userId"
              id="profesor-userId"
              data-cy="userId"
              :class="{ valid: !$v.profesor.userId.$invalid, invalid: $v.profesor.userId.$invalid }"
              v-model.number="$v.profesor.userId.$model"
              required
            />
            <div v-if="$v.profesor.userId.$anyDirty && $v.profesor.userId.$invalid">
              <small class="form-text text-danger" v-if="!$v.profesor.userId.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.profesor.userId.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.urlFotoProfesor')"
              for="profesor-urlFotoProfesor"
              >Url Foto Profesor</label
            >
            <input
              type="text"
              class="form-control"
              name="urlFotoProfesor"
              id="profesor-urlFotoProfesor"
              data-cy="urlFotoProfesor"
              :class="{ valid: !$v.profesor.urlFotoProfesor.$invalid, invalid: $v.profesor.urlFotoProfesor.$invalid }"
              v-model="$v.profesor.urlFotoProfesor.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.tituloAcademico')"
              for="profesor-tituloAcademico"
              >Titulo Academico</label
            >
            <input
              type="text"
              class="form-control"
              name="tituloAcademico"
              id="profesor-tituloAcademico"
              data-cy="tituloAcademico"
              :class="{ valid: !$v.profesor.tituloAcademico.$invalid, invalid: $v.profesor.tituloAcademico.$invalid }"
              v-model="$v.profesor.tituloAcademico.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.tablaElementoCatalogo')"
              for="profesor-tablaElementoCatalogo"
              >Tabla Elemento Catalogo</label
            >
            <select
              class="form-control"
              id="profesor-tablaElementoCatalogo"
              data-cy="tablaElementoCatalogo"
              name="tablaElementoCatalogo"
              v-model="profesor.tablaElementoCatalogo"
              required
            >
              <option v-if="!profesor.tablaElementoCatalogo" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  profesor.tablaElementoCatalogo && tablaElementoCatalogoOption.id === profesor.tablaElementoCatalogo.id
                    ? profesor.tablaElementoCatalogo
                    : tablaElementoCatalogoOption
                "
                v-for="tablaElementoCatalogoOption in tablaElementoCatalogos"
                :key="tablaElementoCatalogoOption.id"
              >
                {{ tablaElementoCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.profesor.tablaElementoCatalogo.$anyDirty && $v.profesor.tablaElementoCatalogo.$invalid">
            <small
              class="form-text text-danger"
              v-if="!$v.profesor.tablaElementoCatalogo.required"
              v-text="$t('entity.validation.required')"
            >
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.facultad')" for="profesor-facultad"
              >Facultad</label
            >
            <select class="form-control" id="profesor-facultad" data-cy="facultad" name="facultad" v-model="profesor.facultad" required>
              <option v-if="!profesor.facultad" v-bind:value="null" selected></option>
              <option
                v-bind:value="profesor.facultad && facultadOption.id === profesor.facultad.id ? profesor.facultad : facultadOption"
                v-for="facultadOption in facultads"
                :key="facultadOption.id"
              >
                {{ facultadOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.profesor.facultad.$anyDirty && $v.profesor.facultad.$invalid">
            <small class="form-text text-danger" v-if="!$v.profesor.facultad.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.cursoMateria')" for="profesor-cursoMateria"
              >Curso Materia</label
            >
            <select
              class="form-control"
              id="profesor-cursoMaterias"
              data-cy="cursoMateria"
              multiple
              name="cursoMateria"
              v-if="profesor.cursoMaterias !== undefined"
              v-model="profesor.cursoMaterias"
            >
              <option
                v-bind:value="getSelected(profesor.cursoMaterias, cursoMateriaOption)"
                v-for="cursoMateriaOption in cursoMaterias"
                :key="cursoMateriaOption.id"
              >
                {{ cursoMateriaOption.id }}
              </option>
            </select>
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
