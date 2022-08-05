<template>
  <div class="container_evento_facultad">
    <section class="seccion_imagen_principal">
      <div class="">
        <img alt="imagen_principal" :src="obtenerImagen(evento.urlFoto) | headerFileBase64" />
      </div>
    </section>
    <section class="seccion_info_evento container">
      <div class="">
        <div class="d-flex justify-content-center align-items-center">
          <div class="container_titulo_evento horizontal-divides-red-2 ">
            <h1>{{ evento.titulo }}</h1>
          </div>
        </div>
        <div class="mt-5 row mx-0 container_info_evento_desc">
          <div class="col">
            <p>{{ evento.cuerpo }}</p>
          </div>
          <div class="col d-flex justify-content-center align-items-center">
            <div class="container_info_evento d-flex justify-content-center align-items-center">
              <div class="">
                <div class="row mx-0 mb-3" v-if="evento.lugar">
                  <div class="col-sm-auto px-1">
                    <img src="/content/images/iconos/map.png" />
                  </div>
                  <div class="col-sm-auto px-1 d-flex align-items-center">
                    <h4 v-text="$t('fragments.fragment-eventos.text-lugar')"></h4>
                  </div>
                  <div class="col p-0 d-flex align-items-center">
                    <h3 class="text-value">{{ evento.lugar }}</h3>
                  </div>
                </div>
                <div class="row mx-0 mb-3" v-if="evento.fecha">
                  <div class="col-sm-auto px-1">
                    <img src="/content/images/iconos/calendar.png" />
                  </div>
                  <div class="col-sm-auto px-1 d-flex align-items-center">
                    <h4 v-text="$t('fragments.fragment-eventos.text-fecha')"></h4>
                  </div>
                  <div class="col p-0 d-flex align-items-center">
                    <h3 class="text-value">{{ convertDateTimeFromServer(evento.fecha) }}</h3>
                  </div>
                </div>
                <!--<div class="mt-4 d-flex justify-content-center align-items-center">

                </div>-->
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./evento-info.component.ts"></script>
