<template>
  <div class="container-pagina-programa">
    <section class="section-encabezado-programa">
      <div class="row mx-0 py-5">
        <div class="col">
          <div class="continer-name-title-programa horizontal-divides-red-2">
            <h2 class="nombre-programa">{{ programa.nombre }}</h2>
          </div>
          <div class="body-informacion">
            <div class="container-opcion row mx-0" v-if="programa.codigoSnies">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.codigo-snies')">código snies</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.codigoSnies }}</h4>
              </div>
            </div>
            <div class="container-opcion row mx-0" v-if="programa.codigoRegistroCalificado && programa.fechaRegistroCalificado">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.registo-calificado')">registro calificado</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.codigoRegistroCalificado + ' de ' + convertDateTimeFromServer(programa.fechaRegistroCalificado) }}</h4>
              </div>
            </div>
            <div class="container-opcion row mx-0" v-if="programa.tipoFormacion">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.metodologia')">metodologia</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.tipoFormacion.nombre }}</h4>
              </div>
            </div>
            <div class="container-opcion row mx-0" v-if="programa.nivelFormacion">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.nivel-formacion')">nivel formacion</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.nivelFormacion.nombre }}</h4>
              </div>
            </div>
            <div class="container-opcion row mx-0" v-if="programa.nombreTituloOtorgado">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.titulo-otorgado')">Titulo otorgado</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.nombreTituloOtorgado }}</h4>
              </div>
            </div>
            <div class="container-opcion row mx-0" v-if="programa.numeroCreditos">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.numero-creditos')">numero creditos</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.numeroCreditos }}</h4>
              </div>
            </div>
            <div class="container-opcion row mx-0" v-if="programa.duracionPrograma">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.duracionPrograma')">numero creditos</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.duracionPrograma + ' semestres' }}</h4>
              </div>
            </div>
            <div class="container-opcion row mx-0" v-if="programa.costoPrograma">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.costoPrograma')">numero creditos</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.costoPrograma | toCOPCurrency }}</h4>
              </div>
            </div>
          </div>
        </div>
        <div class="col px-0">
          <img
            alt="imagen programa"
            class="imagen_programa"
            :src="
              programa.urlFotoPrograma && programa.urlFotoPrograma !== ''
                ? programa.urlFotoPrograma
                : '/content/images/static/image-gris.jpg'
            "
          />
        </div>
      </div>
    </section>
    <!--<section class="informacion_presentacion">
      <div class="container-swiper-presentacion">
        <swiper :slides-per-view="1" :space-between="50" :loop="false" :navigation="true" :pagination="true">
          <swiper-slide v-for="info in pesentacionBasico" :key="info.title" class="slide-swiper">
            <div class="card-container-presentacion mx-auto">
              <div class="">
                <div class="container-title horizontal-divides-red-22 d-flex align-items-center justify-content-center">
                  <h1 class="title">{{ info.title }}</h1>
                </div>
                <div class="body-descripcion">
                  <p class="text">{{ info.descripcion }}</p>
                </div>
              </div>
            </div>-->
            <!--<img
              :src="getImageUrl(34+n)"
              class="img-fluid w-100 mx-auto"
              blank="true"
            >-->
          <!--</swiper-slide>
        </swiper>-->
      </div>
    </section>
  </div>
</template>

<script lang="ts" src="./programa.component.ts"></script>
