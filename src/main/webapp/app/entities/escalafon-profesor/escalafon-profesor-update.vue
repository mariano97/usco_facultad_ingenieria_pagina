<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.escalafonProfesor.home.createOrEditLabel"
          data-cy="EscalafonProfesorCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.home.createOrEditLabel')"
        >
          Create or edit a EscalafonProfesor
        </h2>
        <div>
          <div class="form-group" v-if="escalafonProfesor.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="escalafonProfesor.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.puntucacionPromedio')"
              for="escalafon-profesor-puntucacionPromedio"
              >Puntucacion Promedio</label
            >
            <input
              type="number"
              class="form-control"
              name="puntucacionPromedio"
              id="escalafon-profesor-puntucacionPromedio"
              data-cy="puntucacionPromedio"
              :class="{
                valid: !$v.escalafonProfesor.puntucacionPromedio.$invalid,
                invalid: $v.escalafonProfesor.puntucacionPromedio.$invalid,
              }"
              v-model.number="$v.escalafonProfesor.puntucacionPromedio.$model"
              required
            />
            <div v-if="$v.escalafonProfesor.puntucacionPromedio.$anyDirty && $v.escalafonProfesor.puntucacionPromedio.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.escalafonProfesor.puntucacionPromedio.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.escalafonProfesor.puntucacionPromedio.numeric"
                v-text="$t('entity.validation.number')"
              >
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.fecha')"
              for="escalafon-profesor-fecha"
              >Fecha</label
            >
            <div class="d-flex">
              <input
                id="escalafon-profesor-fecha"
                data-cy="fecha"
                type="datetime-local"
                class="form-control"
                name="fecha"
                :class="{ valid: !$v.escalafonProfesor.fecha.$invalid, invalid: $v.escalafonProfesor.fecha.$invalid }"
                required
                :value="convertDateTimeFromServer($v.escalafonProfesor.fecha.$model)"
                @change="updateInstantField('fecha', $event)"
              />
            </div>
            <div v-if="$v.escalafonProfesor.fecha.$anyDirty && $v.escalafonProfesor.fecha.$invalid">
              <small class="form-text text-danger" v-if="!$v.escalafonProfesor.fecha.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.escalafonProfesor.fecha.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.escalafonProfesor.profesor')"
              for="escalafon-profesor-profesor"
              >Profesor</label
            >
            <select
              class="form-control"
              id="escalafon-profesor-profesor"
              data-cy="profesor"
              name="profesor"
              v-model="escalafonProfesor.profesor"
              required
            >
              <option v-if="!escalafonProfesor.profesor" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  escalafonProfesor.profesor && profesorOption.id === escalafonProfesor.profesor.id
                    ? escalafonProfesor.profesor
                    : profesorOption
                "
                v-for="profesorOption in profesors"
                :key="profesorOption.id"
              >
                {{ profesorOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.escalafonProfesor.profesor.$anyDirty && $v.escalafonProfesor.profesor.$invalid">
            <small class="form-text text-danger" v-if="!$v.escalafonProfesor.profesor.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.escalafonProfesor.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./escalafon-profesor-update.component.ts"></script>
