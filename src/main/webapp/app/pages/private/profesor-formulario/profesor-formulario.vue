<template>
  <div class="container_formulario_profesor">
    <section class="header_arrow d-flex justify-content-between">
      <div class="d-flex justify-content-start">
        <div class="row mx-0">
          <div class="col-sm-auto px-0 d-flex align-items-center">
            <router-link :to="{ name: 'usuario_profesores_lista' }" class="">
              <img alt="retornar" src="/content/images/iconos/left-arrow-red.png" />
            </router-link>
          </div>
          <div class="col d-flex align-items-center">
            <h2 class="title_formulario_return">Programa</h2>
          </div>
        </div>
      </div>
      <div class="col-sm-auto row mx-0">
        <button v-if="isModeEdit && !enableEdit"
          class="btn btn_editar d-flex align-items-center justify-content-center col-sm-auto" type="button"
          id="btn_editar" v-text="$t('entity.action.edit')" v-on:click="enableFormularioEditar()">
          Editar
        </button>
        <!--<button
          v-if="isModeEdit && !enableEdit"
          class="btn btn_editar d-flex align-items-center justify-content-center col-sm-auto"
          type="button"
          id="btn_editar"
          v-text="$t('entity.action.delete')"
          v-on:click="enableFormularioEditar()"
        >
          Eliminar
        </button>-->
      </div>
    </section>
    <section class="profesor_formulario">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="guardar()">
        <div class="">
          <div class="">
            <div class="row mx-0">
              <div class="col-3 form-group px-0">
                <label class="form-control-label campo_requerido" for="firstName"
                  v-text="$t('profesor.formulario.labels.primerNombre')"></label>
                <input type="text" id="firstName" name="firstName" class="form-control" placeholder="Eg. Edgar"
                  v-model="$v.userAccount.firstName.$model" :disabled="checkHabilitacionCampos()" required />
                <div class="" v-if="$v.userAccount.firstName.$anyDirty && $v.userAccount.firstName.$invalid">
                  <small class="form-text text-danger" v-if="!$v.userAccount.firstName.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.userAccount.firstName.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.userAccount.firstName.minLength"
                    v-text="$t('entity.validation.minlength', { max: 5 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
              <div class="col-3 form-group">
                <label class="form-control-label" for="secondName"
                  v-text="$t('profesor.formulario.labels.segundoNombre')"></label>
                <input type="text" id="secondName" name="secondName" class="form-control" placeholder="Eg. Andrés"
                  v-model="$v.userAccount.secondName.$model" :disabled="checkHabilitacionCampos()" />
                <div class="" v-if="$v.userAccount.secondName.$anyDirty && $v.userAccount.secondName.$invalid">
                  <small class="form-text text-danger" v-if="!$v.userAccount.secondName.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
              <div class="col form-group px-0">
                <label class="form-control-label campo_requerido" for="lastName"
                  v-text="$t('profesor.formulario.labels.lastName')"></label>
                <input type="text" id="lastName" name="lastName" class="form-control" placeholder="Eg. Andrés"
                  v-model="$v.userAccount.lastName.$model" :disabled="checkHabilitacionCampos()" required />
                <div class="" v-if="$v.userAccount.lastName.$anyDirty && $v.userAccount.lastName.$invalid">
                  <small class="form-text text-danger" v-if="!$v.userAccount.lastName.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.userAccount.lastName.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.userAccount.lastName.minLength"
                    v-text="$t('entity.validation.minlength', { max: 5 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
            </div>
            <div class="row mx-0">
              <div class="col-5 form-group px-0">
                <label class="form-control-label campo_requerido" for="email"
                  v-text="$t('profesor.formulario.labels.email')"></label>
                <input type="text" id="email" name="email" class="form-control" placeholder="Eg. example@example.com"
                  v-model="$v.userAccount.email.$model" :disabled="checkHabilitacionCampos()" required />
                <div class="" v-if="$v.userAccount.email.$anyDirty && $v.userAccount.email.$invalid">
                  <small class="form-text text-danger" v-if="!$v.userAccount.email.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.userAccount.email.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.userAccount.email.minLength"
                    v-text="$t('entity.validation.minlength', { max: 10 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.userAccount.email.email"
                    v-text="$t('entity.validation.email')">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
              <div class="col-5 form-group">
                <label class="form-control-label" for="emailAlternativo"
                  v-text="$t('profesor.formulario.labels.emailAlternativo')"></label>
                <input type="text" id="emailAlternativo" name="emailAlternativo" class="form-control"
                  placeholder="Eg. example@example.com" v-model="$v.profesor.emailAlternativo.$model"
                  :disabled="checkHabilitacionCampos()" />
                <div class="" v-if="$v.profesor.emailAlternativo.$anyDirty && $v.profesor.emailAlternativo.$invalid">
                  <small class="form-text text-danger" v-if="!$v.profesor.emailAlternativo.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.profesor.emailAlternativo.email"
                    v-text="$t('entity.validation.email')">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
              <div class="col form-group px-0">
                <label class="form-control-label" for="telefonoCelular"
                  v-text="$t('profesor.formulario.labels.telCelular')"></label>
                <input type="text" id="telefonoCelular" name="telefonoCelular" class="form-control"
                  placeholder="Eg. 31234567890" v-model="$v.profesor.telefonoCelular.$model"
                  :disabled="checkHabilitacionCampos()" />
                <div class="" v-if="$v.profesor.telefonoCelular.$anyDirty && $v.profesor.telefonoCelular.$invalid">
                  <small class="form-text text-danger" v-if="!$v.profesor.telefonoCelular.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 50 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
            </div>
            <div class="row mx-0">
              <div class="col form-group px-0">
                <label class="form-control-label campo_requerido" for="perfil"
                  v-text="$t('profesor.formulario.labels.perfil')"></label>
                <div class="d-flex justify-content-end">
                  <small class="count_tamano_text"
                    :class="{ count_text_limit: countCharacter(255, $v.profesor.perfil.$model) < 10 }">Quedan
                    {{ countCharacter(255, $v.profesor.perfil.$model) }} caracteres</small>
                </div>
                <textarea id="perfil" class="form-control" name="perfil" cols="50" rows="10"
                  placeholder="Eg. Profesor graduado de la universida...." v-model="$v.profesor.perfil.$model"
                  :disabled="checkHabilitacionCampos()" required></textarea>
                <div class="" v-if="$v.profesor.perfil.$anyDirty && $v.profesor.perfil.$invalid">
                  <div>{{ $v.profesor.perfil }}</div>
                  <small class="form-text text-danger" v-if="!$v.profesor.perfil.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.profesor.perfil.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 255 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.profesor.perfil.minLength"
                    v-text="$t('entity.validation.minlength', { max: 10 })">
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
                  :disabled="$v.userAccount.$invalid || $v.profesor.$invalid || isSaving">
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
<script lang="ts" src="./profesor-formulario.component.ts"></script>
