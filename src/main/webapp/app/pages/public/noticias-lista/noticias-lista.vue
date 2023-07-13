<template>
  <div class="container_noticias_facultad">
    <section class="section_title_noticias">
      <div class="container">
        <div class="d-flex justify-content-center">
          <div class="container-title_carateristicas horizontal-divides-red-2">
            <h1>Noticias</h1>
          </div>
        </div>
      </div>
    </section>
    <section class="section_ultima_noticia">
      <div class="">
        <div class="row mx-0">
          <div class="col container_imagen_noticia">
            <img :src="obtenerImagen(ultimaNoticia.urlFoto) | headerFileBase64" />
          </div>
          <div class="col container_info_noticia">
            <div class="mb-3">
              <h1 class="titulo_noticias">{{ ultimaNoticia.titulo }}</h1>
            </div>
            <div class="container_fecha mb-3">
              <h4 class="fecha">{{ convertDateTimeFromServer(ultimaNoticia.fecha) }}</h4>
            </div>
            <div class="container_sintesis mb-3">
              <p class="m-0">{{ ultimaNoticia.sintesis }}</p>
            </div>
            <div class="container_btn_ver_mas mt-5 d-flex justify-content-end align-content-center">
              <router-link :to="{ name: 'noticia_info_public', params: { noticiaId: ultimaNoticia.id } }">
                <button class="btn btn_ver_mas d-flex align-items-center justify-content-center">
                  Ver más
                </button>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="section_listado_otras_noticias" v-if="noticiasListado.length > 0">
      <div class="">
        <div class="container_resultados_noticias row mx-0 my-3"
          v-for="(noticiasArray, index) in noticiasArrayListado" :key="index">
          <div class="col d-flex justify-content-center align-items-center" v-for="(noticia, index2) in noticiasArray"
            :key="index2">
            <div class="container-resultado row mx-0 d-flex justify-content-center align-items-center"
              v-if="noticia.id">
              <div class="col-sm-9">
                <div class="card-container-noticias">
                  <div class="imagen">
                    <img :src="obtenerImagen(noticia.urlFoto) | headerFileBase64" />
                  </div>
                  <div class="body">
                    <div class="container_fecha mb-2">
                      <h2 class="fecha">{{ convertDateTimeFromServer(noticia.fecha) }}</h2>
                    </div>
                    <div class="conatiner-title-noticias">
                      <h2 class="title">{{ noticia.titulo }}</h2>
                    </div>
                    <div class="container-desc-noticia">
                      <p>{{ noticia.sintesis }}</p>
                    </div>
                  </div>
                  <div class="boton-ver-mas d-flex justify-content-center align-items-center">
                    <router-link :to="{ name: 'noticia_info_public', params: { noticiaId: noticia.id } }">
                      <button class="btn btn-ver-mas d-flex justify-content-center"
                        v-text="$t('fragments.fragment-noticias.ver-mas')">
                        Ver máss
                      </button>
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./noticias-lista.component.ts"></script>
