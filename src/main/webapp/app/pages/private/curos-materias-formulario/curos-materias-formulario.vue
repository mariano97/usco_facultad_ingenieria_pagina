<template>
  <div class="container_formulario_curso_materia">
    <section class="header_arrow d-flex justify-content-between">
      <div class="d-flex justify-content-start">
        <div class="row mx-0">
          <div class="col-sm-auto px-0 d-flex align-items-center">
            <router-link :to="{ name: 'curso_materias_lista' }" class="">
              <img alt="retornar" src="/content/images/iconos/left-arrow-red.png" />
            </router-link>
          </div>
          <div class="col d-flex align-items-center">
            <h2 class="title_formulario_return">Materia</h2>
          </div>
        </div>
      </div>
      <div class="col-sm-auto row mx-0">
        <button
          v-if="isModeEdit && !enableEdit"
          class="btn btn_editar d-flex align-items-center justify-content-center col-sm-auto"
          type="button"
          id="btn_editar"
          v-text="$t('entity.action.edit')"
          v-on:click="enableFormularioEditar()"
        >
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
    <section class="materia_formulario">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="guardar()">
        <div class="">
          <div class="">
            <div class="row mx-0">
              <div class="col form-group px-0">
                <label class="form-control-label campo_requerido" for="nombre" v-text="$t('materias.formulario.labels.nombre')"> </label>
                <input
                  type="text"
                  id="nombre"
                  name="nombre"
                  class="form-control"
                  placeholder="Eg. Análisis experimental"
                  v-model="$v.cursoMateria.nombre.$model"
                  :disabled="checkHabilitacionCampos()"
                  required
                />
                <div class="" v-if="$v.cursoMateria.nombre.$anyDirty && $v.cursoMateria.nombre.$invalid">
                  <small class="form-text text-danger" v-if="!$v.cursoMateria.nombre.required" v-text="$t('entity.validation.required')">
                    Este campo es obligatorio.
                  </small>
                </div>
              </div>
            </div>
            <div class="row mx-0">
              <div class="col form-group px-0 row mx-0">
                <div class="col">
                  <label
                    class="form-control-label campo_requerido"
                    for="numeroCreditos"
                    v-text="$t('materias.formulario.labels.numeroCreditos')"
                  >
                  </label>
                  <input
                    type="number"
                    id="numeroCreditos"
                    name="numeroCreditos"
                    class="form-control"
                    placeholder="Eg. 12"
                    v-model="$v.cursoMateria.numeroCreditos.$model"
                    :disabled="checkHabilitacionCampos()"
                    required
                  />
                  <div class="" v-if="$v.cursoMateria.numeroCreditos.$anyDirty && $v.cursoMateria.numeroCreditos.$invalid">
                    <small
                      class="form-text text-danger"
                      v-if="!$v.cursoMateria.numeroCreditos.required"
                      v-text="$t('entity.validation.required')"
                    >
                      Este campo es obligatorio.
                    </small>
                  </div>
                </div>
                <div class="col-sm-auto d-flex align-items-end">
                  <h4>Creditos</h4>
                </div>
              </div>
              <div class="col form-group px-0 row mx-0">
                <div class="col">
                  <label
                    class="form-control-label campo_requerido"
                    for="semestreImpartida"
                    v-text="$t('materias.formulario.labels.semestreImpartida')"
                  >
                  </label>
                  <input
                    type="text"
                    id="semestreImpartida"
                    name="semestreImpartida"
                    class="form-control"
                    placeholder="Eg. 1"
                    v-model="$v.cursoMateria.semestreImpartida.$model"
                    :disabled="checkHabilitacionCampos()"
                    required
                  />
                  <div class="" v-if="$v.cursoMateria.semestreImpartida.$anyDirty && $v.cursoMateria.semestreImpartida.$invalid">
                    <small
                      class="form-text text-danger"
                      v-if="!$v.cursoMateria.semestreImpartida.required"
                      v-text="$t('entity.validation.required')"
                    >
                      Este campo es obligatorio.
                    </small>
                  </div>
                </div>
                <div class="col-sm-auto d-flex align-items-end">
                  <h4>Semestre</h4>
                </div>
              </div>
              <div class="col form-group px-0">
                <label
                  class="form-control-label campo_requerido"
                  for="nivelAcademico"
                  v-text="$t('materias.formulario.labels.nivelAcademico')"
                >
                </label>
                <select
                  class="form-control"
                  id="nivelAcademico"
                  data-cy="nivelAcademico"
                  name="nivelAcademico"
                  v-model="cursoMateria.nivelAcademico"
                  :disabled="checkHabilitacionCampos()"
                  required
                >
                  <option v-if="!cursoMateria.nivelAcademico" v-bind:value="null" selected></option>
                  <option
                    v-bind:value="
                      cursoMateria.nivelAcademico && tablaElementoCatalogoOption.id === cursoMateria.nivelAcademico.id
                        ? cursoMateria.nivelAcademico
                        : tablaElementoCatalogoOption
                    "
                    v-for="tablaElementoCatalogoOption in listNivelesAcademicos"
                    :key="tablaElementoCatalogoOption.id"
                  >
                    {{ tablaElementoCatalogoOption.nombre }}
                  </option>
                </select>
                <div class="" v-if="$v.cursoMateria.nivelAcademico.$anyDirty && $v.cursoMateria.nivelAcademico.$invalid">
                  <small
                    class="form-text text-danger"
                    v-if="!$v.cursoMateria.nivelAcademico.required"
                    v-text="$t('entity.validation.required')"
                  >
                    Este campo es obligatorio.
                  </small>
                </div>
              </div>
              <!--<div class="col form-group pr-0">
                <label
                  class="form-control-label "
                  for="programa"
                >
                  Programa
                </label>
                <select
                  class="form-control"
                  id="programa"
                  data-cy="programa"
                  name="programa"
                  v-model="cursoMateria.programas"
                  :disabled="checkHabilitacionCampos()"
                  required
                >
                  <option v-if="!cursoMateria.programas" v-bind:value="null" selected></option>
                  <option
                    v-bind:value="
                      cursoMateria.programas && programa.id === cursoMateria.programas.id
                        ? cursoMateria.programas
                        : programa
                    "
                    v-for="programa in programasFacultad"
                    :key="programa.id"
                  >
                    {{ programa.nombre }}
                  </option>
                </select>-->
                <!----<div class="" v-if="$v.cursoMateria.programas.$anyDirty && $v.cursoMateria.programas.$invalid">
                  <small
                    class="form-text text-danger"
                    v-if="!$v.cursoMateria.programas.required"
                    v-text="$t('entity.validation.required')"
                  >
                    Este campo es obligatorio.
                  </small>
                </div>-->
              <!---</div>-->
            </div>
          </div>
          <section class="buttons_formulario">
            <div class="row mx-0">
              <div class="col-sm-auto">
                <button
                  v-if="enableEdit"
                  class="btn btn_cancel d-flex align-items-center justify-content-center"
                  type="button"
                  id="btn_cancel"
                  v-text="$t('entity.action.cancel')"
                  v-on:click="cancelarAccion()"
                >
                  Cancelar
                </button>
              </div>
              <div class="col-sm-auto">
                <button
                  v-if="enableEdit"
                  class="btn btn_guardar d-flex align-items-center justify-content-center"
                  type="submit"
                  id="btn_guardar"
                  v-text="$t('entity.action.save')"
                  :disabled="$v.cursoMateria.$invalid || isSaving"
                >
                  Guardar
                </button>
              </div>
            </div>
          </section>
        </div>
      </form>
    </section>
    <section class="seccion_programas mt-3" v-if="cursoMateria.id">
      <div class="tab">
        <input type="checkbox" id="cursoMateria_programa" />
        <label class="tab-label item_acordion" for="cursoMateria_programa"
          >Programas</label>
        <div class="tab-content web_tabs">
          <div class="">
            <div class="contenido_listado_programas">
              <div class="row mx-0 acordion_item mb-3" v-for="programaTemp in cursoMateria.programas" :key="programaTemp.id">
                <div class="col-sm-auto container_nombre" >
                  <h3>{{ programaTemp.nombre }}</h3>
                </div>
                <div class="col-sm-auto d-flex align-items-center container_option_list">
                  <a type="button" class="btn btn_opcion_item d-flex justify-content-center align-items-center"
                    v-on:click="eliminarProgramaToCursoMaterias(programaTemp)">
                    <img alt="eliminar_sede_programa" src="/content/images/iconos/minus.png" />
                  </a>
                </div>
              </div>
            </div>
            <div class="d-flex align-items-center justify-content-end mt-3">
              <button class="btn btn_agregar_data d-flex align-items-center justify-content-center"
                id="btn_agregar_data" v-on:click="openPopupAgregarPrograma()">
                Agregar programa
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section>
      <b-modal id="modalPopupAgregarPrograma" ref="modalPopupAgregarPrograma">
        <div class="modal-body">
          <div class="">
            <div class="row mx-0 align-items-center justify-content-center">
              <div class="">
                <input
                  type="text"
                  class="form-control"
                  id="filterPrograma"
                  name="filterPrograma"
                  placeholder="Eg. Ingenieria"
                  v-model="modelFilterPorgrama"
                  @keyup="filterProgramas"
                />
              </div>
            </div>
            <div class="d-flex justify-content-center respuesta_vacia" v-if="programasFacultadFilter.length < 1">
              <p>No hay datos</p>
            </div>
            <div class="row container_item_popup mx-0 mb-3" v-for="programaTemp in programasFacultadFilter" :key="programaTemp.id">
              <div class="col-sm-auto d-flex align-items-center">
                <h3>{{ programaTemp.nombre }}</h3>
              </div>
              <div class="col-sm-auto row mx-0">
                <div class="container_spinner d-flex align-items-center justify-content-center px-3 col">
                  <b-spinner v-if="programaAgregadoId == programaTemp.id" type="grow" label="Cargando"
                    variant="danger">
                  </b-spinner>
                </div>
                <div class="col p-0 ">
                  <a type="button" class="opcion_agregar_sede p-3 d-flex justify-content-center align-items-center"
                    v-on:click="agregarProgramaToCursoMateria(programaTemp)"
                    v-if="programaAgregadoId != programaTemp.id">
                    <img alt="imagen_agregar_sede" src="/content/images/iconos/add.png" />
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div slot="modal-footer">
          <button type="button" class="btn btn_cancelar_subida" v-text="$t('entity.action.close')"
            v-on:click="closePopupAgregarPrograma()">
            Cancel
          </button>
        </div>
      </b-modal>
    </section>
  </div>
</template>
<script lang="ts" src="./curos-materias-formulario.component.ts"></script>
