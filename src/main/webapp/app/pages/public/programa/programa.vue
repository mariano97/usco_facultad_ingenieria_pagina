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
            <div class="container-opcion row mx-0"
              v-if="programa.codigoRegistroCalificado && programa.fechaRegistroCalificado">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.registo-calificado')">registro calificado</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ programa.codigoRegistroCalificado + ' de ' +
                convertDateTimeFromServer(programa.fechaRegistroCalificado) }}</h4>
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
            <div class="container-opcion row mx-0" v-if="programa.sedes && programa.sedes.length > 0">
              <div class="col-sm-auto px-0">
                <h4 class="title-opcion" v-text="$t('programa.titulos.lugarOferta')">numero creditos</h4>
              </div>
              <div class="col">
                <h4 class="option">{{ generateTextLugarOferta() }}</h4>
              </div>
            </div>
          </div>
        </div>
        <div class="col px-0 d-flex align-items-center">
          <img v-if="showImage" alt="imagen programa" class="imagen_programa"
            :src="imageProfilePrograma | headerFileBase64" />
          <img v-if="!showImage" alt="imagen programa" class="imagen_programa"
            :src="'/content/images/static/image-gris.jpg'" />
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
                      <img alt="perfilEstudainte" class="imagen_card_caracteristicas"
                        src="/content/images/iconos/notebook-gray.png" />
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
                      <img alt="perfilEstudainte" class="imagen_card_caracteristicas"
                        src="/content/images/iconos/sombrero-de-graduacion-gray.png" />
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
                      <img alt="perfilEstudainte" class="imagen_card_caracteristicas"
                        src="/content/images/iconos/occupational-profile-gray.png" />
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
            <div class="col d-flex justify-content-center align-items-center" v-if="archivoProgramaPlanEstudio.id">
              <div class="card-custom d-flex justify-content-center align-items-center">
                <div class="face face1 d-flex justify-content-center align-items-center">
                  <div class="content">
                    <div class="d-flex justify-content-center align-items-center">
                      <img alt="perfilEstudainte" class="imagen_card_caracteristicas"
                        src="/content/images/iconos/plan-de-negocios-gray.png" />
                    </div>
                    <div class="d-flex justify-content-center align-items-center">
                      <h2 class="title" v-text="$t('programa.titulos.plan-estudio')">Plan de estudio</h2>
                    </div>
                  </div>
                </div>
                <div class="face face2">
                  <div class="content h-50">
                    <h2 class="mb-4" v-text="$t('programa.titulos.plan-estudio')"></h2>
                    <div class="m-0 h-100 texto_descripcion d-flex align-items-center justify-content-center">
                      <div class="container_spinner d-flex align-items-center justify-content-center">
                        <b-spinner v-if="showSpinnerPlanEstudio" style="width: 3rem; height: 3rem;" type="grow"
                          label="Cargando" variant="danger">
                        </b-spinner>
                      </div>
                      <button v-if="archivoProgramaPlanEstudio.id && !showSpinnerPlanEstudio" id="btn_show_plan_estudio"
                        class="btn btn_show_plan_estudio d-flex align-items-center justify-content-center"
                        v-text="$t('programa.buttons.publicPlanEstudio')" type="button"
                        @click="verArchivoPrograma(archivoProgramaPlanEstudio)">
                        Ver plan de estudio
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="sedes_mapa_programa">
      <div class="">
        <div class="d-flex justify-content-center">
          <div class="container-title_sede horizontal-divides-red-2">
            <h1 v-text="$t('programa.titulos.sedesMapa')">Sedes</h1>
          </div>
        </div>
        <div class="body_mapa_sedes mb-3">
          <div class="">
            <l-map ref="map" style="height: 500px; width: 100%">
              <l-tile-layer :url="urlMap" :attribution="copyrightMap" />
              <l-marker v-for="markSede in marksSedes" :key="markSede.name" :lat-lng="[markSede.lat, markSede.lon]"
                @click="clickIntoMarks">
                <l-popup>{{ markSede.name }}</l-popup>
              </l-marker>
            </l-map>
          </div>
        </div>
      </div>
    </section>
    <section class="documentos_programa container" v-if="listArrayArchivosProgramas.length > 0">
      <div class="">
        <div class="d-flex justify-content-center">
          <div class="container-title_documentos horizontal-divides-red-2">
            <h1 v-text="$t('programa.titulos.documentosPrograma')">Documentos</h1>
          </div>
        </div>
        <div class="body_documentos_programa">
          <div class="row mx-0 my-3 d-flex justify-content-start align-items-center"
            v-for="(archivoProgramaArray, index) in listArrayArchivosProgramas" :key="index">
            <div class="col col_container_archivo d-flex align-items-center justify-content-center"
              v-for="(archivo, index2) in archivoProgramaArray" :key="index2">
              <div class="container_documento_programa row mx-0" v-if="archivo.id">
                <div class="col py-3">
                  <h3 class="m-0 name_documento">{{archivo.nombreArchivo}}</h3>
                </div>
                <div class="col-sm-auto d-flex justify-content-center align-items-center px-0">
                  <button class="btn btn_ver_archivo_documento d-flex justify-content-center align-items-center"
                    type="button" v-text="$t('programa.buttons.ver')" @click="verArchivoPrograma(archivo)"
                    :disabled="showSpinnerPlanEstudio">
                    Ver
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="redes_sociales_programa container" v-if="listRedesSocialesPrograma.length > 0">
      <div class="">
        <div class="row mx-0">
          <div class="col-sm-auto container-title_nuestras_redes d-flex align-items-center">
            <h3 v-text="$t('programa.titulos.nuestrasRedes')">Nuestras redes</h3>
          </div>
          <div class="dividor vertical-divider-red"></div>
          <div class="row mx-0 justify-content-start conatiner_redes_body">
            <div class="col container_red_social_item" v-for="redSocial in listRedesSocialesPrograma"
              :key="redSocial.id">
              <a target="_blan" :href="redSocial.urlRedSocial"
                class="w-100 h-100 d-flex align-items-center justify-content-center">
                <img v-if="redSocial.tablaElementoCatalogo.id === 8" alt="red_social-facebook"
                  src="/content/images/iconos/facebook.png" />
                <img v-if="redSocial.tablaElementoCatalogo.id === 9" alt="red_social-instagram"
                  src="/content/images/iconos/instagram.png" />
                <img v-if="redSocial.tablaElementoCatalogo.id === 10" alt="red_social-tiktok"
                  src="/content/images/iconos/tik-tok.png" />
                <img v-if="redSocial.tablaElementoCatalogo.id === 11" alt="red_social-twitter"
                  src="/content/images/iconos/twitter.png" />
                <img v-if="redSocial.tablaElementoCatalogo.id === 12" alt="red_social-you-tube"
                  src="/content/images/iconos/youtube.png" />
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="opciones_informacion_adicional">
      <div class="">
        <div class="row mx-0">
          <div class="col card-container-opciones-adicionales">
            <div class="content_image">
              <img alt="requisitos" src="/content/images/static/checklist.png" />
            </div>
            <div class="body">
              <a target="_blank" href="https://www.usco.edu.co/es/ingreso-a-la-usco/">
                <div class="mt-3 d-flex align-items-center justify-content-center title_opcion">
                  <h3 v-text="$t('programa.titulos.requisitos')">Requisitos</h3>
                </div>
                <div class="mt-3 texto">
                  <p class="m-0" v-text="$t('programa.opcionesDescripcion.requisitos')"></p>
                </div>
              </a>
            </div>
          </div>
          <div class="col card-container-opciones-adicionales">
            <div class="content_image">
              <img alt="requisitos" src="/content/images/static/online-education.jpeg" />
            </div>
            <div class="body">
              <a target="_blank"
                href="https://www.usco.edu.co/contenido/SGC-USCO/documentos/06.MI-FOR-FORMACION/MI-FOR-PR-22%20HOMOLOGACIONES.pdf">
                <div class="mt-3 d-flex align-items-center justify-content-center title_opcion">
                  <h3 v-text="$t('programa.titulos.homologacion')">homologacion</h3>
                </div>
                <div class="mt-3 texto">
                  <p class="m-0" v-text="$t('programa.opcionesDescripcion.homologacion')"></p>
                </div>
              </a>
            </div>
          </div>
          <div class="col card-container-opciones-adicionales">
            <div class="content_image">
              <img alt="requisitos" src="/content/images/static/graduation.jpeg" />
            </div>
            <div class="body">
              <a target="_blank" href="">
                <div class="mt-3 d-flex align-items-center justify-content-center title_opcion">
                  <h3 v-text="$t('programa.titulos.opcionesGrado')">opcionesGrado</h3>
                </div>
                <div class="mt-3 texto">
                  <p class="m-0" v-text="$t('programa.opcionesDescripcion.opcionesGrado')"></p>
                </div>
              </a>
            </div>
          </div>
          <div class="col card-container-opciones-adicionales">
            <div class="content_image">
              <img alt="requisitos" src="/content/images/static/book-teach.jpg" />
            </div>
            <div class="body">
              <div class="mt-3 d-flex align-items-center justify-content-center title_opcion">
                <h3 v-text="$t('programa.titulos.profesores')">opcionesGrado</h3>
              </div>
              <div class="mt-3 texto">
                <p class="m-0" v-text="$t('programa.opcionesDescripcion.profesores')"></p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts" src="./programa.component.ts"></script>
