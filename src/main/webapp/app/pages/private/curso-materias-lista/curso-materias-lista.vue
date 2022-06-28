<template>
  <div class="container_curso_materias_lista">
    <section>
      <div class="d-flex justify-content-between">
        <div class="">
          <h2 class="title-page" v-text="$t('paginas.privado.materias-lista.title-page')"></h2>
        </div>
        <div class="">
          <router-link :to="{ name: 'curso_materias_descripcion' }">
            <button class="btn btn-crear-materia d-flex align-items-center justify-content-center"
              v-text="$t('paginas.privado.materias-lista.buttons.button-crear')"></button>
          </router-link>
        </div>
      </div>
    </section>
    <section class="tabla-resultados">
      <div class="">
        <table class="table">
          <thead class="thead-dark">
            <tr>
              <th scope="row" v-on:click="changeOrder('nombre')">
                <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.nombre')">Nombre</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'">
                </jhi-sort-indicator>
              </th>
              <th scope="row" v-on:click="changeOrder('numeroCreditos')">
                <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.numeroCreditos')">Numero
                  Creditos</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numeroCreditos'">
                </jhi-sort-indicator>
              </th>
              <th scope="row" v-on:click="changeOrder('semestreImpartida')">
                <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.semestreImpartida')">Semestre
                  Impartida</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'semestreImpartida'">
                </jhi-sort-indicator>
              </th>
              <th scope="row" v-on:click="changeOrder('nivelAcademico.nombre')">
                <span v-text="$t('paginaFacultadIngenieriaProyectoApp.cursoMateria.nivelAcademico')">Nivel
                  Academico</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nivelAcademico.nombre'">
                </jhi-sort-indicator>
              </th>
            </tr>
          </thead>
          <tbody>
            <router-link v-for="materia in cursoMateriasLista" :key="materia.id"
              :to="{ name: 'curso_materias_descripcion_updated', params: { materiaId: materia.id } }" tag="tr">
              <td>{{ materia.nombre }}</td>
              <td>{{ materia.numeroCreditos + ' Creditos' }}</td>
              <td>{{ materia.semestreImpartida + ' Semestre' }}</td>
              <td>{{ materia.nivelAcademico ? materia.nivelAcademico.nombre : '' }}</td>
            </router-link>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion"
        v-show="cursoMateriasLista && cursoMateriasLista.length > 0">
        <div class="" style="width: fit-content;">
          <div class="row justify-content-center">
            <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
          </div>
          <div class="row justify-content-center">
            <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"
              :change="loadPage(page)"></b-pagination>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./curso-materias-lista.component.ts"></script>
