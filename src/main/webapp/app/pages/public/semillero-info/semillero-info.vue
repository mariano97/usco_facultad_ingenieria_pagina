<template>
  <div class="container_semillero_facultad">
    <section class="seccion_imagen_principal">
      <div class="">
        <img alt="imagen_principal" :src="obtenerImagen(semillero.urlFoto) | headerFileBase64" />
      </div>
    </section>
    <section class="seccion_info_semillero container">
      <div class="">
        <div class="container_nombre_semillero d-flex justify-content-center align-items-center">
          <h1 class="title">{{ semillero.nombre }}</h1>
        </div>
        <div class="container_descripcion">
          <p>{{ semillero.informacionGeneral }}</p>
        </div>
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./semillero-info.component.ts"></script>
