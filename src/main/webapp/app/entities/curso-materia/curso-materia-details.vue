<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cursoMateria">
        <h2 class="jh-entity-heading" data-cy="cursoMateriaDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.detail.title')">CursoMateria</span> {{ cursoMateria.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.nombre')">Nombre</span>
          </dt>
          <dd>
            <span>{{ cursoMateria.nombre }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.numeroCreditos')">Numero Creditos</span>
          </dt>
          <dd>
            <span>{{ cursoMateria.numeroCreditos }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.semestreImpartida')">Semestre Impartida</span>
          </dt>
          <dd>
            <span>{{ cursoMateria.semestreImpartida }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.nivelAcademico')">Nivel Academico</span>
          </dt>
          <dd>
            <div v-if="cursoMateria.nivelAcademico">
              <router-link
                :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: cursoMateria.nivelAcademico.id } }"
                >{{ cursoMateria.nivelAcademico.nombre }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.programa')">Programa</span>
          </dt>
          <dd>
            <span v-for="(programa, i) in cursoMateria.programas" :key="programa.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ProgramaView', params: { programaId: programa.id } }">{{ programa.nombre }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="cursoMateria.id"
          :to="{ name: 'CursoMateriaEdit', params: { cursoMateriaId: cursoMateria.id } }"
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

<script lang="ts" src="./curso-materia-details.component.ts"></script>
