<template>
  <div class="container-fragment-eventos my-5">
    <h2 class="title" v-text="$t('fragments.fragment-eventos.title')">Eventos</h2>
    <div class="container-swiper-eventos">
      <swiper
        :slides-per-view="3.5"
        :space-between="30"
        :loop="false"
        :navigation="true"
      >
        <swiper-slide
          v-for="n in 10"
          :key="n"
          class="slide-swiper"
        >
          <div class="card-container-eventos">
            <div class="imagen">
              <img
                :src="getImageUrl(30+n)"
              />
            </div>
            <div class="body">
              <div class="conatiner-title-eventos">
                <h2 class="title">Title Noticia</h2>
              </div>
              <div class="container-lugar mb-2 row mx-0">
                <div class="col-sm-auto p-0">
                  <img
                    alt="map"
                    src="/content/images/iconos/map.png"
                  />
                </div>
                <div class="col-sm-auto d-flex align-items-center">
                  <h4 class="text-lugar" v-text="$t('fragments.fragment-eventos.text-lugar')">Lugar:</h4>
                </div>
                <div class="col p-0 d-flex align-items-center">
                  <h4 class="ubicacion">lugar/ubicacion</h4>
                </div>
              </div>
              <div class="container-fecha mb-2 row mx-0">
                <div class="col-sm-auto p-0">
                  <img
                    alt="map"
                    src="/content/images/iconos/calendar.png"
                  />
                </div>
                <div class="col-sm-auto d-flex align-items-center">
                  <h4 class="text-fecha" v-text="$t('fragments.fragment-eventos.text-fecha')">Fecha:</h4>
                </div>
                <div class="col p-0 d-flex align-items-center">
                  <h4 class="fecha">DD/MM/AAAA</h4>
                </div>
              </div>
            </div>
            <div class="boton-ver-mas d-flex justify-content-center align-items-center">
              <router-link :to="{ name: 'JhiUserView' }">
                <button class="btn btn-ver-mas d-flex justify-content-center" v-text="$t('fragments.fragment-eventos.ver-mas')">
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
        <router-link to="/">
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
