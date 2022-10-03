<template>
  <div class="container_formulario_laboratorio">
    <section class="header_arrow d-flex justify-content-between">
      <div class="d-flex justify-content-start">
        <div class="row mx-0">
          <div class="col-sm-auto px-0 d-flex align-items-center">
            <router-link :to="{ name: 'laboratorios_lista' }" class="">
              <img alt="retornar" src="/content/images/iconos/left-arrow-red.png" />
            </router-link>
          </div>
          <div class="col d-flex align-items-center">
            <h2 class="title_formulario_return">Laboratorio</h2>
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
    <section class="laboratorio_formulario">
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
                  placeholder="Eg. Participación del programa ..." v-model="$v.laboratorio.nombre.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div class="" v-if="$v.laboratorio.nombre.$anyDirty && $v.laboratorio.nombre.$invalid">
                  <small class="form-text text-danger" v-if="!$v.laboratorio.nombre.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.laboratorio.nombre.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.laboratorio.nombre.minLength"
                    v-text="$t('entity.validation.minlength', { min: 1 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
              <div class="col form-group">
                <label class="form-control-label campo_requerido">Tipo de laboratorio</label>
                <select
                  class="form-control"
                  id="tipoLaboratorio"
                  name="tipoLaboratorio"
                  v-model="laboratorio.tipoLaboratorio"
                  :disabled="checkHabilitacionCampos()"
                  required
                >
                  <option v-if="!laboratorio.tipoLaboratorio" v-bind:value="null" selected>Seleccione</option>
                  <option
                    v-bind:value="
                      laboratorio.tipoLaboratorio && tipoLab.id === laboratorio.tipoLaboratorio.id ? laboratorio.tipoLaboratorio : tipoLab
                    "
                    v-for="tipoLab in filterTiposLaboratorio()"
                    :key="tipoLab.id"
                  >
                    {{ tipoLab.nombre }}
                  </option>
                </select>
                <div v-if="$v.laboratorio.tipoLaboratorio.$anyDirty && $v.laboratorio.tipoLaboratorio.$invalid">
                  <small class="form-text text-danger" v-if="!$v.laboratorio.tipoLaboratorio.required" v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                </div>
              </div>
              <div class="col form-group px-0">
                <label class="form-control-label campo_requerido" for="correoContacto">Correo de contacto</label>
                <input type="text" id="correoContacto" name="correoContacto" class="form-control"
                  placeholder="Eg. Participación del programa ..." v-model="$v.laboratorio.correoContacto.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div class="" v-if="$v.laboratorio.correoContacto.$anyDirty && $v.laboratorio.correoContacto.$invalid">
                  <small class="form-text text-danger" v-if="!$v.laboratorio.correoContacto.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.laboratorio.correoContacto.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.laboratorio.correoContacto.minLength"
                    v-text="$t('entity.validation.minlength', { min: 1 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
            </div>
            <div class="row mx-0">
              <div class="col form-group px-0">
                <label class="form-control-label" for="direccion">Dirección / Ubicación</label>
                <div class="d-flex justify-content-end">
                  <small class="count_tamano_text"
                    :class="{ count_text_limit: countCharacter(500, $v.laboratorio.direccion.$model) < 10 }">Quedan
                    {{
                    countCharacter(500, $v.laboratorio.direccion.$model) }} caracteres</small>
                </div>
                <textarea id="direccion" class="form-control" name="direccion" cols="50" rows="10"
                  placeholder="Eg. Cra 1H...."
                  v-model="$v.laboratorio.direccion.$model" :disabled="checkHabilitacionCampos()"
                  required></textarea>
                <div class=""
                  v-if="$v.laboratorio.direccion.$anyDirty && $v.laboratorio.direccion.$invalid">
                  <small class="form-text text-danger" v-if="!$v.laboratorio.direccion.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.laboratorio.direccion.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 500 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.laboratorio.direccion.minLength"
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
                    :class="{ count_text_limit: countCharacter(1000, $v.laboratorio.informacionGeneral.$model) < 10 }">Quedan
                    {{
                    countCharacter(1000, $v.laboratorio.informacionGeneral.$model) }} caracteres</small>
                </div>
                <textarea id="informacionGeneral" class="form-control" name="informacionGeneral" cols="50" rows="10"
                  placeholder="Eg. Profesor graduado de la universida...."
                  v-model="$v.laboratorio.informacionGeneral.$model" :disabled="checkHabilitacionCampos()"
                  required></textarea>
                <div class=""
                  v-if="$v.laboratorio.informacionGeneral.$anyDirty && $v.laboratorio.informacionGeneral.$invalid">
                  <small class="form-text text-danger" v-if="!$v.laboratorio.informacionGeneral.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.laboratorio.informacionGeneral.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 1000 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.laboratorio.informacionGeneral.minLength"
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
                  :disabled="$v.laboratorio.$invalid || isSaving">
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
<script lang="ts" src="./laboratorio-formulario.component.ts"></script>
