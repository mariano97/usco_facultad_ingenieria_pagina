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
    <section class="informacion_presentacion container" v-if="isPresentacionBasicaLoaded">
      <div class="container-swiper-presentacion">
        <swiper :slides-per-view="1" :space-between="0" :loop="true" :navigation="true" autoplay :pagination="false">
          <swiper-slide v-for="info in pesentacionBasico" :key="info.title" class="slide-swiper">
            <div class="card-container-presentacion mx-auto">
              <div class="">
                <div class="container-title horizontal-divides-red-2 mb-3">
                  <h1 class="title">{{ info.title }}</h1>
                </div>
                <div class="body-descripcion">
                  <p class="text">{{ info.descripcion }}</p>
                </div>
              </div>
            </div>
          </swiper-slide>
        </swiper>
      </div>
    </section>
    <section class="caracteristicas_programa">
      <div class="">
        <div class="d-flex justify-content-center">
          <div class="container-title_carateristicas horizontal-divides-red-2">
            <h1 v-text="$t('programa.titulos.caracteristicas')">Caracteristicas</h1>
          </div>
        </div>
        <div class="body_caracteristicas">
          <div class="row mx-0 d-flex justify-content-center align-items-center" style="height: 540px;">
            <div class="col d-flex justify-content-center align-items-center" v-if="programa.perfilEstudiante">
              <div class="card-custom d-flex justify-content-center align-items-center">
                <div class="face face1 d-flex justify-content-center align-items-center">
                  <div class="content">
                    <div class="d-flex justify-content-center align-items-center">
                      <img alt="perfilEstudainte" class="imagen_card_caracteristicas" src="/content/images/iconos/notebook-gray.png" />
                    </div>
                    <div class="d-flex justify-content-center align-items-center">
                      <h2 class="title" v-text="$t('programa.titulos.perfil-estudiante')">Perfil del estudiante</h2>
                    </div>
                  </div>
                </div>
                <div class="face face2">
                  <div class="content">
                    <h2 class="mb-4" v-text="$t('programa.titulos.perfil-estudiante')"></h2>
                    <p class="m-0 texto_descripcion">
                      {{ programa.perfilEstudiante }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <div class="col d-flex justify-content-center align-items-center" v-if="programa.perfilEgresado">
              <div class="card-custom d-flex justify-content-center align-items-center">
                <div class="face face1 d-flex justify-content-center align-items-center">
                  <div class="content">
                    <div class="d-flex justify-content-center align-items-center">
                      <img alt="perfilEstudainte" class="imagen_card_caracteristicas" src="/content/images/iconos/sombrero-de-graduacion-gray.png" />
                    </div>
                    <div class="d-flex justify-content-center align-items-center">
                      <h2 class="title" v-text="$t('programa.titulos.perfil-egresado')">Perfil del egresado</h2>
                    </div>
                  </div>
                </div>
                <div class="face face2">
                  <div class="content">
                    <h2 class="mb-4" v-text="$t('programa.titulos.perfil-egresado')"></h2>
                    <p class="m-0 texto_descripcion">
                      {{ programa.perfilEgresado }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <div class="col d-flex justify-content-center align-items-center" v-if="programa.perfilOcupacional">
              <div class="card-custom d-flex justify-content-center align-items-center">
                <div class="face face1 d-flex justify-content-center align-items-center">
                  <div class="content">
                    <div class="d-flex justify-content-center align-items-center">
                      <img alt="perfilEstudainte" class="imagen_card_caracteristicas" src="/content/images/iconos/sombrero-de-graduacion-gray.png" />
                    </div>
                    <div class="d-flex justify-content-center align-items-center">
                      <h2 class="title" v-text="$t('programa.titulos.perfil-ocupacional')">Perfil ocupacional</h2>
                    </div>
                  </div>
                </div>
                <div class="face face2">
                  <div class="content">
                    <h2 class="mb-4" v-text="$t('programa.titulos.perfil-ocupacional')"></h2>
                    <p class="m-0 texto_descripcion">
                      {{ programa.perfilOcupacional }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <div class="col d-flex justify-content-center align-items-center" v-if="programa.perfilEgresado">
              <div class="card-custom d-flex justify-content-center align-items-center">
                <div class="face face1 d-flex justify-content-center align-items-center">
                  <div class="content">
                    <div class="d-flex justify-content-center align-items-center">
                      <img alt="perfilEstudainte" class="imagen_card_caracteristicas" src="/content/images/iconos/plan-de-negocios-gray.png" />
                    </div>
                    <div class="d-flex justify-content-center align-items-center">
                      <h2 class="title" v-text="$t('programa.titulos.plan-estudio')">Plan de estudio</h2>
                    </div>
                  </div>
                </div>
                <div class="face face2">
                  <div class="content">
                    <h2 class="mb-4" v-text="$t('programa.titulos.plan-estudio')"></h2>
                    <p class="m-0 texto_descripcion">
                      {{ programa.perfilEgresado }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>


          <!--<div class="card">
            <div class="face face1">
              <div class="content">
                <img src="icon-2.png"/>
                <h3>code</h3>
              </div>
            </div>
            <div class="face face2">
              <div class="content">
                <p>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna
                  aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis
                  aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                </p>
              </div>
            </div>
          </div>

          <div class="card">
            <div class="face face1">
              <div class="content">
                <img src="icon-2.png"/>
                <h3>code2</h3>
              </div>
            </div>
            <div class="face face2">
              <div class="content">
                <p>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna
                  aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis
                  aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                </p>
              </div>
            </div>
          </div>

          <div class="card">
            <div class="face face1">
              <div class="content">
                <img src="icon-2.png"/>
                <h3>code3</h3>
              </div>
            </div>
            <div class="face face2">
              <div class="content">
                <p>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna
                  aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis
                  aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                </p>
              </div>
            </div>
          </div>-->
          <!--<div class="team">
       <div class="card">
          <div class="side side1">
             <div class="img-info">
                <img src="img/1.png">
                <h3>Emma Jones</h3>
             </div>
          </div>
          <div class="side side2">
             <div class="img-info">
               <h2>Short Bio</h2>
                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam suscipit sequi esse, tenetur amet molestiae, corrupti tempora! Officia cum nobis, tempore perferendis, voluptates laboriosam alias. Lorem ipsum dolor.</p>
                <a href="#">More Info</a>
             </div>
          </div>
       </div>
       <div class="card">
          <div class="side side1">
             <div class="img-info">
                <img src="img/2.png">
                <h3>Josh Wood</h3>
             </div>
          </div>
          <div class="side side2">
             <div class="img-info">
               <h2>Short Bio</h2>
                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam suscipit sequi esse, tenetur amet molestiae, corrupti tempora! Officia cum nobis, tempore perferendis, voluptates laboriosam alias. Lorem ipsum dolor.</p>
                <a href="#">More Info</a>
             </div>
          </div>
       </div>
       <div class="card">
          <div class="side side1">
             <div class="img-info">
                <img src="img/3.png">
                <h3>Ellie Jones</h3>
             </div>
          </div>
          <div class="side side2">
             <div class="img-info">
               <h2>Short Bio</h2>
                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam suscipit sequi esse, tenetur amet molestiae, corrupti tempora! Officia cum nobis, tempore perferendis, voluptates laboriosam alias. Lorem ipsum dolor.</p>
                <a href="#">More Info</a>
             </div>
          </div>
       </div>
    </div>-->
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts" src="./programa.component.ts"></script>
