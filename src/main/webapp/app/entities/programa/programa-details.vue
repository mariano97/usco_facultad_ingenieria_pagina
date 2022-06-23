<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="programa">
        <h2 class="jh-entity-heading" data-cy="programaDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.detail.title')">Programa</span> {{ programa.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ programa.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.codigoSnies')">Codigo Snies</span>
          </dt>
          <dd>
            <span>{{ programa.codigoSnies }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.codigoRegistroCalificado')">Codigo Registro Calificado</span>
          </dt>
          <dd>
            <span>{{ programa.codigoRegistroCalificado }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.fechaRegistroCalificado')">Fecha Registro Calificado</span>
          </dt>
          <dd>
            <span v-if="programa.fechaRegistroCalificado">{{ $d(Date.parse(programa.fechaRegistroCalificado), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nombreTituloOtorgado')">Nombre Titulo Otorgado</span>
          </dt>
          <dd>
            <span>{{ programa.nombreTituloOtorgado }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.numeroCreditos')">Numero Creditos</span>
          </dt>
          <dd>
            <span>{{ programa.numeroCreditos }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.duracionPrograma')">Duracion Programa</span>
          </dt>
          <dd>
            <span>{{ programa.duracionPrograma }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.presentacionPrograma')">Presentacion Programa</span>
          </dt>
          <dd>
            <span>{{ programa.presentacionPrograma }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.mision')">Mision</span>
          </dt>
          <dd>
            <span>{{ programa.mision }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.vision')">Vision</span>
          </dt>
          <dd>
            <span>{{ programa.vision }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilEstudiante')">Perfil Estudiante</span>
          </dt>
          <dd>
            <span>{{ programa.perfilEstudiante }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilEgresado')">Perfil Egresado</span>
          </dt>
          <dd>
            <span>{{ programa.perfilEgresado }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.perfilOcupacional')">Perfil Ocupacional</span>
          </dt>
          <dd>
            <span>{{ programa.perfilOcupacional }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.urlFotoPrograma')">Url Foto Programa</span>
          </dt>
          <dd>
            <span>{{ programa.urlFotoPrograma }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.dirigidoAQuien')">Dirigido A Quien</span>
          </dt>
          <dd>
            <span>{{ programa.dirigidoAQuien }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.costoPrograma')">Costo Programa</span>
          </dt>
          <dd>
            <span>{{ programa.costoPrograma }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.estado')">Estado</span>
          </dt>
          <dd>
            <span>{{ programa.estado }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.emailContacto')">Email Contacto</span>
          </dt>
          <dd>
            <span>{{ programa.emailContacto }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.nivelFormacion')">Nivel Formacion</span>
          </dt>
          <dd>
            <div v-if="programa.nivelFormacion">
              <router-link :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: programa.nivelFormacion.id } }">{{
                programa.nivelFormacion.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.tipoFormacion')">Tipo Formacion</span>
          </dt>
          <dd>
            <div v-if="programa.tipoFormacion">
              <router-link :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: programa.tipoFormacion.id } }">{{
                programa.tipoFormacion.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.facultad')">Facultad</span>
          </dt>
          <dd>
            <div v-if="programa.facultad">
              <router-link :to="{ name: 'FacultadView', params: { facultadId: programa.facultad.id } }">{{
                programa.facultad.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.sede')">Sede</span>
          </dt>
          <dd>
            <span v-for="(sede, i) in programa.sedes" :key="sede.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'SedeView', params: { sedeId: sede.id } }">{{ sede.nombre }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.programa.profesor')">Profesor</span>
          </dt>
          <dd>
            <span v-for="(profesor, i) in programa.profesors" :key="profesor.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ProfesorView', params: { profesorId: profesor.id } }">{{ profesor.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="programa.id" :to="{ name: 'ProgramaEdit', params: { programaId: programa.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./programa-details.component.ts"></script>
