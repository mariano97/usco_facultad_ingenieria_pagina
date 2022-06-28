<template>
  <div class="profesores_lista_container">
    <section>
      <div class="d-flex justify-content-between">
        <div class="">
          <h2 class="title-page" v-text="$t('paginas.privado.profesores-lista.title-page')"></h2>
        </div>
        <div class="">
          <router-link :to="{ name: 'usuario_profesores_crear'  }">
            <button class="btn btn-crear-profesor d-flex align-items-center justify-content-center"
              v-text="$t('paginas.privado.profesores-lista.buttons.button-crear')"></button>
          </router-link>
        </div>
      </div>
    </section>
    <section class="tabla-resultados">
      <div class="">
        <table class="table">
          <thead class="thead-dark">
            <tr>
              <th scope="col" v-on:click="changeOrder('email')">
                <span v-text="$t('userManagement.email')">Email</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'">
                </jhi-sort-indicator>
              </th>
              <th scope="col" v-on:click="changeOrder('nameComplete')">
                <span v-text="$t('userManagement.nameComplete')">Email</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameComplete'">
                </jhi-sort-indicator>
              </th>
              <th scope="col">
                <span v-text="$t('userManagement.roles')"></span>
              </th>
            </tr>
          </thead>
          <tbody>
            <router-link v-for="usuario in usuarios" :key="usuario.id"
              :to="{ name: 'usuario_profesores_descripcion', params: { userLogin: usuario.login } }" tag="tr">
              <td>{{ usuario.email }}</td>
              <td>{{ usuario.nameComplete }}</td>
              <td>{{ generateRolesString(usuario.authorities) }}</td>
            </router-link>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion" v-show="usuarios && usuarios.length > 0">
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
<script lang="ts" src="./profesores-lista.component.ts"></script>
