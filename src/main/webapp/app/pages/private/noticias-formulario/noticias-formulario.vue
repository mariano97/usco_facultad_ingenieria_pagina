<template>
  <div class="container_formulario_noticias">
    <section class="header_arrow d-flex justify-content-between">
      <div class="d-flex justify-content-start">
        <div class="row mx-0">
          <div class="col-sm-auto px-0 d-flex align-items-center">
            <router-link :to="{ name: 'noticias_lista' }" class="">
              <img alt="retornar" src="/content/images/iconos/left-arrow-red.png" />
            </router-link>
          </div>
          <div class="col d-flex align-items-center">
            <h2 class="title_formulario_return">Noticia</h2>
          </div>
        </div>
      </div>
      <div class="col-sm-auto row mx-0">
        <button v-if="isModeEdit && !enableEdit"
          class="btn btn_editar d-flex align-items-center justify-content-center col-sm-auto" type="button"
          id="btn_editar" v-text="$t('entity.action.edit')" v-on:click="enableFormularioEditar()">
          Editar
        </button>
      </div>
    </section>
    <section class="noticia_formulario">
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
                <label class="form-control-label campo_requerido" for="titulo">Titulo</label>
                <input type="text" id="titulo" name="titulo" class="form-control"
                  placeholder="Eg. Participación del programa ..." v-model="$v.noticia.titulo.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div class="" v-if="$v.noticia.titulo.$anyDirty && $v.noticia.titulo.$invalid">
                  <small class="form-text text-danger" v-if="!$v.noticia.titulo.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.noticia.titulo.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.noticia.titulo.minLength"
                    v-text="$t('entity.validation.minlength', { min: 1 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
              <div class="form-group col-3">
                <label for="fecha" class="form-control-label campo_requerido">Fecha</label>
                <!--<date-picker type="date">

                </date-picker>-->
                <input type="date" id="fecha" name="fecha" class="form-control" min="1900-01-01" :max="dateMax"
                  :value="convertDateTimeFromServer($v.noticia.fecha.$model)"
                  @change="updateInstantField('fecha', $event)" :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.noticia.fecha.$anyDirty && $v.noticia.fecha.$invalid">
                  <small class="form-text text-danger" v-if="!$v.noticia.fecha.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.noticia.fecha.ZonedDateTimelocal"
                    v-text="$t('entity.validation.ZonedDateTimelocal')">
                    This field should be a date and time.
                  </small>
                </div>
              </div>
            </div>
            <div class="row mx-0">
              <div class="col form-group px-0">
                <label class="form-control-label campo_requerido" for="sintesis">Sintesis</label>
                <div class="d-flex justify-content-end">
                  <small class="count_tamano_text"
                    :class="{ count_text_limit: countCharacter(500, $v.noticia.sintesis.$model) < 10 }">Quedan {{
                    countCharacter(500, $v.noticia.sintesis.$model) }} caracteres</small>
                </div>
                <textarea id="sintesis" class="form-control" name="sintesis" cols="50" rows="10"
                  placeholder="Eg. Profesor graduado de la universida...." v-model="$v.noticia.sintesis.$model"
                  :disabled="checkHabilitacionCampos()" required></textarea>
                <div class="" v-if="$v.noticia.sintesis.$anyDirty && $v.noticia.sintesis.$invalid">
                  <small class="form-text text-danger" v-if="!$v.noticia.sintesis.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.noticia.sintesis.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 500 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.noticia.sintesis.minLength"
                    v-text="$t('entity.validation.minlength', { min: 1 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
            </div>
            <div class="row mx-0">
              <div class="col form-group px-0">
                <label class="form-control-label campo_requerido" for="cuerpoDescripcion">Cuerpo Descripcion</label>
                <div class="d-flex justify-content-end">
                  <small class="count_tamano_text"
                    :class="{ count_text_limit: countCharacter(1000, $v.noticia.cuerpoDescripcion.$model) < 10 }">Quedan
                    {{
                    countCharacter(1000, $v.noticia.cuerpoDescripcion.$model) }} caracteres</small>
                </div>
                <textarea id="cuerpoDescripcion" class="form-control" name="cuerpoDescripcion" cols="50" rows="10"
                  placeholder="Eg. Profesor graduado de la universida...." v-model="$v.noticia.cuerpoDescripcion.$model"
                  :disabled="checkHabilitacionCampos()" required></textarea>
                <div class="" v-if="$v.noticia.cuerpoDescripcion.$anyDirty && $v.noticia.cuerpoDescripcion.$invalid">
                  <small class="form-text text-danger" v-if="!$v.noticia.cuerpoDescripcion.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.noticia.cuerpoDescripcion.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 1000 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.noticia.cuerpoDescripcion.minLength"
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
                  :disabled="$v.noticia.$invalid || isSaving">
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
<script lang="ts" src="./noticias-formulario.component.ts"></script>
