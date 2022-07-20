<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.noticia.home.createOrEditLabel"
          data-cy="NoticiaCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.home.createOrEditLabel')"
        >
          Create or edit a Noticia
        </h2>
        <div>
          <div class="form-group" v-if="noticia.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="noticia.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.titulo')" for="noticia-titulo"
              >Titulo</label
            >
            <input
              type="text"
              class="form-control"
              name="titulo"
              id="noticia-titulo"
              data-cy="titulo"
              :class="{ valid: !$v.noticia.titulo.$invalid, invalid: $v.noticia.titulo.$invalid }"
              v-model="$v.noticia.titulo.$model"
              required
            />
            <div v-if="$v.noticia.titulo.$anyDirty && $v.noticia.titulo.$invalid">
              <small class="form-text text-danger" v-if="!$v.noticia.titulo.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.sintesis')" for="noticia-sintesis"
              >Sintesis</label
            >
            <input
              type="text"
              class="form-control"
              name="sintesis"
              id="noticia-sintesis"
              data-cy="sintesis"
              :class="{ valid: !$v.noticia.sintesis.$invalid, invalid: $v.noticia.sintesis.$invalid }"
              v-model="$v.noticia.sintesis.$model"
              required
            />
            <div v-if="$v.noticia.sintesis.$anyDirty && $v.noticia.sintesis.$invalid">
              <small class="form-text text-danger" v-if="!$v.noticia.sintesis.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.cuerpoDescripcion')"
              for="noticia-cuerpoDescripcion"
              >Cuerpo Descripcion</label
            >
            <input
              type="text"
              class="form-control"
              name="cuerpoDescripcion"
              id="noticia-cuerpoDescripcion"
              data-cy="cuerpoDescripcion"
              :class="{ valid: !$v.noticia.cuerpoDescripcion.$invalid, invalid: $v.noticia.cuerpoDescripcion.$invalid }"
              v-model="$v.noticia.cuerpoDescripcion.$model"
              required
            />
            <div v-if="$v.noticia.cuerpoDescripcion.$anyDirty && $v.noticia.cuerpoDescripcion.$invalid">
              <small class="form-text text-danger" v-if="!$v.noticia.cuerpoDescripcion.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.fecha')" for="noticia-fecha"
              >Fecha</label
            >
            <div class="d-flex">
              <input
                id="noticia-fecha"
                data-cy="fecha"
                type="datetime-local"
                class="form-control"
                name="fecha"
                :class="{ valid: !$v.noticia.fecha.$invalid, invalid: $v.noticia.fecha.$invalid }"
                required
                :value="convertDateTimeFromServer($v.noticia.fecha.$model)"
                @change="updateInstantField('fecha', $event)"
              />
            </div>
            <div v-if="$v.noticia.fecha.$anyDirty && $v.noticia.fecha.$invalid">
              <small class="form-text text-danger" v-if="!$v.noticia.fecha.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.noticia.fecha.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.urlFoto')" for="noticia-urlFoto"
              >Url Foto</label
            >
            <input
              type="text"
              class="form-control"
              name="urlFoto"
              id="noticia-urlFoto"
              data-cy="urlFoto"
              :class="{ valid: !$v.noticia.urlFoto.$invalid, invalid: $v.noticia.urlFoto.$invalid }"
              v-model="$v.noticia.urlFoto.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.facultad')" for="noticia-facultad"
              >Facultad</label
            >
            <select class="form-control" id="noticia-facultad" data-cy="facultad" name="facultad" v-model="noticia.facultad" required>
              <option v-if="!noticia.facultad" v-bind:value="null" selected></option>
              <option
                v-bind:value="noticia.facultad && facultadOption.id === noticia.facultad.id ? noticia.facultad : facultadOption"
                v-for="facultadOption in facultads"
                :key="facultadOption.id"
              >
                {{ facultadOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.noticia.facultad.$anyDirty && $v.noticia.facultad.$invalid">
            <small class="form-text text-danger" v-if="!$v.noticia.facultad.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.noticia.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./noticia-update.component.ts"></script>
