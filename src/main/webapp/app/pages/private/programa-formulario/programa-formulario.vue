<template>
  <div class="container_formulario_programa">
    <section class="header_arrow d-flex justify-content-between">
      <div class="d-flex justify-content-start">
        <div class="row mx-0">
          <div class="col-sm-auto px-0 d-flex align-items-center">
            <router-link :to="{ name: 'usuario_programas_lista' }" class="">
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
    <section class="programa_formulario">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="guardar()">
        <div class="">
          <div class="container_upload_image d-flex align-items-end" :class="{ 'flex-column': showImage }">
            <img v-if="showImage" class="image_programa" alt="imagen_programa"
              :src="imageProfilePrograma | headerFileBase64" />
            <a id="hide" type="button" class="btn btn-upload d-flex align-items-center justify-content-center">
              <label for="file-input" class="row mx-0 align-items-center justify-content-center">
                <div class="col-sm-auto">
                  <img class="image" alt="image-upload" src="/content/images/iconos/icon-upload.png" />
                </div>
                <div class="">
                  <h4 v-text="$t('programa.formulario.labels.subirImagen')">Subir imagen</h4>
                </div>
              </label>
              <input id="file-input" type="file" accept="image/png, image/jpeg, image/jpg" @change="changeImage" />
            </a>
          </div>
          <div class="">
            <div class="col form-group px-0">
              <label class="form-control-label campo_requerido" v-text="$t('programa.formulario.labels.nombrePrograma')"
                for="nombre_programa">Nombre del programa</label>
              <input type="text" id="nombre_programa" name="nombre_programa" class="form-control"
                placeholder="Eg. Ingenieria de Software" v-model="$v.programa.nombre.$model"
                :disabled="checkHabilitacionCampos()" required />
              <div class="" v-if="$v.programa.nombre.$anyDirty && $v.programa.nombre.$invalid">
                <small class="form-text text-danger" v-if="!$v.programa.nombre.required"
                  v-text="$t('entity.validation.required')">
                  Este campo es obligatorio.
                </small>
              </div>
            </div>
            <div class="row">
              <div class="form-group col-3">
                <label for="codigo_snies" class="form-control-label campo_requerido"
                  v-text="$t('programa.formulario.labels.codigoSnies')">Código SNIES</label>
                <input type="number" id="codigo_snies" name="codigo_snies" class="form-control" placeholder="Eg. 12345"
                  v-model.number="$v.programa.codigoSnies.$model" :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.programa.codigoSnies.$anyDirty && $v.programa.codigoSnies.$invalid">
                  <small class="form-text text-danger" v-if="!$v.programa.codigoSnies.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.programa.codigoSnies.numeric"
                    v-text="$t('entity.validation.number')">
                    This field should be a number.
                  </small>
                </div>
              </div>
              <div class="form-group col">
                <label for="codigo_registro_calificado" class="form-control-label campo_requerido"
                  v-text="$t('programa.formulario.labels.codigoRegistroCalificado')">Código registro calificado</label>
                <input type="number" id="codigo_registro_calificado" name="codigo_registro_calificado"
                  class="form-control" placeholder="Eg. 12345"
                  v-model.number="$v.programa.codigoRegistroCalificado.$model" :disabled="checkHabilitacionCampos()"
                  required />
                <div
                  v-if="$v.programa.codigoRegistroCalificado.$anyDirty && $v.programa.codigoRegistroCalificado.$invalid">
                  <small class="form-text text-danger" v-if="!$v.programa.codigoRegistroCalificado.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.programa.codigoRegistroCalificado.numeric"
                    v-text="$t('entity.validation.number')">
                    This field should be a number.
                  </small>
                </div>
              </div>
              <div class="form-group col">
                <label for="fecha_registro_calificado" class="form-control-label campo_requerido"
                  v-text="$t('programa.formulario.labels.fechaRegistroCalificado')">Fecha registro calificado</label>
                <!--<date-picker type="date">

                </date-picker>-->
                <input type="date" id="fecha_registro_calificado" name="fecha_registro_calificado" class="form-control"
                  min="1900-01-01" :max="dateMax"
                  :value="convertDateTimeFromServer($v.programa.fechaRegistroCalificado.$model)"
                  @change="updateInstantField('fechaRegistroCalificado', $event)" :disabled="checkHabilitacionCampos()"
                  required />
              </div>
            </div>
            <div class="row">
              <div class="form-group col-3">
                <label for="creditos" class="form-control-label campo_requerido"
                  v-text="$t('programa.formulario.labels.creditos')">Creditos</label>
                <input type="number" id="creditos" name="creditos" class="form-control" placeholder="Eg. 345"
                  v-model.number="$v.programa.numeroCreditos.$model" :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.programa.numeroCreditos.$anyDirty && $v.programa.numeroCreditos.$invalid">
                  <small class="form-text text-danger" v-if="!$v.programa.numeroCreditos.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.programa.numeroCreditos.numeric"
                    v-text="$t('entity.validation.number')">
                    This field should be a number.
                  </small>
                </div>
              </div>
              <div class="form-group col">
                <label for="duracion_programa" class="form-control-label"
                  v-text="$t('programa.formulario.labels.duracionPrograma')">Duración programa</label>
                <div class="row">
                  <div class="col">
                    <input type="number" id="duracion_programa" name="duracion_programa" class="form-control"
                      placeholder="Eg. 10" v-model.number="$v.programa.duracionPrograma.$model"
                      :disabled="checkHabilitacionCampos()" />
                  </div>
                  <div class="col-sm-auto">
                    <h4 class="texto-semestre" v-text="$t('programa.formulario.textos.semestres')">Semestres</h4>
                  </div>
                </div>
              </div>
              <div class="form-group col">
                <label for="titulo_otorgado" class="form-control-label campo_requerido"
                  v-text="$t('programa.formulario.labels.titloOtorgado')">Titulo otrogado</label>
                <input type="text" id="titulo_otorgado" name="titulo_otorgado" class="form-control"
                  placeholder="Eg. Ingeniero de software" v-model="$v.programa.nombreTituloOtorgado.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.programa.nombreTituloOtorgado.$anyDirty && $v.programa.nombreTituloOtorgado.$invalid">
                  <small class="form-text text-danger" v-if="!$v.programa.nombreTituloOtorgado.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                </div>
              </div>
              <div class="form-group col-3">
                <label for="costo_programa" class="form-control-label campo_requerido"
                  v-text="$t('programa.formulario.labels.costoPrograma')">Titulo otrogado</label>
                <input type="number" id="costo_programa" name="costo_programa" class="form-control"
                  placeholder="Eg. 1000000" v-model="$v.programa.costoPrograma.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.programa.costoPrograma.$anyDirty && $v.programa.costoPrograma.$invalid">
                  <small class="form-text text-danger" v-if="!$v.programa.costoPrograma.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.programa.costoPrograma.numeric"
                    v-text="$t('entity.validation.number')">
                    This field should be a number.
                  </small>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="form-group col">
                <label for="tipo_metodologia" class="form-control-label campo_requerido"
                  v-text="$t('programa.formulario.labels.tipoMetodologia')">Tipo de metodologia</label>
                <select class="form-control" id="tipo_metodologia" name="tipo_metodologia"
                  v-model="programa.tipoFormacion" :disabled="checkHabilitacionCampos()" required>
                  <option v-if="!programa.tipoFormacion" selected v-bind:value="null">Seleccione</option>
                  <option v-for="tipoFormacion in listTiposFormacion" :key="tipoFormacion.id" v-bind:value="
                    programa.tipoFormacion && programa.tipoFormacion.id === tipoFormacion.id ? programa.tipoFormacion : tipoFormacion
                  ">
                    {{ tipoFormacion.nombre }}
                  </option>
                </select>
                <div v-if="$v.programa.tipoFormacion.$anyDirty && $v.programa.tipoFormacion.$invalid">
                  <small class="form-text text-danger" v-if="!$v.programa.tipoFormacion.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                </div>
              </div>
              <div class="form-group col">
                <label for="nivel_formacion" class="form-control-label campo_requerido"
                  v-text="$t('programa.formulario.labels.nivelFormacion')">Nivel de formación</label>
                <select class="form-control" id="nivel_formacion" name="nivel_formacion"
                  v-model="programa.nivelFormacion" :disabled="checkHabilitacionCampos()" required>
                  <option v-if="!programa.nivelFormacion" selected v-bind:value="null">Seleccione</option>
                  <option v-for="tipoPrograma in listTiposPrograma" :key="tipoPrograma.id" v-bind:value="
                    programa.nivelFormacion && programa.nivelFormacion.id === tipoPrograma.id ? programa.nivelFormacion : tipoPrograma
                  ">
                    {{ tipoPrograma.nombre }}
                  </option>
                </select>
                <div v-if="$v.programa.nivelFormacion.$anyDirty && $v.programa.nivelFormacion.$invalid">
                  <small class="form-text text-danger" v-if="!$v.programa.nivelFormacion.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                </div>
              </div>
            </div>
            <div class="tab">
              <input type="checkbox" id="check_presentacio" />
              <label class="tab-label item_acordion" for="check_presentacio"
                v-text="$t('programa.formulario.labels.presentacionProgramaTitle')">Presentación del programa</label>
              <div class="tab-content web_tabs">
                <div class="">
                  <div class="col px-0 form-group">
                    <label for="presentacion_programa" class="form-control-label campo_requerido"
                      v-text="$t('programa.formulario.labels.presentacionPrograma')">¿Cúal es la presentación del
                      programa?</label>
                    <div class="d-flex justify-content-end">
                      <small class="count_tamano_text"
                        :class="{ count_text_limit: countCharacter(255, $v.programa.presentacionPrograma.$model) < 10 }">Quedan
                        {{ countCharacter(255, $v.programa.presentacionPrograma.$model) }} caracteres</small>
                    </div>
                    <textarea id="presentacion_programa" class="form-control" name="presentacion_programa" cols="50"
                      rows="5" placeholder="Eg. Es un programa dado por profesores de primera..."
                      v-model="$v.programa.presentacionPrograma.$model" :disabled="checkHabilitacionCampos()"
                      required></textarea>
                    <div v-if="$v.programa.presentacionPrograma.$anyDirty && $v.programa.presentacionPrograma.$invalid">
                      <small class="form-text text-danger" v-if="!$v.programa.presentacionPrograma.required"
                        v-text="$t('entity.validation.required')">
                        This field is required.
                      </small>
                      <small class="form-text text-danger" v-if="!$v.sede.presentacionPrograma.maxLength"
                        v-text="$t('entity.validation.maxlength', { max: 255 })">
                        This field cannot be longer than 10 characters.
                      </small>
                    </div>
                  </div>
                  <div class="row">
                    <div class="form-group col">
                      <label for="mision_programa" class="form-control-label campo_requerido"
                        v-text="$t('programa.formulario.labels.misionPrograma')">Misión del programa</label>
                      <div class="d-flex justify-content-end">
                        <small class="count_tamano_text"
                          :class="{ count_text_limit: countCharacter(255, $v.programa.mision.$model) < 10 }">Quedan {{
                          countCharacter(255, $v.programa.mision.$model) }} caracteres</small>
                      </div>
                      <textarea id="mision_programa" class="form-control" name="mision_programa" cols="50" rows="5"
                        placeholder="Eg. En el 2025 seremos el programa más..." :disabled="checkHabilitacionCampos()"
                        v-model="$v.programa.mision.$model" required></textarea>
                      <div v-if="$v.programa.mision.$anyDirty && $v.programa.mision.$invalid">
                        <small class="form-text text-danger" v-if="!$v.programa.mision.required"
                          v-text="$t('entity.validation.required')">
                          This field is required.
                        </small>
                        <small class="form-text text-danger" v-if="!$v.sede.mision.maxLength"
                          v-text="$t('entity.validation.maxlength', { max: 255 })">
                          This field cannot be longer than 10 characters.
                        </small>
                      </div>
                    </div>
                    <div class="form-group col">
                      <label for="vision_programa" class="form-control-label campo_requerido"
                        v-text="$t('programa.formulario.labels.visionPrograma')">Visión del programa</label>
                      <div class="d-flex justify-content-end">
                        <small class="count_tamano_text"
                          :class="{ count_text_limit: countCharacter(255, $v.programa.vision.$model) < 10 }">Quedan {{
                          countCharacter(255, $v.programa.vision.$model) }} caracteres</small>
                      </div>
                      <textarea id="vision_programa" class="form-control" name="vision_programa" cols="50" rows="5"
                        placeholder="Eg. En el 2025 seremos el programa más..." v-model="$v.programa.vision.$model"
                        :disabled="checkHabilitacionCampos()" required></textarea>
                      <div v-if="$v.programa.vision.$anyDirty && $v.programa.vision.$invalid">
                        <small class="form-text text-danger" v-if="!$v.programa.vision.required"
                          v-text="$t('entity.validation.required')">
                          This field is required.
                        </small>
                        <small class="form-text text-danger" v-if="!$v.sede.vision.maxLength"
                          v-text="$t('entity.validation.maxlength', { max: 255 })">
                          This field cannot be longer than 10 characters.
                        </small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="tab">
              <input type="checkbox" id="perfiles_programa" />
              <label class="tab-label item_acordion" for="perfiles_programa"
                v-text="$t('programa.formulario.labels.prefilesProgramaTitle')">Perfiles del programa</label>
              <div class="tab-content web_tabs">
                <div class="">
                  <div class="col px-0 form-group">
                    <label for="perfil_estudiante" class="form-control-label"
                      v-text="$t('programa.formulario.labels.perfilEstudiante')">perfilEstudiante</label>
                    <div class="d-flex justify-content-end">
                      <small class="count_tamano_text"
                        :class="{ count_text_limit: countCharacter(255, $v.programa.perfilEstudiante.$model) < 10 }">Quedan
                        {{ countCharacter(255, $v.programa.perfilEstudiante.$model) }} caracteres</small>
                    </div>
                    <textarea id="perfil_estudiante" class="form-control" name="perfil_estudiante" cols="50" rows="5"
                      placeholder="Eg. En el 2025 seremos el programa más..."
                      v-model="$v.programa.perfilEstudiante.$model" :disabled="checkHabilitacionCampos()"></textarea>
                    <div v-if="$v.programa.perfilEstudiante.$anyDirty && $v.programa.perfilEstudiante.$invalid">
                      <small class="form-text text-danger" v-if="!$v.sede.perfilEstudiante.maxLength"
                        v-text="$t('entity.validation.maxlength', { max: 255 })">
                        This field cannot be longer than 10 characters.
                      </small>
                    </div>
                  </div>
                  <div class="row">
                    <div class="form-group col">
                      <label for="perfil_ocupacional" class="form-control-label"
                        v-text="$t('programa.formulario.labels.perfilOcupacional')">perfilEstudiante</label>
                      <div class="d-flex justify-content-end">
                        <small class="count_tamano_text"
                          :class="{ count_text_limit: countCharacter(255, $v.programa.perfilOcupacional.$model) < 10 }">Quedan
                          {{ countCharacter(255, $v.programa.perfilOcupacional.$model) }} caracteres</small>
                      </div>
                      <textarea id="perfil_ocupacional" class="form-control" name="perfil_ocupacional" cols="50"
                        rows="5" placeholder="Eg. En el 2025 seremos el programa más..."
                        v-model="$v.programa.perfilOcupacional.$model" :disabled="checkHabilitacionCampos()"></textarea>
                      <div v-if="$v.programa.perfilOcupacional.$anyDirty && $v.programa.perfilOcupacional.$invalid">
                        <small class="form-text text-danger" v-if="!$v.sede.perfilOcupacional.maxLength"
                          v-text="$t('entity.validation.maxlength', { max: 255 })">
                          This field cannot be longer than 10 characters.
                        </small>
                      </div>
                    </div>
                    <div class="form-group col">
                      <label for="perfil_egresado" class="form-control-label campo_requerido"
                        v-text="$t('programa.formulario.labels.pefilEgresado')">Visión del programa</label>
                      <div class="d-flex justify-content-end">
                        <small class="count_tamano_text"
                          :class="{ count_text_limit: countCharacter(255, $v.programa.perfilEgresado.$model) < 10 }">Quedan
                          {{ countCharacter(255, $v.programa.perfilEgresado.$model) }} caracteres</small>
                      </div>
                      <textarea id="perfil_egresado" class="form-control" name="perfil_egresado" cols="50" rows="5"
                        placeholder="Eg. En el 2025 seremos el programa más..."
                        v-model="$v.programa.perfilEgresado.$model" :disabled="checkHabilitacionCampos()"
                        required></textarea>
                      <div v-if="$v.programa.perfilEgresado.$anyDirty && $v.programa.perfilEgresado.$invalid">
                        <small class="form-text text-danger" v-if="!$v.programa.perfilEgresado.required"
                          v-text="$t('entity.validation.required')">
                          This field is required.
                        </small>
                        <small class="form-text text-danger" v-if="!$v.sede.perfilEgresado.maxLength"
                          v-text="$t('entity.validation.maxlength', { max: 255 })">
                          This field cannot be longer than 10 characters.
                        </small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="tab">
              <input type="checkbox" id="sedes_programa" />
              <label class="tab-label item_acordion" for="sedes_programa"
                v-text="$t('programa.formulario.labels.sedesPrograma')">Sedes del programa</label>
              <div class="tab-content web_tabs">
                <div class="">
                  <div class="contenido_listado_sede">
                    <div class="row mx-0 sede_item mb-3" v-for="sedeTemp in programa.sedes" :key="sedeTemp.id">
                      <div class="col-sm-auto container_nombre">
                        <h3>{{ sedeTemp.nombre }}</h3>
                      </div>
                      <div class="col ">
                        <h3>{{ sedeTemp.direccion | subString(65, '...') }}</h3>
                      </div>
                      <div class="col-sm-auto d-flex align-items-center container_option_list">
                        <a type="button" class="btn btn_opcion_item d-flex justify-content-center align-items-center"
                          v-on:click="eliminarSedeToPrograma(sedeTemp)">
                          <img alt="eliminar_sede_programa" src="/content/images/iconos/minus.png" />
                        </a>
                      </div>
                    </div>
                  </div>
                  <div class="d-flex align-items-center justify-content-end mt-3">
                    <a type="button" class="btn btn_agregar_sede d-flex align-items-center justify-content-center"
                      id="btn_agregar_sede" v-text="$t('programa.buttons.agregarSedes')"
                      v-on:click="openPopupAgregarSede()">
                      Agregar sede
                    </a>
                  </div>
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
                  :disabled="$v.programa.$invalid || isSaving">
                  Guardar
                </button>
              </div>
            </div>
          </section>
        </div>
      </form>
    </section>
    <section class="seccion_documentos_programas" v-if="programa.id">
      <div class="tab">
        <input type="checkbox" id="documentos_programa" />
        <label class="tab-label item_acordion" for="documentos_programa"
          v-text="$t('programa.formulario.labels.documentosProgramaTitle')">Documentos del programa</label>
        <div class="tab-content web_tabs">
          <div class="">
            <div class="contenido_listado_archivos">
              <div class="row mx-0 archivo_item"
                v-for="archivo in filtrarArchivosProgramaOnlyDocs(archivosProgramaList)" :key="archivo.id">
                <div class="col container_name d-flex">
                  <h3>{{ archivo.nombreArchivo }}</h3>
                  <span v-if="archivo.tablaElementoCatalogo.id === 7" style="font-size: 18px; color: #8f141b"
                    class="pl-3" v-text="$t('archivosPrograma.labels.planEstudio')">Plan de estudio</span>
                </div>
                <div class="col-sm-auto row mx-0">
                  <div class="col-sm-auto opcion_archivo d-flex align-items-center justify-content-center">
                    <a id="opcion_archivo_ver_doc" class="opcion_archivo ver_doc" type="button"
                      @click="verArchivoPrograma(archivo)">
                      <img alt="ver_documento" src="/content/images/iconos/search-red.png" />
                    </a>
                  </div>
                  <div class="col-sm-auto opcion_archivo d-flex align-items-center justify-content-center">
                    <a id="opcion_archivo_cambiar_doc" class="opcion_archivo cambiar_doc" type="button"
                      @click="openPopupActualizarDocumento(POPUP_DOCUMENTO_ACCION_ACTUALIZAR, archivo)">
                      <img alt="ver_documento" src="/content/images/iconos/change-red.png" />
                    </a>
                  </div>
                  <div class="col-sm-auto opcion_archivo d-flex align-items-center justify-content-center">
                    <a id="opcion_archivo_cambiar_doc" class="opcion_archivo borrar_doc" type="button"
                      @click="openPopupEliminarDocumento(archivo)">
                      <img alt="ver_documento" src="/content/images/iconos/bin-red.png" />
                    </a>
                  </div>
                </div>
              </div>
            </div>
            <div class="d-flex align-items-center justify-content-end mt-3">
              <button class="btn btn_agregar_documento d-flex align-items-center justify-content-center"
                id="btn_agregar_documento" v-text="$t('programa.buttons.agregarDocumento')"
                v-on:click="openPopupCrearDocumentoNuevo(POPUP_DOCUMENTO_ACCION_CREAR)">
                Agregar nuevo documento
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="seccion_profesores_programa" v-if="programa.id">
      <div class="tab">
        <input type="checkbox" id="profesores_programa" />
        <label class="tab-label item_acordion" for="profesores_programa"
          v-text="$t('programa.formulario.labels.profesoresPrograma')"></label>
        <div class="tab-content web_tabs">
          <div class="">
            <div class="contenido_listado_profesores">
              <div class="row mx-0 sede_item mb-3" v-for="profesorTemp in programa.profesors"
                :key="profesorTemp.id">
                <div class="col-sm-auto container_nombre" v-if="filtarNombreProfesor(profesorTemp.userId).length > 0">
                  <h3>{{ filtarNombreProfesor(profesorTemp.userId) }}</h3>
                </div>
                <div class="col-sm-auto d-flex align-items-center container_option_list"
                  v-if="filtarNombreProfesor(profesorTemp.userId).length > 0">
                  <a type="button" class="btn btn_opcion_item d-flex justify-content-center align-items-center"
                    v-on:click="eliminarProfesorToPrograma(profesorTemp)">
                    <img alt="eliminar_sede_programa" src="/content/images/iconos/minus.png" />
                  </a>
                </div>
              </div>
            </div>
            <div class="d-flex align-items-center justify-content-end mt-3">
              <button class="btn btn_agregar_red_social d-flex align-items-center justify-content-center"
                id="btn_agregar_red_social" v-text="$t('programa.buttons.agregarRedSocial')"
                v-on:click="openPopupAgregarProfesor()">
                Agregar profesor
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="seccion_redes_programas" v-if="programa.id">
      <div class="tab">
        <input type="checkbox" id="redes_programa" />
        <label class="tab-label item_acordion" for="redes_programa"
          v-text="$t('programa.formulario.labels.redesPrograma')">Redes Programa</label>
        <div class="tab-content web_tabs">
          <div class="">
            <div class="contenido_listado_redes">
              <div class="row mx-0 archivo_item" v-for="redSocial in redesSocialesPrograma" :key="redSocial.id">
                <div class="col container_name row">
                  <div class="col-sm-auto container_imagen_red_social d-flex align-items-center">
                    <img v-if="redSocial.tablaElementoCatalogo.id === 8" alt="red_social-facebook"
                      src="/content/images/iconos/facebook.png" />
                    <img v-if="redSocial.tablaElementoCatalogo.id === 9" alt="red_social-instagram"
                      src="/content/images/iconos/instagram.png" />
                    <img v-if="redSocial.tablaElementoCatalogo.id === 10" alt="red_social-tiktok"
                      src="/content/images/iconos/tik-tok.png" />
                    <img v-if="redSocial.tablaElementoCatalogo.id === 11" alt="red_social-twitter"
                      src="/content/images/iconos/twitter.png" />
                    <img v-if="redSocial.tablaElementoCatalogo.id === 12" alt="red_social-you-tube"
                      src="/content/images/iconos/youtube.png" />
                  </div>
                  <div class="col container_name d-flex align-items-center">
                    <h3>{{ redSocial.urlRedSocial }}</h3>
                  </div>
                </div>
                <div class="col-sm-auto">
                  <a class="opcion_red_social editar_red" type="button" @click="openPopupEditarRedSocial(redSocial)">
                    <img alt="editar_red_social" src="/content/images/iconos/pencil.png" />
                  </a>
                </div>
              </div>
            </div>
            <div class="d-flex align-items-center justify-content-end mt-3">
              <button class="btn btn_agregar_red_social d-flex align-items-center justify-content-center"
                id="btn_agregar_red_social" v-text="$t('programa.buttons.agregarRedSocial')"
                v-on:click="openPopupAgregarNuevaRedSocial()">
                Agregar red social
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section>
      <b-modal id="modalPopupCrearDocumentoPrograma" ref="modalPopupCrearDocumentoPrograma"
        class="modalPopupCrearDocumentoPrograma">
        <div class="modal-body">
          <div class="">
            <div class="mb-3 container_notas">
              <span v-if="!hasArchivoProgramaPlanEstudio"
                v-text="$t('archivosPrograma.labels.favorSubirPlanEstudio')">Por favor subir el plan de estudio</span>
            </div>
            <a id="" type="button" class="btn btn-upload d-flex align-items-center justify-content-center">
              <label for="file-crear-documento-nuevo" class="row mx-0 align-items-center justify-content-center">
                <div class="">
                  <h4 v-text="$t('programa.popups.buttons.crearDocumentoNuevo')">Subir documento</h4>
                </div>
              </label>
              <input id="file-crear-documento-nuevo" type="file" accept="application/pdf"
                @change="changeAgregarDocumentoNuevo" />
            </a>
            <div class="container_message_error">
              <small>{{ mensajeError }}</small>
            </div>
            <div class="mt-4 contenido_info_documento" v-if="programaDocumentoNuevo.file">
              <div class="row mx-0">
                <div class="col-sm-auto">
                  <h3 v-text="$t('programa.popups.labels.nombreDoc')">Nombre:</h3>
                </div>
                <div class="col">
                  <h3>{{ programaDocumentoNuevo.nombre }}</h3>
                </div>
              </div>
              <div class="row mx-0">
                <div class="col row">
                  <div class="col-sm-auto">
                    <h3 v-text="$t('programa.popups.labels.tipoDoc')">Nombre:</h3>
                  </div>
                  <div class="col">
                    <h3>{{ programaDocumentoNuevo.tipoDocumento | tipoDocumento }}</h3>
                  </div>
                </div>
                <div class="col row">
                  <div class="col-sm-auto">
                    <h3 v-text="$t('programa.popups.labels.sizeDoc')">Nombre:</h3>
                  </div>
                  <div class="col">
                    <h3>{{ programaDocumentoNuevo.size | sieFileToMB }}</h3>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div slot="modal-footer">
          <div class="container_spinner d-flex align-items-center justify-content-center px-3">
            <b-spinner v-if="isArchivoDocumentoNuevoUploaded" type="grow" label="Cargando" variant="danger"></b-spinner>
          </div>
          <button v-if="programaDocumentoNuevo.isValidDoc && !isArchivoDocumentoNuevoUploaded" type="button"
            class="btn btn_subir_documento" id="programaSubirDocumento" data-cy="entitySubirDocumentoButton"
            v-text="$t('entity.action.subir')" v-on:click="subirDocumentoNuevo()">
            Subir
          </button>
          <button type="button" class="btn btn_cancelar_subida" v-text="$t('entity.action.cancel')"
            v-on:click="closePopupCrearDocumentoNuevo()">
            Cancel
          </button>
        </div>
      </b-modal>
      <b-modal id="modalPopupEliminarDocumentoPrograma" ref="modalPopupEliminarDocumentoPrograma">
        <div class="modal-body">
          <div class="modal_eliminar_documento">
            <h4 v-text="$t('archivosPrograma.labels.seguroEliminarDoc', { nameDoc: programaDocumentoNuevo.nombre })">
              Esta seguro que desea eliminar el documento
            </h4>
          </div>
        </div>
        <div slot="modal-footer">
          <div class="container_spinner d-flex align-items-center justify-content-center px-3">
            <b-spinner v-if="isArchivoDocumentoNuevoUploaded" type="grow" label="Cargando" variant="danger"></b-spinner>
          </div>
          <button v-if="!isArchivoDocumentoNuevoUploaded" type="button" class="btn btn_subir_documento"
            id="programaEliminarDocumento" data-cy="entityEliminarDocumentoButton" v-text="$t('entity.action.delete')"
            v-on:click="eliminarArchivoPrograma()">
            Eliminar
          </button>
          <button type="button" class="btn btn_cancelar_subida" v-text="$t('entity.action.cancel')"
            v-on:click="closePopupCrearDocumentoNuevo()">
            Cancel
          </button>
        </div>
      </b-modal>
      <b-modal id="modalPopupAgregarRedSocial" ref="modalPopupAgregarRedSocial">
        <div class="modal-body">
          <form name="formRedSocial" role="form" ref="formulario_redese_sociales_programa">
            <div class="row mx-0">
              <div class="col form-group">
                <label class="form-control-label campo_requerido" for="url_red_social_programa"
                  v-text="$t('programa.popups.labels.urlRedSocial')">Url de la red social</label>
                <input type="text" id="url_red_social_programa" name="url_red_social_programa" class="form-control"
                  placeholder="Eg. redSocial.com/pagina" v-model="redesPrograma.urlRedSocial" required />
                <div v-if="validarUrlRedSocial()">
                  <small class="form-text text-danger" v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                </div>
              </div>
              <div class="col form-group">
                <label class="form-control-label" v-text="$t('programa.popups.labels.tipoResSocial')"
                  for="tipo_red_social">Tabla elemento catalogo</label>
                <select class="form-control" id="tipo_red_social" name="tipo_red_social"
                  v-model="redesPrograma.tablaElementoCatalogo" required>
                  <option v-if="!redesPrograma.tablaElementoCatalogo" selected v-bind:value="null">Seleccione</option>
                  <option v-for="tipoRedSocial in tiposRedSocialElemento" :key="tipoRedSocial.id" v-bind:value="
                    redesPrograma.tablaElementoCatalogo && redesPrograma.tablaElementoCatalogo.id === tipoRedSocial.id
                      ? redesPrograma.tablaElementoCatalogo
                      : tipoRedSocial
                  ">
                    {{ tipoRedSocial.nombre }}
                  </option>
                </select>
                <div v-if="validarTipoRedSocial()">
                  <small class="form-text text-danger" v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                </div>
              </div>
            </div>
          </form>
        </div>
        <div slot="modal-footer">
          <div class="container_spinner d-flex align-items-center justify-content-center px-3">
            <b-spinner v-if="isRedSocialCreatedUptaded" type="grow" label="Cargando" variant="danger"></b-spinner>
          </div>
          <button v-if="!isRedSocialCreatedUptaded" type="button" class="btn btn_subir_documento"
            id="programaRedSocialAgregarActualizar" v-text="$t('entity.action.save')"
            v-on:click="agregarActualizarRedSocial()" :disabled="$v.redesPrograma.$invalid">
            Agregar / Actualizar
          </button>
          <button type="button" class="btn btn_cancelar_subida" v-text="$t('entity.action.cancel')"
            v-on:click="closePopupAgregarRedSocial()">
            Cancel
          </button>
        </div>
      </b-modal>
      <b-modal id="modalPopupAgregarSede" ref="modalPopupAgregarSede">
        <div class="modal-body">
          <div class="">
            <div class="row container_item_sede mx-0 mb-3" v-for="sedeTemp in filtrarSedesConPrograma()"
              :key="sedeTemp.id">
              <div class="col-sm-auto d-flex align-items-center">
                <h3>{{ sedeTemp.nombre }}</h3>
              </div>
              <div class="col d-flex align-items-center">
                <h3>{{ sedeTemp.direccion }}</h3>
              </div>
              <div class="col-sm-auto row mx-0">
                <div class="container_spinner d-flex align-items-center justify-content-center px-3 col">
                  <b-spinner v-if="sedeAgregadaId == sedeTemp.id" type="grow" label="Cargando" variant="danger">
                  </b-spinner>
                </div>
                <div class="col p-0 ">
                  <a type="button" class="opcion_agregar_sede p-3 d-flex justify-content-center align-items-center"
                    v-on:click="agregarSedeToPrograma(sedeTemp)" v-if="sedeAgregadaId != sedeTemp.id">
                    <img alt="imagen_agregar_sede" src="/content/images/iconos/add.png" />
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div slot="modal-footer">
          <!--<button  type="button" class="btn btn_subir_documento"
            id="programaRedSocialAgregarActualizar" v-text="$t('entity.action.save')"
            v-on:click="agregarActualizarRedSocial()">
            Agregar / Actualizar
          </button>-->
          <button type="button" class="btn btn_cancelar_subida" v-text="$t('entity.action.close')"
            v-on:click="closePopupAgregarSede()">
            Cancel
          </button>
        </div>
      </b-modal>
      <b-modal id="modalPopupAgregarProfesores" ref="modalPopupAgregarProfesores">
        <div class="modal-body">
          <div class="">
            <div class="row mx-0">
              <div class="col">
                <input type="text" id="nameFilterProfesorPopup" name="nameFilterProfesorPopup"
                  v-model="nameFilterProfesor" placeholder="Nombre del profesor" class="form-control" />
              </div>
              <div class="col">
                <button type="button" class="btn btn_subir_documento"
                  v-on:click="consultarProfesoresByNameComplete()">Buscar
                  Profesor</button>
              </div>
            </div>
            <div class="d-flex justify-content-center respuesta_vacia" v-if="filtrarProfesoresDelPrograma().length < 1">
              <p>No hay datos</p>
            </div>
            <div class="row container_item_profesor mx-0 mb-3" v-for="profesoresTemp in filtrarProfesoresDelPrograma()"
              :key="profesoresTemp.adminUserDTO.id">
              <div class="col-sm-auto d-flex align-items-center">
                <h3>{{ profesoresTemp.adminUserDTO.nameComplete }}</h3>
              </div>
              <div class="col-sm-auto row mx-0">
                <div class="container_spinner d-flex align-items-center justify-content-center px-3 col">
                  <b-spinner v-if="profesorAgregadoId == profesoresTemp.profesorDTO.id" type="grow" label="Cargando"
                    variant="danger">
                  </b-spinner>
                </div>
                <div class="col p-0 ">
                  <a type="button" class="opcion_agregar_sede p-3 d-flex justify-content-center align-items-center"
                    v-on:click="agregarProfesorToPrograma(profesoresTemp)"
                    v-if="profesorAgregadoId != profesoresTemp.profesorDTO.id">
                    <img alt="imagen_agregar_sede" src="/content/images/iconos/add.png" />
                  </a>
                </div>
              </div>
            </div>
            <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion" v-if="totalItemsProfesor > 0">
              <div class="" style="width: fit-content;">
                <div class="row justify-content-center">
                  <jhi-item-count :page="pageProfesores" :total="queryCountProfesor"
                    :itemsPerPage="itemsPerPagePorfesores">
                  </jhi-item-count>
                </div>
                <div class="row justify-content-center">
                  <b-pagination size="md" :total-rows="totalItemsProfesor" v-model="pageProfesores"
                    :per-page="itemsPerPagePorfesores" :change="loadPageProfesores(pageProfesores)"></b-pagination>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div slot="modal-footer">
          <button type="button" class="btn btn_cancelar_subida" v-text="$t('entity.action.close')"
            v-on:click="closePopupAgregarProfesor()">
            Cancel
          </button>
        </div>
      </b-modal>
    </section>
  </div>
</template>

<script lang="ts" src="./programa-formulario.component.ts"></script>
