<template>
  <div class="container_laboratorio_facultad">
    <section class="seccion_imagen_principal">
      <div class="">
        <img alt="imagen_principal" :src="obtenerImagen(laboratorio.urlFoto) | headerFileBase64" />
      </div>
    </section>
    <section class="seccion_info_laboratorio container">
      <div class="">
        <div class="container_nombre_laboratorio d-flex justify-content-center align-items-center">
          <h1 class="title">{{ laboratorio.nombre }}</h1>
        </div>
        <div class="container_info_contacto row mx-0 mt-4">
          <div class="col" v-if="laboratorio.direccion">
            <h3>Dirección / Ubicación</h3>
            <p>{{ laboratorio.direccion }}</p>
          </div>
          <div class="col" v-if="laboratorio.correoContacto">
            <h3>Correo electronico</h3>
            <p>{{ laboratorio.correoContacto }}</p>
          </div>
        </div>
        <div class="container_descripcion col">
          <h3>Descripción</h3>
          <p>{{ laboratorio.informacionGeneral }}</p>
        </div>
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./laboratorio-info.component.ts"></script>
