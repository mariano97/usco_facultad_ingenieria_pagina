<template>
  <div class="sedes_lista_conatiner">
    <section>
      <div class="d-flex justify-content-between">
        <div class="">
          <h2 class="title-page" v-text="$t('paginas.privado.sedes-lista.title-page')"></h2>
        </div>
        <div class="">
          <router-link :to="{ name: 'usuario_crear_sede', params: { accion: constantCrearPrograma } }">
            <button
              class="btn btn-crear-sede d-flex align-items-center justify-content-center"
              v-text="$t('paginas.privado.sedes-lista.buttons.button-crear')"
            ></button>
          </router-link>
        </div>
      </div>
    </section>
    <section class="tabla-resultados">
      <div class="">
        <table class="table" aria-describedby="sedes">
          <thead class="thead-dark">
            <tr>
              <th scope="row" v-on:click="changeOrder('nombre')">
                <span v-text="$t('paginas.privado.sedes-lista.tabla.nombre-sede')"></span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
              </th>
              <th scope="row" v-on:click="changeOrder('direccion')">
                <span v-text="$t('paginas.privado.sedes-lista.tabla.direccion-sede')"></span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'direccion'"></jhi-sort-indicator>
              </th>
              <th scope="row" v-on:click="changeOrder('telefonoFijo')">
                <span v-text="$t('paginas.privado.sedes-lista.tabla.telefono-sede')"></span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'telefonoFijo'"></jhi-sort-indicator>
              </th>
              <th scope="row" v-on:click="changeOrder('correoElectronico')">
                <span v-text="$t('paginas.privado.sedes-lista.tabla.email')"></span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'correoElectronico'"></jhi-sort-indicator>
              </th>
            </tr>
          </thead>
          <tbody>
            <router-link v-for="sede in sedes" :key="sede.id" data-cy="sede" tag="tr" :to="{ name: 'usuario_sedes_descripcion', params: { sedeId: sede.codigoIndicativo, accion: constantEditarPrograma } }">
              <td>{{ sede.nombre }}</td>
              <td>{{ sede.direccion }}</td>
              <td>{{ sede.telefonoFijo | formatTelefonoFijo }}</td>
              <td>{{ sede.correoElectronico }}</td>
            </router-link>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion" v-show="sedes && sedes.length > 0">
        <div class="" style="width: fit-content;">
          <div class="row justify-content-center">
            <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
          </div>
          <div class="row justify-content-center">
            <b-pagination
              size="md"
              :total-rows="totalItems"
              v-model="page"
              :per-page="itemsPerPage"
              :change="loadPage(page)"
            ></b-pagination>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts" src="./sedes-lista.component.ts"></script>
