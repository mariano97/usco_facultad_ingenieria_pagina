<template>
  <div class="container-fragment-eventos my-5">
    <h2 class="title" v-text="$t('fragments.fragment-eventos.title')">Eventos</h2>
    <div class="container-swiper-eventos" v-if="eventos.length > 0">
      <swiper :slides-per-view="3.5" :space-between="30" :loop="false" :navigation="true"
        :breakpoints="swiperOptions.breakpoints">
        <swiper-slide v-for="evento in eventos" :key="evento.id" class="slide-swiper">
          <div class="card-container-eventos">
            <div class="imagen">
              <img :src="obtenerImagen(evento.urlFoto) | headerFileBase64" />
            </div>
            <div class="body">
              <div class="conatiner-title-eventos">
                <h2 class="title">{{ evento.titulo }}</h2>
              </div>
              <div class="container-lugar mb-2 row mx-0" v-if="evento.lugar">
                <div class="col-sm-auto p-0">
                  <img alt="map" src="/content/images/iconos/map.png" />
                </div>
                <div class="col-sm-auto d-flex align-items-center">
                  <h4 class="text-lugar" v-text="$t('fragments.fragment-eventos.text-lugar')">Lugar:</h4>
                </div>
                <div class="col p-0 d-flex align-items-center">
                  <h4 class="ubicacion">{{ evento.lugar }}</h4>
                </div>
              </div>
              <div class="container-fecha mb-2 row mx-0" v-if="evento.fecha">
                <div class="col-sm-auto p-0">
                  <img alt="map" src="/content/images/iconos/calendar.png" />
                </div>
                <div class="col-sm-auto d-flex align-items-center">
                  <h4 class="text-fecha" v-text="$t('fragments.fragment-eventos.text-fecha')">Fecha:</h4>
                </div>
                <div class="col p-0 d-flex align-items-center">
                  <h4 class="fecha">{{ convertDateTimeFromServer(evento.fecha) }}</h4>
                </div>
              </div>
            </div>
            <div class="boton-ver-mas d-flex justify-content-center align-items-center">
              <router-link :to="{ name: 'evento_info_public', params: { eventoId: evento.id } }">
                <button class="btn btn-ver-mas d-flex justify-content-center"
                  v-text="$t('fragments.fragment-eventos.ver-mas')">
                  Ver más
                </button>
              </router-link>
            </div>
          </div>
        </swiper-slide>
      </swiper>
    </div>
    <div class="container-ver-todo mt-5 row mx-0">
      <div class="col p-0 d-flex align-items-center">
        <hr />
      </div>
      <div class="col-sm-auto p-0 d-flex align-items-center">
        <router-link :to="{ name: 'eventos_lista_public' }">
          <button class="btn btn-ver-todo d-flex justify-content-center align-items-center"
            v-text="$t('fragments.fragment-eventos.ver-todo')">Ver todo</button>
        </router-link>
      </div>
      <div class="col p-0 d-flex align-items-center">
        <hr />
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./eventos-fragment.component.ts"></script>
