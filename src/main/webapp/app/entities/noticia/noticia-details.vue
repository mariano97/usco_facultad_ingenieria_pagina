<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="noticia">
        <h2 class="jh-entity-heading" data-cy="noticiaDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.detail.title')">Noticia</span> {{ noticia.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.titulo')">Titulo</span>
          </dt>
          <dd>
            <span>{{ noticia.titulo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.sintesis')">Sintesis</span>
          </dt>
          <dd>
            <span>{{ noticia.sintesis }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.cuerpoDescripcion')">Cuerpo Descripcion</span>
          </dt>
          <dd>
            <span>{{ noticia.cuerpoDescripcion }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.fecha')">Fecha</span>
          </dt>
          <dd>
            <span v-if="noticia.fecha">{{ $d(Date.parse(noticia.fecha), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.urlFoto')">Url Foto</span>
          </dt>
          <dd>
            <span>{{ noticia.urlFoto }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.noticia.facultad')">Facultad</span>
          </dt>
          <dd>
            <div v-if="noticia.facultad">
              <router-link :to="{ name: 'FacultadView', params: { facultadId: noticia.facultad.id } }">{{
                noticia.facultad.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="noticia.id" :to="{ name: 'NoticiaEdit', params: { noticiaId: noticia.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./noticia-details.component.ts"></script>
