<template>
  <div class="container_eventos_facultad">
    <section class="section_title_eventos">
      <div class="container">
        <div class="d-flex justify-content-center">
          <div class="container-title_eventos horizontal-divides-red-2">
            <h1>Eventos</h1>
          </div>
        </div>
      </div>
    </section>
    <section class="section_listado_otras_eventos">
      <div class="">
        <div class="col d-flex align-items-center justify-content-center no_resultados" v-if="eventosArrayListado.length < 1">
          <p>Los sentimos, no hallamos resultados</p>
        </div>
        <div class="container_resultados_eventos row mx-0 my-3" v-for="(eventosArray, index) in eventosArrayListado"
          :key="index">
          <div class="col d-flex justify-content-center align-items-center" v-for="(evento, index2) in eventosArray"
            :key="index2">
            <div class="container-resultado row mx-0 d-flex justify-content-center align-items-center" v-if="evento.id">
              <div class="col-sm-9">
                <div class="card-container-evento">
                  <div class="imagen">
                    <img :src="obtenerImagen(evento.urlFoto) | headerFileBase64" />
                  </div>
                  <div class="body">
                    <div class="container_fecha mb-2">
                      <h2 class="fecha">{{ convertDateTimeFromServer(evento.fecha) }}</h2>
                    </div>
                    <div class="conatiner-title-evento">
                      <h2 class="title">{{ evento.titulo }}</h2>
                    </div>
                    <div class="container-desc-evento">
                      <p>{{ evento.cuerpo }}</p>
                    </div>
                  </div>
                  <div class="boton-ver-mas d-flex justify-content-center align-items-center">
                    <router-link :to="{ name: 'evento_info_public', params: { eventoId: evento.id } }">
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
        <div class="d-flex justify-content-end pr-4 mt-3 content-paginacion" v-if="totalItems > 0">
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
      </div>
    </section>
  </div>
</template>
<script lang="ts" src="./eventos-lista.component.ts"></script>
