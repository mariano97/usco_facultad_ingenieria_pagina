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
            <h2 class="title_formulario_return">Profesor</h2>
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
                    v-text="$t('entity.validation.minlength', { min: 5 })">
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
                    v-text="$t('entity.validation.minlength', { min: 5 })">
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
                    v-text="$t('entity.validation.minlength', { min: 10 })">
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
              <div class="col form-group">
                <label class="form-control-label" for="oficina"
                  v-text="$t('profesor.formulario.labels.oficina')"></label>
                <input type="text" id="oficina" name="oficina" class="form-control" placeholder="Eg. Piso 3 bloque B"
                  v-model="$v.profesor.oficina.$model" :disabled="checkHabilitacionCampos()" />
                <div class="" v-if="$v.profesor.oficina.$anyDirty && $v.profesor.oficina.$invalid">
                  <small class="form-text text-danger" v-if="!$v.profesor.oficina.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 255 })">
                    This field cannot be longer than 50 characters.
                  </small>
                </div>
              </div>
              <div class="col form-group">
                <label class="form-control-label campo_requerido" for="tablaElementoCatalogo"
                  v-text="$t('profesor.formulario.labels.tipoProfesor')"></label>
                <select class="form-control" id="tablaElementoCatalogo" data-cy="tablaElementoCatalogo"
                  name="tablaElementoCatalogo" v-model="profesor.tablaElementoCatalogo"
                  :disabled="checkHabilitacionCampos()" required>
                  <option v-if="!profesor.tablaElementoCatalogo" v-bind:value="null" selected></option>
                  <option v-bind:value="
                    profesor.tablaElementoCatalogo && tablaElementoCatalogoOption.id === profesor.tablaElementoCatalogo.id
                      ? profesor.tablaElementoCatalogo
                      : tablaElementoCatalogoOption
                  " v-for="tablaElementoCatalogoOption in tiposProfesoresElemento"
                    :key="tablaElementoCatalogoOption.id">
                    {{ tablaElementoCatalogoOption.nombre }}
                  </option>
                </select>
                <div class=""
                  v-if="$v.profesor.tablaElementoCatalogo.$anyDirty && $v.profesor.tablaElementoCatalogo.$invalid">
                  <small class="form-text text-danger" v-if="!$v.profesor.tablaElementoCatalogo.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
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
                    :class="{ count_text_limit: countCharacter(255, $v.profesor.perfil.$model) < 10 }">Quedan {{
                    countCharacter(255, $v.profesor.perfil.$model) }} caracteres</small>
                </div>
                <textarea id="perfil" class="form-control" name="perfil" cols="50" rows="10"
                  placeholder="Eg. Profesor graduado de la universida...." v-model="$v.profesor.perfil.$model"
                  :disabled="checkHabilitacionCampos()" required></textarea>
                <div class="" v-if="$v.profesor.perfil.$anyDirty && $v.profesor.perfil.$invalid">
                  <small class="form-text text-danger" v-if="!$v.profesor.perfil.required"
                    v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.profesor.perfil.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 255 })">
                    This field cannot be longer than 50 characters.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.profesor.perfil.minLength"
                    v-text="$t('entity.validation.minlength', { min: 10 })">
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
    <section class="seccion_estudios_profesor" v-if="userAccount.id && profesor.id">
      <div class="tab">
        <input type="checkbox" id="estudios_profesor" />
        <label class="tab-label item_acordion" for="estudios_profesor"
          v-text="$t('profesor.formulario.labels.estudiosProfesor')"></label>
        <div class="tab-content web_tabs">
          <div class="">
            <div class="contenido_listado_estudios">
              <div class="row mx-0 titulo_academico_item" v-for="titulo in titulosAcademicosProfesorLista"
                :key="titulo.id">
                <div class="col container_name_titulo d-flex align-items-center">
                  <h3>{{ titulo.nombreTitulo + ' - ' + convertDateTimeFromServer(titulo.yearTitulo) }}</h3>
                </div>
                <div class="col-sm-auto row mx-0">
                  <div class="col-sm-auto opcion_titulo_academico d-flex align-items-center justify-content-center">
                    <a class="opcion_titulo_academico editar_titulo" type="button"
                      @click="openPopupEditarNuevoTituloAcademico(titulo)">
                      <img alt="ver_titulo" src="/content/images/iconos/change-red.png" />
                    </a>
                  </div>
                  <div class="col-sm-auto opcion_titulo_academico d-flex align-items-center justify-content-center">
                    <a class="opcion_titulo_academico borrar_titulo" type="button"
                      @click="eliminarTituloAcademico(titulo)">
                      <img alt="eliminar_titulo" src="/content/images/iconos/bin-red.png" />
                    </a>
                  </div>
                </div>
              </div>
            </div>
            <div class="d-flex align-items-center justify-content-end mt-3">
              <button class="btn btn_agregar_estudio_profesor d-flex align-items-center justify-content-center"
                id="btn_agregar_estudio_profesor" v-text="$t('profesor.buttons.agregarEstudio')"
                v-on:click="openPopupAgregarNuevoTituloAcademico()">
                Agregar red social
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="seccion_cursos_profesor" v-if="userAccount.id && profesor.id">
      <div class="tab">
        <input type="checkbox" id="materias_profesor" />
        <label class="tab-label item_acordion" for="materias_profesor"
          v-text="$t('profesor.formulario.labels.materiasProfesor')"></label>
        <div class="tab-content web_tabs">
          <div class="">
            <div class="contenido_listado_materias">
              <div class="row mx-0 curso_materia_item" v-for="curso in profesor.cursoMaterias" :key="curso.id">
                <div class="col container_name">
                  <h3>{{ curso.nombre }}</h3>
                </div>
                <div class="col container_name">
                  <h3>{{ curso.nivelAcademico.nombre }}</h3>
                </div>
                <div class="col-sm-auto row mx-0">
                  <div class="col-sm-auto opcion_curso_materia d-flex align-items-center justify-content-center">
                    <a class="opcion_curso_materia editar_titulo" type="button"
                      @click="openPopupEditarCursoMateria(curso)">
                      <img alt="ver_titulo" src="/content/images/iconos/eye-red.png" />
                    </a>
                  </div>
                  <div class="col-sm-auto opcion_curso_materia d-flex align-items-center justify-content-center">
                    <a class="opcion_curso_materia borrar_titulo" type="button" @click="eliminarCursoMateria(curso)">
                      <img alt="eliminar_titulo" src="/content/images/iconos/bin-red.png" />
                    </a>
                  </div>
                </div>
              </div>
            </div>
            <div class="d-flex align-items-center justify-content-end mt-3">
              <button class="btn btn_agregar_materia_profesor d-flex align-items-center justify-content-center"
                id="btn_agregar_materia_profesor" v-text="$t('profesor.buttons.agregarCursoMateria')"
                v-on:click="openPopupAgregarCursoMateria()">
                Agregar curso
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section>
      <b-modal id="modalPopupCrearEstudioProfesor" ref="modalPopupCrearEstudioProfesor">
        <div class="modal-body">
          <div class="">
            <form name="formTituloEstudioProfesor" role="form" novalidate>
              <div class="row mx-0">
                <div class="col form-group">
                  <label class="form-control-label campo_requerido" for="nombreTitulo"
                    v-text="$t('profesor.popups.labels.nombreTitulo')"></label>
                  <input type="text" id="nombreTitulo" name="nombreTitulo" class="form-control"
                    placeholder="Eg. Ingeniero..." v-model="tituloAcademicoProfesor.nombreTitulo" required
                    @change="checkCamposFormularioTitulosAcademicos" />
                  <div
                    v-if="$v.tituloAcademicoProfesor.nombreTitulo.$anyDirty && $v.tituloAcademicoProfesor.nombreTitulo.$invalid">
                    <small class="form-text text-danger" v-if="!$v.tituloAcademicoProfesor.nombreTitulo.required"
                      v-text="$t('entity.validation.required')">
                      This field is required.
                    </small>
                    <small class="form-text text-danger" v-if="!$v.tituloAcademicoProfesor.nombreTitulo.minLength"
                      v-text="$t('entity.validation.minlength', { min: 5 })">
                      This field cannot be longer than 50 characters.
                    </small>
                  </div>
                </div>
                <div class="col form-group">
                  <label class="form-control-label campo_requerido" for="nombreUniversidadTitulo"
                    v-text="$t('profesor.popups.labels.nombreUniversidadTitulo')"></label>
                  <input type="text" id="nombreUniversidadTitulo" name="nombreUniversidadTitulo" class="form-control"
                    placeholder="Eg. Universidad..." v-model="tituloAcademicoProfesor.nombreUniversidadTitulo"
                    @change="checkCamposFormularioTitulosAcademicos" required />
                  <div v-if="
                    $v.tituloAcademicoProfesor.nombreUniversidadTitulo.$anyDirty &&
                    $v.tituloAcademicoProfesor.nombreUniversidadTitulo.$invalid
                  ">
                    <small class="form-text text-danger"
                      v-if="!$v.tituloAcademicoProfesor.nombreUniversidadTitulo.required"
                      v-text="$t('entity.validation.required')">
                      This field is required.
                    </small>
                    <small class="form-text text-danger"
                      v-if="!$v.tituloAcademicoProfesor.nombreUniversidadTitulo.minLength"
                      v-text="$t('entity.validation.minlength', { min: 5 })">
                      This field cannot be longer than 50 characters.
                    </small>
                  </div>
                </div>
                <div class="col-3 form-group">
                  <label class="form-control-label campo_requerido" for="yearTitulo"
                    v-text="$t('profesor.popups.labels.yearTitulo')"></label>
                  <input type="date" id="yearTitulo" name="yearTitulo" class="form-control" min="1900-01-01"
                    :max="dateMax" :value="convertDateTimeFromServer(tituloAcademicoProfesor.yearTitulo)"
                    @change="updateInstantFieldTituloAcademico('yearTitulo', $event)" required />
                  <div
                    v-if="tituloAcademicoProfesor.yearTitulo === null || tituloAcademicoProfesor.yearTitulo === undefined">
                    <small class="form-text text-danger" v-text="$t('entity.validation.required')"> This field is
                      required. </small>
                  </div>
                  <!--<div
                    v-if="$v.tituloAcademicoProfesor.yearTitulo.$anyDirty && $v.tituloAcademicoProfesor.yearTitulo.$invalid">
                    <small class="form-text text-danger" v-if="!$v.tituloAcademicoProfesor.yearTitulo.required"
                      v-text="$t('entity.validation.required')">
                      This field is required.
                    </small>
                  </div>-->
                </div>
              </div>
              <div class="row mx-0">
                <div class="col-3 form-group">
                  <label class="form-control-label campo_requerido" for="tablaElementoCatalogo"
                    v-text="$t('profesor.popups.labels.tipoTitulo')"></label>
                  <select class="form-control" id="tablaElementoCatalogo" data-cy="tablaElementoCatalogo"
                    name="tablaElementoCatalogo" v-model="tituloAcademicoProfesor.tablaElementoCatalogo"
                    @change="checkCamposFormularioTitulosAcademicos" required>
                    <option v-if="!tituloAcademicoProfesor.tablaElementoCatalogo" v-bind:value="null" selected></option>
                    <option v-bind:value="
                      tituloAcademicoProfesor.tablaElementoCatalogo &&
                      tablaElementoCatalogoOption.id === tituloAcademicoProfesor.tablaElementoCatalogo.id
                        ? tituloAcademicoProfesor.tablaElementoCatalogo
                        : tablaElementoCatalogoOption
                    " v-for="tablaElementoCatalogoOption in listaTiposTitulosAcademicos"
                      :key="tablaElementoCatalogoOption.id">
                      {{ tablaElementoCatalogoOption.nombre }}
                    </option>
                  </select>
                  <div v-if="
                    $v.tituloAcademicoProfesor.tablaElementoCatalogo.$anyDirty &&
                    $v.tituloAcademicoProfesor.tablaElementoCatalogo.$invalid
                  ">
                    <small class="form-text text-danger"
                      v-if="!$v.tituloAcademicoProfesor.tablaElementoCatalogo.required"
                      v-text="$t('entity.validation.required')">
                      This field is required.
                    </small>
                  </div>
                </div>
                <div class="col-3 form-group">
                  <label class="form-control-label campo_requerido" for="paises"
                    v-text="$t('profesor.popups.labels.paiseTitulo')"></label>
                  <select class="form-control" id="paises" data-cy="paises" name="paises"
                    v-model="tituloAcademicoProfesor.paises" @change="checkCamposFormularioTitulosAcademicos" required>
                    <option v-if="!tituloAcademicoProfesor.paises" v-bind:value="null" selected></option>
                    <option v-bind:value="
                      tituloAcademicoProfesor.paises && paisesOption.id === tituloAcademicoProfesor.paises.id
                        ? tituloAcademicoProfesor.paises
                        : paisesOption
                    " v-for="paisesOption in listaPaises" :key="paisesOption.id">
                      {{ paisesOption.nombrePais }}
                    </option>
                  </select>
                  <div v-if="$v.tituloAcademicoProfesor.paises.$anyDirty && $v.tituloAcademicoProfesor.paises.$invalid">
                    <small class="form-text text-danger" v-if="!$v.tituloAcademicoProfesor.paises.required"
                      v-text="$t('entity.validation.required')">
                      This field is required.
                    </small>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
        <div slot="modal-footer">
          <div class="container_spinner d-flex align-items-center justify-content-center px-3">
            <b-spinner v-if="isSaveTituloAcademico" type="grow" label="Cargando" variant="danger"></b-spinner>
          </div>
          <button v-if="!isSaveTituloAcademico" type="button" class="btn btn_subir_documento"
            id="programaSubirDocumento" data-cy="entitySubirDocumentoButton" v-text="$t('entity.action.save')"
            v-on:click="guardarTituloAcademico()"
            :disabled="$v.tituloAcademicoProfesor.$invalid || isSaveTituloAcademico">
            guardar
          </button>
          <button type="button" class="btn btn_cancelar_subida" v-text="$t('entity.action.cancel')"
            v-on:click="closePopupAgregarNuevoTituloAcademico()">
            Cancel
          </button>
        </div>
      </b-modal>
      <b-modal id="modalPopupAgregarMateria" ref="modalPopupAgregarMateria">
        <div class="modal-body">
          <div class="">
            <div class="row mx-0">
              <div class="col form-group">
                <label class="form-control-label" for="popupCursoMateria"
                  v-text="$t('profesor.popups.labels.cursoMateria')"></label>
                <select id="popupCursoMateria" name="popupCursoMateria" class="form-control" required
                  @change="changeCursoMateria" :disabled="!isAgregarCurso">
                  <option v-if="!cursoMateriaSeleccionado.id" v-bind:value="null" selected></option>
                  <option
                    v-bind:value="cursoMateriaSeleccionado && curso.id === cursoMateriaSeleccionado.id ? cursoMateriaSeleccionado.id : curso.id"
                    v-for="curso in filterListaCursosMaterias()" :key="curso.id">
                    {{ curso.nombre }}</option>
                </select>
              </div>
            </div>
            <div class="row mx-0 mt-3" v-if="cursoMateriaSeleccionado.id">
              <div class="col">
                <h3 class="nombre_propiedad">{{ cursoMateriaSeleccionado.nombre }}</h3>
              </div>
              <div class="col">
                <h3 class="nombre_propiedad">{{ cursoMateriaSeleccionado.numeroCreditos + ' Creditos' }}</h3>
              </div>
              <div class="col">
                <h3 class="nombre_propiedad">{{ cursoMateriaSeleccionado.semestreImpartida + ' Semestre' }}</h3>
              </div>
            </div>
            <div class="row mx-0 mt-2" v-if="cursoMateriaSeleccionado.nivelAcademico">
              <div class="col">
                <h3 class="nombre_propiedad">{{ 'Nivel Academico: ' + cursoMateriaSeleccionado.nivelAcademico.nombre }}
                </h3>
              </div>
            </div>
          </div>
        </div>
        <div slot="modal-footer">
          <button v-if="isAgregarCurso" type="button" class="btn btn_subir_documento btn_agregar_curso"
            id="profesroAgregarCurso" v-text="$t('entity.action.save')"
            v-on:click="agregarCursoMateria(cursoMateriaSeleccionado)"
            :disabled="!cursoMateriaSeleccionado || !cursoMateriaSeleccionado.id">
            guardar
          </button>
          <button type="button" class="btn btn_cancelar_subida" v-text="$t('entity.action.cancel')"
            v-on:click="closePopupAgregarCursoMateria()">
            Cancel
          </button>
        </div>
      </b-modal>
    </section>
  </div>
</template>
<script lang="ts" src="./profesor-formulario.component.ts"></script>
