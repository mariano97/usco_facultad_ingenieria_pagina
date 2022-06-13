<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="redesPrograma">
        <h2 class="jh-entity-heading" data-cy="redesProgramaDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.detail.title')">RedesPrograma</span> {{ redesPrograma.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.urlRedSocial')">Url Red Social</span>
          </dt>
          <dd>
            <span>{{ redesPrograma.urlRedSocial }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.programa')">Programa</span>
          </dt>
          <dd>
            <div v-if="redesPrograma.programa">
              <router-link :to="{ name: 'ProgramaView', params: { programaId: redesPrograma.programa.id } }">{{
                redesPrograma.programa.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.redesPrograma.tablaElementoCatalogo')">Tabla Elemento Catalogo</span>
          </dt>
          <dd>
            <div v-if="redesPrograma.tablaElementoCatalogo">
              <router-link
                :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: redesPrograma.tablaElementoCatalogo.id } }"
                >{{ redesPrograma.tablaElementoCatalogo.nombre }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="redesPrograma.id"
          :to="{ name: 'RedesProgramaEdit', params: { redesProgramaId: redesPrograma.id } }"
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

<script lang="ts" src="./redes-programa-details.component.ts"></script>
