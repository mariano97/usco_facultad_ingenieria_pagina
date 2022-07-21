<template>
  <div class="container_noticia_facultad container">
    <section class="seccion_informacion_principal">
      <div class="row mx-0 desktop">
        <div class="col conatiner_info_basic">
          <div class="container_title">
            <h1 class="title">{{ noticia.titulo }}</h1>
          </div>
          <div class="container_fecha horizontal-divides-red-2">
            <h4 class="fecha">{{ convertDateTimeFromServer(noticia.fecha) }}</h4>
          </div>
          <div class="container_sintesis mt-4">
            <p>{{ noticia.sintesis }}</p>
          </div>
        </div>
        <div class="col container_imagen">
          <img alt="imagen_noticia" :src="obtenerImagen(noticia.urlFoto) | headerFileBase64" />
        </div>
      </div>
    </section>
    <section class="seccion_cuerpo_noticia">
      <div class="">
        <div class="conatiner_cuerpo_descripcion">
          <p>{{ noticia.cuerpoDescripcion }}</p>
        </div>
      </div>
    </section>
    <section class="seccion_otras_noticias" v-if="otrasNoticias.length > 0">
      <div class="container_titulo d-flex justify-content-center align-items-center">
        <h2 class="title">Otras Noticias</h2>
      </div>
      <div class="">
        <div class="container-swiper-noticias" v-if="otrasNoticias.length > 0">
          <swiper :slides-per-view="3.5" :space-between="30" :loop="false" :navigation="true"
            :breakpoints="swiperOptions.breakpoints">
            <swiper-slide v-for="noticiaTemp in otrasNoticias" :key="noticiaTemp.id" class="slide-swiper">
              <div class="card-container-noticias">
                <div class="imagen">
                  <img :src="obtenerImagen(noticiaTemp.urlFoto) | headerFileBase64" />
                </div>
                <div class="body">
                  <div class="container-fecha mb-2">
                    <h4 class="fecha">{{ convertDateTimeFromServer(noticiaTemp.fecha) }}</h4>
                  </div>
                  <div class="conatiner-title-noticias">
                    <h2 class="title">{{ noticiaTemp.titulo }}</h2>
                  </div>
                  <div class="container-desc-noticia">
                    <p>{{ noticiaTemp.sintesis }}</p>
                  </div>
                </div>
                <div class="boton-ver-mas d-flex justify-content-center align-items-center">
                  <a :href="'/faultad-ingenieria/noticia-info/' + noticiaTemp.id">
                    <button class="btn btn-ver-mas d-flex justify-content-center"
                      v-text="$t('fragments.fragment-noticias.ver-mas')">
                      Ver máss
                    </button>
                  </a>
                </div>
              </div>
            </swiper-slide>
          </swiper>
        </div>
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./noticia-info.component.ts"></script>
