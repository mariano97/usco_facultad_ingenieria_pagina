<template>
  <div class="container_semillaros_facultad">
    <section>
      <div class="d-flex justify-content-between">
        <div class="">
          <h2 class="title-page">Lista de Semilleros</h2>
        </div>
        <div class="">
          <router-link :to="{ name: 'semillero_formulario' }">
            <button class="btn btn-crear-semillero d-flex align-items-center justify-content-center">
              Crear Semillero
            </button>
          </router-link>
        </div>
      </div>
    </section>
    <section class="tabla-resultados">
      <div class="tabla_container">
        <table class="table">
          <thead class="thead-dark">
            <tr>
              <th scope="row" v-on:click="changeOrder('nombre')">
                <span>Nombre</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'">
                </jhi-sort-indicator>
              </th>
            </tr>
          </thead>
          <tbody>
            <router-link v-for="semillero in semilleros" :key="semillero.id" data-cy="programa" tag="tr"
              :to="{ name: 'semillero_formulario_descripcion', params: { semilleroId: semillero.id } }">
              <td>{{ semillero.nombre }}</td>
            </router-link>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion" v-show="semilleros && semilleros.length > 0">
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
<script lang="ts" src="./semilleros-lista.component.ts"></script>
