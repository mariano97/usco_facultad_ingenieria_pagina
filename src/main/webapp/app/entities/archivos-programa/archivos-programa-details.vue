<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="archivosPrograma">
        <h2 class="jh-entity-heading" data-cy="archivosProgramaDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.detail.title')">ArchivosPrograma</span>
          {{ archivosPrograma.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.urlName')">Url Name</span>
          </dt>
          <dd>
            <span>{{ archivosPrograma.urlName }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.generationStorage')">Generation Storage</span>
          </dt>
          <dd>
            <span>{{ archivosPrograma.generationStorage }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.storageContentType')">Storage Content Type</span>
          </dt>
          <dd>
            <span>{{ archivosPrograma.storageContentType }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.tipoDocumento')">Tipo Documento</span>
          </dt>
          <dd>
            <span>{{ archivosPrograma.tipoDocumento }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.planEstudio')">Plan Estudio</span>
          </dt>
          <dd>
            <span>{{ archivosPrograma.planEstudio }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.nombreArchivo')">Nombre Archivo</span>
          </dt>
          <dd>
            <span>{{ archivosPrograma.nombreArchivo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.programa')">Programa</span>
          </dt>
          <dd>
            <div v-if="archivosPrograma.programa">
              <router-link :to="{ name: 'ProgramaView', params: { programaId: archivosPrograma.programa.id } }">{{
                archivosPrograma.programa.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.archivosPrograma.tablaElementoCatalogo')">Tabla Elemento Catalogo</span>
          </dt>
          <dd>
            <div v-if="archivosPrograma.tablaElementoCatalogo">
              <router-link
                :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: archivosPrograma.tablaElementoCatalogo.id } }"
                >{{ archivosPrograma.tablaElementoCatalogo.nombre }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="archivosPrograma.id"
          :to="{ name: 'ArchivosProgramaEdit', params: { archivosProgramaId: archivosPrograma.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./archivos-programa-details.component.ts"></script>
