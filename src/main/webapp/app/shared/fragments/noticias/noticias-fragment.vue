<template>
  <div class="container-fragment-noticias my-5">
    <h2 class="title" v-text="$t('fragments.fragment-noticias.title')"></h2>
    <div class="container-swiper-noticias">
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
          <div class="card-container-noticias">
            <div class="imagen">
              <img
                :src="getImageUrl(34+n)"
              />
            </div>
            <div class="body">
              <div class="container-fecha mb-2">
                <h4 class="fecha">DD/MM/AAAA</h4>
              </div>
              <div class="conatiner-title-noticias">
                <h2 class="title">Title Noticia</h2>
              </div>
              <div class="container-desc-noticia">
                <p>Lorem ipsum dolor sit amet consectetur adipiscing elit facilisi magna hendrerit justo ultrices lectus, velit cubilia pharetra condimentum hac natoque commodo cum donec cras integer. </p>
              </div>
            </div>
            <div class="boton-ver-mas d-flex justify-content-center align-items-center">
              <router-link :to="{ name: 'JhiUserView' }">
                <button class="btn btn-ver-mas d-flex justify-content-center" v-text="$t('fragments.fragment-noticias.ver-mas')">
                  Ver máss
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
            v-text="$t('fragments.fragment-noticias.ver-todo')">Ver todo</button>
        </router-link>
      </div>
      <div class="col p-0 d-flex align-items-center">
        <hr />
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./noticias-fragment.component.ts"></script>
