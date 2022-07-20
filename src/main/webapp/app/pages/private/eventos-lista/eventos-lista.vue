<template>
  <div class="container_eventos_facultad">
    <section>
      <div class="d-flex justify-content-between">
        <div class="">
          <h2 class="title-page">Lista de Eventos</h2>
        </div>
        <div class="">
          <router-link :to="{ name: 'evento_formulario' }">
            <button class="btn btn-crear-eventos d-flex align-items-center justify-content-center">
              Crear Evento
            </button>
          </router-link>
        </div>
      </div>
    </section>
    <section class="tabla-resultados">
      <div class="">
        <table class="table">
          <thead class="thead-dark">
            <tr>
              <th scope="row" v-on:click="changeOrder('titulo')">
                <span>titulo</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'titulo'">
                </jhi-sort-indicator>
              </th>
              <th scope="row" v-on:click="changeOrder('fecha')">
                <span>Fecha</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'">
                </jhi-sort-indicator>
              </th>
            </tr>
          </thead>
          <tbody>
            <router-link v-for="evento in eventos" :key="evento.id" data-cy="programa" tag="tr"
              :to="{ name: 'evento_formulario_descripcion', params: { eventoId: evento.id } }">
              <td>{{ evento.titulo }}</td>
              <td>{{ dateSplit(evento.fecha ? $d(Date.parse(evento.fecha), 'short') : '') }}</td>
            </router-link>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion" v-show="eventos && eventos.length > 0">
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
<script lang="ts" src="./eventos-lista.component.ts"></script>
