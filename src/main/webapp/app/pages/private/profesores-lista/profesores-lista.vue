<template>
  <div class="profesores_lista_container">
    <section>
      <div class="d-flex justify-content-between">
        <div class="">
          <h2 class="title-page" v-text="$t('paginas.privado.profesores-lista.title-page')"></h2>
        </div>
        <div class="">
          <router-link>
            <button class="btn btn-crear-profesor d-flex align-items-center justify-content-center"
              v-text="$t('paginas.privado.sedes-lista.buttons.button-crear')"></button>
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
              <th scope="col" v-on:click="changeOrder('firstName')">
                <span v-text="$t('userManagement.firstName')">Email</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'firstName'">
                </jhi-sort-indicator>
              </th>
              <th scope="col" v-on:click="changeOrder('lastName')">
                <span v-text="$t('userManagement.lastName')">Email</span>
                <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastName'">
                </jhi-sort-indicator>
              </th>
            </tr>
          </thead>
          <tbody>
            <router-link v-for="usuario in usuarios" :key="usuario.id" :to="{ name: 'usuario_profesores_lista' }"
              tag="tr">
              <td>{{ usuario.email }}</td>
              <td>{{ usuario.firstName }}</td>
              <td>{{ usuario.lastName }}</td>
            </router-link>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./profesores-lista.component.ts"></script>
