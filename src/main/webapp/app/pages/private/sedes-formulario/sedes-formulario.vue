<template >
  <div class="container_sedes_formulario">
    <section class="header_arrow d-flex justify-content-between">
      <div class="d-flex justify-content-start">
        <div class="row mx-0">
          <div class="col-sm-auto px-0 d-flex align-items-center">
            <router-link :to="{ name: 'usuario_sedes_lista' }">
              <img alt="retornar" src="/content/images/iconos/left-arrow-red.png" />
            </router-link>
          </div>
          <div class="col d-flex align-items-center">
            <h2 class="title_formulario_return">Sede</h2>
          </div>
        </div>
      </div>
      <div class="col-sm-auto">
        <button v-if="isModeEdit && !enableEdit" class="btn btn_editar d-flex align-items-center justify-content-center"
          type="button" id="btn_editar" v-text="$t('entity.action.edit')" v-on:click="enableFormularioEditar()">
          Editar
        </button>
      </div>
    </section>
    <section class="sede_formulario">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="guardar()">
        <div class="">
          <div class="">
            <div class="row">
              <div class="col form-group">
                <label class="form-control-label" for="nombre_sede" v-text="$t('sede.formulario.labels.nombreSede')">
                  Nombre de la sede </label>
                <input type="text" id="nombre_sede" name="nombre_sede" class="form-control" placeholder="Eg. Sede Neiva"
                  v-model="$v.sede.nombre.$model" :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.sede.nombre.$anyDirty && $v.sede.nombre.$invalid">
                  <small class="form-text text-danger" v-if="!$v.sede.nombre.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.sede.nombre.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 250 })">
                    This field cannot be longer than 10 characters.
                  </small>
                </div>
              </div>
              <div class="col-3 form-group">
                <label class="form-control-label" v-text="$t('sede.formulario.labels.codigoIndicativo')"></label>
                <input type="text" id="sede_codigo_indicativo" name="sede_codigo_indicativo" class="form-control"
                  placeholder="Eg. SDNV001" v-model="$v.sede.codigoIndicativo.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.sede.codigoIndicativo.$anyDirty && $v.sede.codigoIndicativo.$invalid">
                  <small class="form-text text-danger" v-if="!$v.sede.codigoIndicativo.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.sede.codigoIndicativo.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 20 })">
                    This field cannot be longer than 10 characters.
                  </small>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="form-group col">
                <label class="form-control-label" for="direccion_sede"
                  v-text="$t('sede.formulario.labels.direccion')">Dirección</label>
                <input type="text" id="direccion_sede" name="direccion_sede" class="form-control"
                  placeholder="Eg. Carrera 1H ..." v-model="$v.sede.direccion.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.sede.direccion.$anyDirty && $v.sede.direccion.$invalid">
                  <small class="form-text text-danger" v-if="!$v.sede.direccion.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.sede.direccion.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 250 })">
                    This field cannot be longer than 10 characters.
                  </small>
                </div>
              </div>
              <div class="form-group col">
                <label class="form-control-label" for="sede_latitud"
                  v-text="$t('sede.formulario.labels.latitud')">Latitud</label>
                <input type="number" class="form-control" name="sede_latitud" id="sede_latitud"
                  placeholder="Eg. -12.3444" v-model.number="$v.sede.latitud.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.sede.latitud.$anyDirty && $v.sede.latitud.$invalid">
                  <small class="form-text text-danger" v-if="!$v.sede.latitud.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.sede.latitud.numeric"
                    v-text="$t('entity.validation.number')">
                    This field should be a number.
                  </small>
                </div>
              </div>
              <div class="form-group col">
                <label class="form-control-label" for="sede_longitud"
                  v-text="$t('sede.formulario.labels.longitud')">Longitud</label>
                <input type="number" class="form-control" name="sede_longitud" id="sede_longitud"
                  placeholder="Eg. 75.33333" v-model.number="$v.sede.longitud.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.sede.longitud.$anyDirty && $v.sede.longitud.$invalid">
                  <small class="form-text text-danger" v-if="!$v.sede.longitud.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.sede.longitud.numeric"
                    v-text="$t('entity.validation.number')">
                    This field should be a number.
                  </small>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="form-group col">
                <label class="form-control-label" for="sede_telefono_fijo"
                  v-text="$t('sede.formulario.labels.telefonoFijo')">telefono fijo</label>
                <input type="number" class="form-control" name="sede_telefono_fijo" id="sede_telefono_fijo"
                  placeholder="Eg. 8765478" v-model="$v.sede.telefonoFijo.$model"
                  :disabled="checkHabilitacionCampos()" />
                <div v-if="$v.sede.telefonoFijo.$anyDirty && $v.sede.telefonoFijo.$invalid">
                  <small class="form-text text-danger" v-if="!$v.sede.telefonoFijo.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 7 })">
                    This field cannot be longer than 7 characters.
                  </small>
                </div>
              </div>
              <div class="form-group col">
                <label class="form-control-label" for="sede_telefono_celular"
                  v-text="$t('sede.formulario.labels.telefonoCelular')"></label>
                <input type="number" class="form-control" name="sede_telefono_celular" id="sede_telefono_celular"
                  placeholder="Eg. 3104449345" v-model="$v.sede.telefonoCelular.$model"
                  :disabled="checkHabilitacionCampos()" />
                <div v-if="$v.sede.telefonoCelular.$anyDirty && $v.sede.telefonoCelular.$invalid">
                  <small class="form-text text-danger" v-if="!$v.sede.telefonoCelular.maxLength"
                    v-text="$t('entity.validation.maxlength', { max: 10 })">
                    This field cannot be longer than 10 characters.
                  </small>
                </div>
              </div>
              <div class="form-group col">
                <label class="form-control-label" v-text="$t('sede.formulario.labels.email')"></label>
                <input type="text" class="form-control" name="sede_email" id="sede_email"
                  placeholder="Eg. prueba@correo.com" v-model="$v.sede.correoElectronico.$model"
                  :disabled="checkHabilitacionCampos()" required />
                <div v-if="$v.sede.correoElectronico.$anyDirty && $v.sede.correoElectronico.$invalid">
                  <small class="form-text text-danger" v-if="!$v.sede.correoElectronico.required"
                    v-text="$t('entity.validation.required')">
                    This field is required.
                  </small>
                  <small class="form-text text-danger" v-if="!$v.sede.correoElectronico.validEmail"
                    v-text="$t('entity.validation.email')">
                    This field is required.
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
                  :disabled="$v.sede.$invalid || isSaving">
                  Guardar
                </button>
              </div>
            </div>
          </section>
        </div>
      </form>
    </section>
    <section class="map_continer">
      <div class="row mx-0">
        <div class="col form-group">
          <label class="form-control-label">Ciudad</label>
          <select class="form-control" @change="changeCoordenadas($event)">
            <option value="0">0</option>
            <option value="1">1</option>
            <option value="2">2</option>
          </select>
        </div>
      </div>
      <l-map v-if="showMap" ref="map" style="height: 250px; width: 100%" :zoom="zoomMap"
         @click="clickMap">
        <l-tile-layer :url="urlMap" :attribution="copyrightMap" />
      </l-map>
    </section>
  </div>
</template>

<script lang="ts" src="./sedes-formulario.component.ts"></script>
