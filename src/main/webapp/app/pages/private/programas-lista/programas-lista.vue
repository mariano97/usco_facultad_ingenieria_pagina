<template >
  <div class="container-usuario-programas-lista">
    <section>
      <div class="d-flex justify-content-between">
        <div class="">
          <h2 class="title-page" v-text="$t('paginas.privado.programas-lista.title-page')">Lista de programas</h2>
        </div>
        <div class="">
          <router-link :to="{ name: 'usuario_crear_programa', params: { accion: constantCrearPrograma } }">
            <button class="btn btn-crear-programa d-flex align-items-center justify-content-center" v-text="$t('paginas.privado.programas-lista.buttons.button-crear')">
              Crear Programa
            </button>
          </router-link>
        </div>
      </div>
    </section>
    <section class="tabla-resultados">
      <div class="">
        <table class="table" aria-describedby="programas">
          <thead class="thead-dark">
            <tr>
              <th scope="row">
                <span v-text="$t('paginas.privado.programas-lista.tabla.nombre-programa')">Nombre programa</span>
              </th>
              <th scope="row">
                <span v-text="$t('paginas.privado.programas-lista.tabla.codigo-snies')">SNIES</span>
              </th>
              <th scope="row">
                <span v-text="$t('paginas.privado.programas-lista.tabla.nivel-formacion')">nivel-formacion</span>
              </th>
            </tr>
          </thead>
          <tbody>
            <router-link
              v-for="programa in programasList" :key="programa.id" data-cy="programa"
              tag="tr"
              :to="{ name: 'usuario_programa_descripcion', params: { programaId: programa.id, accion: constantEditarPrograma } }"
            >
              <td>{{ programa.nombre }}</td>
              <td>{{ programa.codigoSnies }}</td>
              <td>{{ programa.nivelFormacion.nombre }}</td>
            </router-link>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion" v-show="programasList && programasList.length > 0">
        <div class="" style="width: fit-content;">
          <div class="row justify-content-center">
            <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
          </div>
          <div class="row justify-content-center">
            <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts" src="./programas-lista.component.ts"></script>
