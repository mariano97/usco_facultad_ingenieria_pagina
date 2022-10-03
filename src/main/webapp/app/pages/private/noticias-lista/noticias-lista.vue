<template>
  <div class="container_noticias_facultad">
    <section>
      <div class="d-flex justify-content-between">
        <div class="">
          <h2 class="title-page">Lista de Noticias</h2>
        </div>
        <div class="">
          <router-link :to="{ name: 'noticias_formulario' }">
            <button class="btn btn-crear-noticia d-flex align-items-center justify-content-center">
              Crear Noticia
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
              <!--<th scope="row" v-on:click="changeOrder('id')">
                <span>Id Noticia</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'">
                </jhi-sort-indicator>
              </th>-->
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
            <router-link v-for="noticia in noticias" :key="noticia.id" data-cy="programa" tag="tr"
              :to="{ name: 'noticias_formulario_descripcion', params: { noticiaId: noticia.id } }">
              <!--<td>{{ noticia.id }}</td>-->
              <td>{{ noticia.titulo }}</td>
              <td>{{ dateSplit(noticia.fecha ? $d(Date.parse(noticia.fecha), 'short') : '') }}</td>
            </router-link>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion" v-show="noticias && noticias.length > 0">
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
<script lang="ts" src="./noticias-lista.component.ts"></script>
