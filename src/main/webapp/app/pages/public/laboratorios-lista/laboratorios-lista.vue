<template>
  <div class="container_laboratorios_facultad container">
    <section class="section_title_laboratorio">
      <div class="container">
        <div class="d-flex justify-content-center">
          <div class="container-title_laboratorios horizontal-divides-red-2">
            <h1>Laboratorios</h1>
          </div>
        </div>
      </div>
    </section>
    <section class="section_listado_laboratorio">
      <div class="">
        <div class="col d-flex align-items-center justify-content-center no_resultados" v-if="laboratoriosArrayLista.length < 1">
          <p>Los sentimos, no hallamos resultados</p>
        </div>
        <div class="container_resultados_laboratorios row mx-0 my-3"
          v-for="(laboratorioArray, index) in laboratoriosArrayLista" :key="index">
          <div class="col d-flex justify-content-center align-items-center"
            v-for="(laboratorio, index2) in laboratorioArray" :key="index2">
            <div class="container-resultado row mx-0 d-flex justify-content-center align-items-center"
              v-if="laboratorio.id">
              <div class="col-sm-9">
                <div class="card-container-laboratorio">
                  <div class="imagen">
                    <img :src="obtenerImagen(laboratorio.urlFoto) | headerFileBase64" />
                  </div>
                  <div class="body">
                    <div class="container_titulo mb-2 d-flex justify-content-center align-items-center mb-2">
                      <h2 class="title">{{ laboratorio.nombre }}</h2>
                    </div>
                    <div class="container_descripcion">
                      <p class="desc">{{ laboratorio.informacionGeneral }}</p>
                    </div>
                  </div>
                  <div class="boton-ver-mas d-flex justify-content-center align-items-center">
                    <router-link :to="{ name: 'laboratorio_info_public', params: { laboratorioId: laboratorio.id } }">
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
<script lang="ts" src="./laboratorios-lista.component.ts"></script>
