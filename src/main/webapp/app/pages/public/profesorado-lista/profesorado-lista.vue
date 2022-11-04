<template>
  <div class="container-lista-profesorado">
    <section class="section_titulo_profesorado">
      <div class="d-flex justify-content-start arraow_return" v-if="!showFiltering">
        <div class="row mx-0">
          <div class="col-sm-auto px-0 d-flex align-items-center">
            <a @click="returnUrlHref" class="">
              <img alt="retornar" src="/content/images/iconos/left-arrow-red.png" />
            </a>
          </div>
          <div class="col d-flex align-items-center">
            <h2 class="title_formulario_return">Regresar</h2>
          </div>
        </div>
      </div>
      <div class="">
        <div class="d-flex justify-content-center">
          <div class="container-title_profesorado horizontal-divides-red-2">
            <h1>{{ tituloPaginaProfesorado }}</h1>
          </div>
        </div>
      </div>
    </section>
    <section class="section_filtros_pagina">
      <div class="d-flex justify-content-center" v-if="showFiltering">
        <div class="container_campos_busqueda row mx-0">
          <div class="col col-opcion form-group">
            <label class="form-control-label" for="select_programa"
              v-text="$t('profesor.titulos.label_busqueda_programa')"></label>
            <select class="form-control" id="select_programa" v-model="formSelectPrograma" @change="filtrarProfesores">
              <option value="0">Seleccione</option>
              <option v-for="(programa, index) in listadoProgramas" :key="index" :value="programa.codigoSnies">{{
              programa.nombre }}</option>
            </select>
          </div>
          <div class="col-sm-auto d-flex justify-content-center align-items-center">
            <button class="btn btn_clean_fields" v-text="$t('profesor.titulos.buttons.btn_clean_field')"
              @click="limpiarCampos"></button>
          </div>
        </div>
      </div>
    </section>
    <section class="section_listado_profesor">
      <div class="">
        <div class="col d-flex align-items-center justify-content-center no_resultados" v-if="usuariosProfesores.length < 1">
          <p>Los sentimos, no hallamos resultados</p>
        </div>
        <div class="container-resultados-profesores row mx-0 my-3" v-for="(profesorArray, index) in usuariosProfesores"
          :key="index">
          <div class="col col-item-profesor" v-for="(profesor, index2) in profesorArray" :key="index2">
            <div class="container_item_profesor row mx-0">
              <div class="container_imagen">
                <img alt="imagen_profesor"
                  :src="obtenerImagen(profesor.profesorDTO.urlFotoProfesor) | headerFileBase64" />
              </div>
              <div class="d-flex align-items-center justify-content-center">
                <div class="col">
                  <h2 class="name_profesor">{{ profesor.adminUserDTO.nameComplete }}</h2>
                  <div class="row mx-0 container_data_profesor">
                    <div class="col-sm-auto px-0">
                      <img class="image_data_profesor" alt="img_email" src="/content/images/iconos/email.png" />
                    </div>
                    <div class="col col-email">
                      <h4 class="texto_data_profesor">{{ profesor.adminUserDTO.email }}</h4>
                    </div>
                  </div>
                  <div class="row mx-0 container_data_profesor">
                    <div class="col-sm-auto px-0">
                      <img class="image_data_profesor" alt="img_email" src="/content/images/iconos/id-card-red.png" />
                    </div>
                    <div class="col">
                      <h4 class="texto_data_profesor">{{ profesor.profesorDTO.tablaElementoCatalogo.nombre }}</h4>
                    </div>
                  </div>
                  <div class="row justify-content-center align-items-center">
                    <router-link
                      :to="{ name: 'profesor_informacion', params: { userLogin: profesor.adminUserDTO.login }  }">
                      <button class="btn btn_ver_profesor d-flex align-items-center justify-content-center"
                        v-text="$t('profesor.titulos.buttons.btn_ver')"></button>
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
<script lang="ts" src="./profesorado-lista.component.ts"></script>
