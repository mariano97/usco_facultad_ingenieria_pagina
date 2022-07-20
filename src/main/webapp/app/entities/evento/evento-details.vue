<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="evento">
        <h2 class="jh-entity-heading" data-cy="eventoDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.detail.title')">Evento</span> {{ evento.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.titulo')">Titulo</span>
          </dt>
          <dd>
            <span>{{ evento.titulo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.cuerpo')">Cuerpo</span>
          </dt>
          <dd>
            <span>{{ evento.cuerpo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.fecha')">Fecha</span>
          </dt>
          <dd>
            <span v-if="evento.fecha">{{ $d(Date.parse(evento.fecha), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.hora')">Hora</span>
          </dt>
          <dd>
            <span v-if="evento.hora">{{ $d(Date.parse(evento.hora), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.lugar')">Lugar</span>
          </dt>
          <dd>
            <span>{{ evento.lugar }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.urlFoto')">Url Foto</span>
          </dt>
          <dd>
            <span>{{ evento.urlFoto }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.evento.facultad')">Facultad</span>
          </dt>
          <dd>
            <div v-if="evento.facultad">
              <router-link :to="{ name: 'FacultadView', params: { facultadId: evento.facultad.id } }">{{
                evento.facultad.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="evento.id" :to="{ name: 'EventoEdit', params: { eventoId: evento.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./evento-details.component.ts"></script>
