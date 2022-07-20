<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.evento.home.createOrEditLabel"
          data-cy="EventoCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.home.createOrEditLabel')"
        >
          Create or edit a Evento
        </h2>
        <div>
          <div class="form-group" v-if="evento.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="evento.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.titulo')" for="evento-titulo"
              >Titulo</label
            >
            <input
              type="text"
              class="form-control"
              name="titulo"
              id="evento-titulo"
              data-cy="titulo"
              :class="{ valid: !$v.evento.titulo.$invalid, invalid: $v.evento.titulo.$invalid }"
              v-model="$v.evento.titulo.$model"
              required
            />
            <div v-if="$v.evento.titulo.$anyDirty && $v.evento.titulo.$invalid">
              <small class="form-text text-danger" v-if="!$v.evento.titulo.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.cuerpo')" for="evento-cuerpo"
              >Cuerpo</label
            >
            <input
              type="text"
              class="form-control"
              name="cuerpo"
              id="evento-cuerpo"
              data-cy="cuerpo"
              :class="{ valid: !$v.evento.cuerpo.$invalid, invalid: $v.evento.cuerpo.$invalid }"
              v-model="$v.evento.cuerpo.$model"
              required
            />
            <div v-if="$v.evento.cuerpo.$anyDirty && $v.evento.cuerpo.$invalid">
              <small class="form-text text-danger" v-if="!$v.evento.cuerpo.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.fecha')" for="evento-fecha"
              >Fecha</label
            >
            <div class="d-flex">
              <input
                id="evento-fecha"
                data-cy="fecha"
                type="datetime-local"
                class="form-control"
                name="fecha"
                :class="{ valid: !$v.evento.fecha.$invalid, invalid: $v.evento.fecha.$invalid }"
                required
                :value="convertDateTimeFromServer($v.evento.fecha.$model)"
                @change="updateInstantField('fecha', $event)"
              />
            </div>
            <div v-if="$v.evento.fecha.$anyDirty && $v.evento.fecha.$invalid">
              <small class="form-text text-danger" v-if="!$v.evento.fecha.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.evento.fecha.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.hora')" for="evento-hora">Hora</label>
            <div class="d-flex">
              <input
                id="evento-hora"
                data-cy="hora"
                type="datetime-local"
                class="form-control"
                name="hora"
                :class="{ valid: !$v.evento.hora.$invalid, invalid: $v.evento.hora.$invalid }"
                required
                :value="convertDateTimeFromServer($v.evento.hora.$model)"
                @change="updateInstantField('hora', $event)"
              />
            </div>
            <div v-if="$v.evento.hora.$anyDirty && $v.evento.hora.$invalid">
              <small class="form-text text-danger" v-if="!$v.evento.hora.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.evento.hora.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.lugar')" for="evento-lugar"
              >Lugar</label
            >
            <input
              type="text"
              class="form-control"
              name="lugar"
              id="evento-lugar"
              data-cy="lugar"
              :class="{ valid: !$v.evento.lugar.$invalid, invalid: $v.evento.lugar.$invalid }"
              v-model="$v.evento.lugar.$model"
              required
            />
            <div v-if="$v.evento.lugar.$anyDirty && $v.evento.lugar.$invalid">
              <small class="form-text text-danger" v-if="!$v.evento.lugar.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.urlFoto')" for="evento-urlFoto"
              >Url Foto</label
            >
            <input
              type="text"
              class="form-control"
              name="urlFoto"
              id="evento-urlFoto"
              data-cy="urlFoto"
              :class="{ valid: !$v.evento.urlFoto.$invalid, invalid: $v.evento.urlFoto.$invalid }"
              v-model="$v.evento.urlFoto.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.facultad')" for="evento-facultad"
              >Facultad</label
            >
            <select class="form-control" id="evento-facultad" data-cy="facultad" name="facultad" v-model="evento.facultad" required>
              <option v-if="!evento.facultad" v-bind:value="null" selected></option>
              <option
                v-bind:value="evento.facultad && facultadOption.id === evento.facultad.id ? evento.facultad : facultadOption"
                v-for="facultadOption in facultads"
                :key="facultadOption.id"
              >
                {{ facultadOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.evento.facultad.$anyDirty && $v.evento.facultad.$invalid">
            <small class="form-text text-danger" v-if="!$v.evento.facultad.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.evento.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./evento-update.component.ts"></script>
