<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="paginaFacultadIngenieriaProyectoApp.programa.home.createOrEditLabel"
          data-cy="ProgramaCreateUpdateHeading"
          v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.home.createOrEditLabel')"
        >
          Create or edit a Programa
        </h2>
        <div>
          <div class="form-group" v-if="programa.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="programa.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nombre')" for="programa-nombre"
              >Nombre</label
            >
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="programa-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.programa.nombre.$invalid, invalid: $v.programa.nombre.$invalid }"
              v-model="$v.programa.nombre.$model"
              required
            />
            <div v-if="$v.programa.nombre.$anyDirty && $v.programa.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.programa.nombre.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.codigoSnies')"
              for="programa-codigoSnies"
              >Codigo Snies</label
            >
            <input
              type="number"
              class="form-control"
              name="codigoSnies"
              id="programa-codigoSnies"
              data-cy="codigoSnies"
              :class="{ valid: !$v.programa.codigoSnies.$invalid, invalid: $v.programa.codigoSnies.$invalid }"
              v-model.number="$v.programa.codigoSnies.$model"
              required
            />
            <div v-if="$v.programa.codigoSnies.$anyDirty && $v.programa.codigoSnies.$invalid">
              <small class="form-text text-danger" v-if="!$v.programa.codigoSnies.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.programa.codigoSnies.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.codigoRegistroCalificado')"
              for="programa-codigoRegistroCalificado"
              >Codigo Registro Calificado</label
            >
            <input
              type="number"
              class="form-control"
              name="codigoRegistroCalificado"
              id="programa-codigoRegistroCalificado"
              data-cy="codigoRegistroCalificado"
              :class="{ valid: !$v.programa.codigoRegistroCalificado.$invalid, invalid: $v.programa.codigoRegistroCalificado.$invalid }"
              v-model.number="$v.programa.codigoRegistroCalificado.$model"
              required
            />
            <div v-if="$v.programa.codigoRegistroCalificado.$anyDirty && $v.programa.codigoRegistroCalificado.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.programa.codigoRegistroCalificado.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.programa.codigoRegistroCalificado.numeric"
                v-text="$t('entity.validation.number')"
              >
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.fechaRegistroCalificado')"
              for="programa-fechaRegistroCalificado"
              >Fecha Registro Calificado</label
            >
            <div class="d-flex">
              <input
                id="programa-fechaRegistroCalificado"
                data-cy="fechaRegistroCalificado"
                type="datetime-local"
                class="form-control"
                name="fechaRegistroCalificado"
                :class="{ valid: !$v.programa.fechaRegistroCalificado.$invalid, invalid: $v.programa.fechaRegistroCalificado.$invalid }"
                required
                :value="convertDateTimeFromServer($v.programa.fechaRegistroCalificado.$model)"
                @change="updateInstantField('fechaRegistroCalificado', $event)"
              />
            </div>
            <div v-if="$v.programa.fechaRegistroCalificado.$anyDirty && $v.programa.fechaRegistroCalificado.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.programa.fechaRegistroCalificado.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.programa.fechaRegistroCalificado.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nombreTituloOtorgado')"
              for="programa-nombreTituloOtorgado"
              >Nombre Titulo Otorgado</label
            >
            <input
              type="text"
              class="form-control"
              name="nombreTituloOtorgado"
              id="programa-nombreTituloOtorgado"
              data-cy="nombreTituloOtorgado"
              :class="{ valid: !$v.programa.nombreTituloOtorgado.$invalid, invalid: $v.programa.nombreTituloOtorgado.$invalid }"
              v-model="$v.programa.nombreTituloOtorgado.$model"
              required
            />
            <div v-if="$v.programa.nombreTituloOtorgado.$anyDirty && $v.programa.nombreTituloOtorgado.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.programa.nombreTituloOtorgado.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.numeroCreditos')"
              for="programa-numeroCreditos"
              >Numero Creditos</label
            >
            <input
              type="number"
              class="form-control"
              name="numeroCreditos"
              id="programa-numeroCreditos"
              data-cy="numeroCreditos"
              :class="{ valid: !$v.programa.numeroCreditos.$invalid, invalid: $v.programa.numeroCreditos.$invalid }"
              v-model.number="$v.programa.numeroCreditos.$model"
              required
            />
            <div v-if="$v.programa.numeroCreditos.$anyDirty && $v.programa.numeroCreditos.$invalid">
              <small class="form-text text-danger" v-if="!$v.programa.numeroCreditos.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.programa.numeroCreditos.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.duracionPrograma')"
              for="programa-duracionPrograma"
              >Duracion Programa</label
            >
            <input
              type="number"
              class="form-control"
              name="duracionPrograma"
              id="programa-duracionPrograma"
              data-cy="duracionPrograma"
              :class="{ valid: !$v.programa.duracionPrograma.$invalid, invalid: $v.programa.duracionPrograma.$invalid }"
              v-model.number="$v.programa.duracionPrograma.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.presentacionPrograma')"
              for="programa-presentacionPrograma"
              >Presentacion Programa</label
            >
            <input
              type="text"
              class="form-control"
              name="presentacionPrograma"
              id="programa-presentacionPrograma"
              data-cy="presentacionPrograma"
              :class="{ valid: !$v.programa.presentacionPrograma.$invalid, invalid: $v.programa.presentacionPrograma.$invalid }"
              v-model="$v.programa.presentacionPrograma.$model"
              required
            />
            <div v-if="$v.programa.presentacionPrograma.$anyDirty && $v.programa.presentacionPrograma.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.programa.presentacionPrograma.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.mision')" for="programa-mision"
              >Mision</label
            >
            <input
              type="text"
              class="form-control"
              name="mision"
              id="programa-mision"
              data-cy="mision"
              :class="{ valid: !$v.programa.mision.$invalid, invalid: $v.programa.mision.$invalid }"
              v-model="$v.programa.mision.$model"
              required
            />
            <div v-if="$v.programa.mision.$anyDirty && $v.programa.mision.$invalid">
              <small class="form-text text-danger" v-if="!$v.programa.mision.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.vision')" for="programa-vision"
              >Vision</label
            >
            <input
              type="text"
              class="form-control"
              name="vision"
              id="programa-vision"
              data-cy="vision"
              :class="{ valid: !$v.programa.vision.$invalid, invalid: $v.programa.vision.$invalid }"
              v-model="$v.programa.vision.$model"
              required
            />
            <div v-if="$v.programa.vision.$anyDirty && $v.programa.vision.$invalid">
              <small class="form-text text-danger" v-if="!$v.programa.vision.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilEstudiante')"
              for="programa-perfilEstudiante"
              >Perfil Estudiante</label
            >
            <input
              type="text"
              class="form-control"
              name="perfilEstudiante"
              id="programa-perfilEstudiante"
              data-cy="perfilEstudiante"
              :class="{ valid: !$v.programa.perfilEstudiante.$invalid, invalid: $v.programa.perfilEstudiante.$invalid }"
              v-model="$v.programa.perfilEstudiante.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilEgresado')"
              for="programa-perfilEgresado"
              >Perfil Egresado</label
            >
            <input
              type="text"
              class="form-control"
              name="perfilEgresado"
              id="programa-perfilEgresado"
              data-cy="perfilEgresado"
              :class="{ valid: !$v.programa.perfilEgresado.$invalid, invalid: $v.programa.perfilEgresado.$invalid }"
              v-model="$v.programa.perfilEgresado.$model"
              required
            />
            <div v-if="$v.programa.perfilEgresado.$anyDirty && $v.programa.perfilEgresado.$invalid">
              <small class="form-text text-danger" v-if="!$v.programa.perfilEgresado.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilOcupacional')"
              for="programa-perfilOcupacional"
              >Perfil Ocupacional</label
            >
            <input
              type="text"
              class="form-control"
              name="perfilOcupacional"
              id="programa-perfilOcupacional"
              data-cy="perfilOcupacional"
              :class="{ valid: !$v.programa.perfilOcupacional.$invalid, invalid: $v.programa.perfilOcupacional.$invalid }"
              v-model="$v.programa.perfilOcupacional.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.urlFotoPrograma')"
              for="programa-urlFotoPrograma"
              >Url Foto Programa</label
            >
            <input
              type="text"
              class="form-control"
              name="urlFotoPrograma"
              id="programa-urlFotoPrograma"
              data-cy="urlFotoPrograma"
              :class="{ valid: !$v.programa.urlFotoPrograma.$invalid, invalid: $v.programa.urlFotoPrograma.$invalid }"
              v-model="$v.programa.urlFotoPrograma.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.dirigidoAQuien')"
              for="programa-dirigidoAQuien"
              >Dirigido A Quien</label
            >
            <input
              type="text"
              class="form-control"
              name="dirigidoAQuien"
              id="programa-dirigidoAQuien"
              data-cy="dirigidoAQuien"
              :class="{ valid: !$v.programa.dirigidoAQuien.$invalid, invalid: $v.programa.dirigidoAQuien.$invalid }"
              v-model="$v.programa.dirigidoAQuien.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.costoPrograma')"
              for="programa-costoPrograma"
              >Costo Programa</label
            >
            <input
              type="number"
              class="form-control"
              name="costoPrograma"
              id="programa-costoPrograma"
              data-cy="costoPrograma"
              :class="{ valid: !$v.programa.costoPrograma.$invalid, invalid: $v.programa.costoPrograma.$invalid }"
              v-model.number="$v.programa.costoPrograma.$model"
              required
            />
            <div v-if="$v.programa.costoPrograma.$anyDirty && $v.programa.costoPrograma.$invalid">
              <small class="form-text text-danger" v-if="!$v.programa.costoPrograma.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.programa.costoPrograma.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.estado')" for="programa-estado"
              >Estado</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="estado"
              id="programa-estado"
              data-cy="estado"
              :class="{ valid: !$v.programa.estado.$invalid, invalid: $v.programa.estado.$invalid }"
              v-model="$v.programa.estado.$model"
              required
            />
            <div v-if="$v.programa.estado.$anyDirty && $v.programa.estado.$invalid">
              <small class="form-text text-danger" v-if="!$v.programa.estado.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nivelFormacion')"
              for="programa-nivelFormacion"
              >Nivel Formacion</label
            >
            <select
              class="form-control"
              id="programa-nivelFormacion"
              data-cy="nivelFormacion"
              name="nivelFormacion"
              v-model="programa.nivelFormacion"
              required
            >
              <option v-if="!programa.nivelFormacion" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  programa.nivelFormacion && tablaElementoCatalogoOption.id === programa.nivelFormacion.id
                    ? programa.nivelFormacion
                    : tablaElementoCatalogoOption
                "
                v-for="tablaElementoCatalogoOption in tablaElementoCatalogos"
                :key="tablaElementoCatalogoOption.id"
              >
                {{ tablaElementoCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.programa.nivelFormacion.$anyDirty && $v.programa.nivelFormacion.$invalid">
            <small class="form-text text-danger" v-if="!$v.programa.nivelFormacion.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.tipoFormacion')"
              for="programa-tipoFormacion"
              >Tipo Formacion</label
            >
            <select
              class="form-control"
              id="programa-tipoFormacion"
              data-cy="tipoFormacion"
              name="tipoFormacion"
              v-model="programa.tipoFormacion"
              required
            >
              <option v-if="!programa.tipoFormacion" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  programa.tipoFormacion && tablaElementoCatalogoOption.id === programa.tipoFormacion.id
                    ? programa.tipoFormacion
                    : tablaElementoCatalogoOption
                "
                v-for="tablaElementoCatalogoOption in tablaElementoCatalogos"
                :key="tablaElementoCatalogoOption.id"
              >
                {{ tablaElementoCatalogoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.programa.tipoFormacion.$anyDirty && $v.programa.tipoFormacion.$invalid">
            <small class="form-text text-danger" v-if="!$v.programa.tipoFormacion.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.facultad')" for="programa-facultad"
              >Facultad</label
            >
            <select class="form-control" id="programa-facultad" data-cy="facultad" name="facultad" v-model="programa.facultad" required>
              <option v-if="!programa.facultad" v-bind:value="null" selected></option>
              <option
                v-bind:value="programa.facultad && facultadOption.id === programa.facultad.id ? programa.facultad : facultadOption"
                v-for="facultadOption in facultads"
                :key="facultadOption.id"
              >
                {{ facultadOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.programa.facultad.$anyDirty && $v.programa.facultad.$invalid">
            <small class="form-text text-danger" v-if="!$v.programa.facultad.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.sede')" for="programa-sede">Sede</label>
            <select
              class="form-control"
              id="programa-sedes"
              data-cy="sede"
              multiple
              name="sede"
              v-if="programa.sedes !== undefined"
              v-model="programa.sedes"
            >
              <option v-bind:value="getSelected(programa.sedes, sedeOption)" v-for="sedeOption in sedes" :key="sedeOption.id">
                {{ sedeOption.nombre }}
              </option>
            </select>
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
            :disabled="$v.programa.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./programa-update.component.ts"></script>
