<template>
  <div class="container_formulario_semillero">
    <section class="header_arrow d-flex justify-content-between">
      <div class="d-flex justify-content-start">
        <div class="row mx-0">
          <div class="col-sm-auto px-0 d-flex align-items-center">
            <router-link :to="{ name: 'semillero_lista' }" class="">
              <img alt="retornar" src="/content/images/iconos/left-arrow-red.png" />
            </router-link>
          </div>
          <div class="col d-flex align-items-center">
            <h2 class="title_formulario_return">Semillero</h2>
          </div>
        </div>
      </div>
      <div class="col-sm-auto row mx-0">
        <button v-if="isModeEdit && !enableEdit"
          class="btn btn_editar d-flex align-items-center justify-content-center col-sm-auto" type="button"
          id="btn_editar" v-text="$t('entity.action.edit')" v-on:click="enableFormularioEditar()">
          Editar
        </button>
        <button
          v-if="isModeEdit && !enableEdit"
          class="btn btn_eliminar d-flex align-items-center justify-content-center col-sm-auto px-0"
          type="button"
          id="btn_eliminar"
          v-text="$t('entity.action.delete')"
          v-on:click="eliminar()"
        >
          Eliminar
        </button>
      </div>
    </section>
    <section class="semillero_formulario">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="guardar()">
        <div class="">
          <div class="container_upload_image d-flex align-items-end" :class="{ 'flex-column': showImage }">
            <img v-if="showImage" class="image_noticia" alt="imagen_programa"
              :src="imageProfileProfesor | headerFileBase64" />
            <a id="hide" type="button" class="btn btn-upload d-flex align-items-center justify-content-center">
              <label for="file-input" class="row mx-0 align-items-center justify-content-center">
                <div class="col-sm-auto">
                  <img class="image" alt="image-upload" src="/content/images/iconos/icon-upload.png" />
                </div>
                <div class="">
                  <h4>Subir imagen</h4>
                </div>
              </label>
              <input id="file-input" type="file" accept="image/png, image/jpeg, image/jpg" @change="changeImage" />
            </a>
          </div>
          <div class="">
            <div class="row mx-0">
              <div class="col form-group px-0">
                <label class="form-control-label campo_requerido" for="nombre">Nombre</label>
                <input type="text" id="nombre" name="nombre" class="form-control"
                  placeholder="Eg. Participación del programa ..." v-model="$v.semillero.nombre.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div class="" v-if="$v.semillero.nombre.$anyDirty && $v.semillero.nombre.$invalid">
                  <small class="form-text text-danger" v-if="!$v.semillero.nombre.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.semillero.nombre.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.semillero.nombre.minLength"
                    v-text="$t('entity.validation.minlength', { min: 1 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
            </div>
            <div calss="row mx-0">
              <div class="col form-group px-0">
                <label class="form-control-label campo_requerido" for="informacionGeneral">Información General</label>
                <div class="d-flex justify-content-end">
                  <small class="count_tamano_text"
                    :class="{ count_text_limit: countCharacter(1000, $v.semillero.informacionGeneral.$model) < 10 }">Quedan
                    {{
                    countCharacter(1000, $v.semillero.informacionGeneral.$model) }} caracteres</small>
                </div>
                <textarea id="informacionGeneral" class="form-control" name="informacionGeneral" cols="50" rows="10"
                  placeholder="Eg. Profesor graduado de la universida...."
                  v-model="$v.semillero.informacionGeneral.$model" :disabled="checkHabilitacionCampos()"
                  required></textarea>
                <div class=""
                  v-if="$v.semillero.informacionGeneral.$anyDirty && $v.semillero.informacionGeneral.$invalid">
                  <small class="form-text text-danger" v-if="!$v.semillero.informacionGeneral.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.semillero.informacionGeneral.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 1000 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.semillero.informacionGeneral.minLength"
                    v-text="$t('entity.validation.minlength', { min: 1 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
            </div>
          </div>
          <section class="buttons_formulario">
            <div class="row mx-0">
              <div class="col-sm-auto">
                <button v-if="enableEdit" class="btn btn_cancel d-flex align-items-center justify-content-center"
                  type="button" id="btn_cancel" v-text="$t('entity.action.cancel')" v-on:click="cancelarAccion()">
                  Cancelar
                </button>
              </div>
              <div class="col-sm-auto">
                <button v-if="enableEdit" class="btn btn_guardar d-flex align-items-center justify-content-center"
                  type="submit" id="btn_guardar" v-text="$t('entity.action.save')"
                  :disabled="$v.semillero.$invalid || isSaving">
                  Guardar
                </button>
              </div>
            </div>
          </section>
        </div>
      </form>
    </section>
  </div>
</template>
<script lang="ts" src="./semillero-formulario.component.ts"></script>
