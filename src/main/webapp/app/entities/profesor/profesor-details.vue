<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="profesor">
        <h2 class="jh-entity-heading" data-cy="profesorDetailsHeading">
          <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.detail.title')">Profesor</span> {{ profesor.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.emailAlternativo')">Email Alternativo</span>
          </dt>
          <dd>
            <span>{{ profesor.emailAlternativo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.activo')">Activo</span>
          </dt>
          <dd>
            <span>{{ profesor.activo }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.perfil')">Perfil</span>
          </dt>
          <dd>
            <span>{{ profesor.perfil }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.telefonoCelular')">Telefono Celular</span>
          </dt>
          <dd>
            <span>{{ profesor.telefonoCelular }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.oficina')">Oficina</span>
          </dt>
          <dd>
            <span>{{ profesor.oficina }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.userId')">User Id</span>
          </dt>
          <dd>
            <span>{{ profesor.userId }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.urlFotoProfesor')">Url Foto Profesor</span>
          </dt>
          <dd>
            <span>{{ profesor.urlFotoProfesor }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.tituloAcademico')">Titulo Academico</span>
          </dt>
          <dd>
            <span>{{ profesor.tituloAcademico }}</span>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.tablaElementoCatalogo')">Tabla Elemento Catalogo</span>
          </dt>
          <dd>
            <div v-if="profesor.tablaElementoCatalogo">
              <router-link
                :to="{ name: 'TablaElementoCatalogoView', params: { tablaElementoCatalogoId: profesor.tablaElementoCatalogo.id } }"
                >{{ profesor.tablaElementoCatalogo.nombre }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.facultad')">Facultad</span>
          </dt>
          <dd>
            <div v-if="profesor.facultad">
              <router-link :to="{ name: 'FacultadView', params: { facultadId: profesor.facultad.id } }">{{
                profesor.facultad.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('paginaFacultadIngenieriaProyectoApp.profesor.cursoMateria')">Curso Materia</span>
          </dt>
          <dd>
            <span v-for="(cursoMateria, i) in profesor.cursoMaterias" :key="cursoMateria.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'CursoMateriaView', params: { cursoMateriaId: cursoMateria.id } }">{{
                cursoMateria.id
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="profesor.id" :to="{ name: 'ProfesorEdit', params: { profesorId: profesor.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./profesor-details.component.ts"></script>
