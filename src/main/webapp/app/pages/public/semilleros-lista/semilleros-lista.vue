<template>
  <div class="container_semilleros_facultad container">
    <section class="section_title_semillero">
      <div class="container">
        <div class="d-flex justify-content-center">
          <div class="container-title_semilleros horizontal-divides-red-2">
            <h1>Semilleros</h1>
          </div>
        </div>
      </div>
    </section>
    <section class="section_listado_semillero">
      <div class="">
        <div class="col d-flex align-items-center justify-content-center no_resultados" v-if="semillerosArrayLista.length < 1">
          <p>Los sentimos, no hallamos resultados</p>
        </div>
        <div class="container_resultados_semilleros row mx-0 my-3"
          v-for="(semilleroArray, index) in semillerosArrayLista" :key="index">
          <div class="col d-flex justify-content-center align-items-center"
            v-for="(semillero, index2) in semilleroArray" :key="index2">
            <div class="container-resultado row mx-0 d-flex justify-content-center align-items-center"
              v-if="semillero.id">
              <div class="col-sm-9">
                <div class="card-container-semillero">
                  <div class="imagen">
                    <img :src="obtenerImagen(semillero.urlFoto) | headerFileBase64" />
                  </div>
                  <div class="body">
                    <div class="container_titulo mb-2 d-flex justify-content-center align-items-center mb-2">
                      <h2 class="title">{{ semillero.nombre }}</h2>
                    </div>
                    <div class="container_descripcion">
                      <p class="desc">{{ semillero.informacionGeneral }}</p>
                    </div>
                  </div>
                  <div class="boton-ver-mas d-flex justify-content-center align-items-center">
                    <router-link :to="{ name: 'semillero_info_public', params: { semilleroId: semillero.id } }">
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
<script lang="ts" src="./semilleros-lista.component.ts"></script>
