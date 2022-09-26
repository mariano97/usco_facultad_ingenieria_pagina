<template>
  <div class="container-programas-lista">
    <section>
      <div class="container-imagen">
        <img alt="imagen-programas-lista" src="/content/images/imagen_libros_usco.jpeg" class=""/>
      </div>
    </section>
    <section class="container mt-5 mb-3 container-formulario-filtrado">
      <div class="container-campos-busqueda row mx-0">
        <div class="col-lg-4 col-sm-auto form-group">
          <label class="form-control-label" for="nombre_programa" v-text="$t('programas-lista.filtro.nombre-programa')">Nombre programa</label>
          <input
            type="text"
            class="form-control"
            id="nombre_programa"
            name="namePrograma"
            placeholder="Eg. Ingenieria"
            v-model="formNamePrograma"
            @keyup="filtrarProgramas"
          />
        </div>
        <div class="col-lg-4 col-sm-auto form-group">
          <label class="form-control-label" for="tipo_programa" v-text="$t('programas-lista.filtro.tipo-programa')">Tipo de programa</label>
          <select
            class="form-control"
            id="tipo_programa"
            v-model="formSelectTipoPrograma"
            @change="filtrarProgramas"
          >
            <option value="0">Seleccione</option>
            <option v-for="(elemento, index) in listTiposPrograma" :key="index" :value="elemento.id">{{elemento.nombre}}</option>
          </select>
        </div>
        <div class="col-lg-4 col-sm-auto d-flex justify-content-center align-items-center">
          <button class="btn" v-text="$t('programas-lista.filtro.btn-limpiar-campos')" @click="limpiarCampos()">
            Limpiar campos
          </button>
        </div>
      </div>
    </section>
    <section class="container cont_resultados mb-5 mt-3">
      <div class="col d-flex align-items-center justify-content-center no_resultados" v-if="listArrayProgramas.length < 1">
        <p>Los sentimos, no hallamos resultados</p>
      </div>
      <div class="container-resultados-programas row mx-0 my-3" v-for="(programaArrayFront, index) in listArrayProgramas" :key="index">
        <div class="col" v-for="(programa, index2) in programaArrayFront" :key="index2">
          <div class="container-resultado row mx-0">
            <div class="col-sm-9">
              <div class="conatiner-nombre-programa">
                <h2 class="mb-0 title-programa">{{ programa.nombre }}</h2>
              </div>
              <div class="cotainer-body">
                <div class="row mx-0">
                  <div class="col-sm-auto pl-0 pr-2">
                    <h4 class="title-opcion" v-text="$t('programas-lista.resultados.titulos.codigo-snies')">Codigo SNIES</h4>
                  </div>
                  <div class="col">
                    <h4 class="data">{{ programa.codigoSnies }}</h4>
                  </div>
                </div>
                <div class="row mx-0">
                  <div class="col-sm-auto pl-0 pr-2">
                    <h4 class="title-opcion" v-text="$t('programas-lista.resultados.titulos.tipo-programa')">Tipo programa</h4>
                  </div>
                  <div class="col">
                    <h4 class="data">{{ programa.nivelFormacion.nombre }}</h4>
                  </div>
                </div>
                <div class="flex-column mx-0">
                  <div class="col-sm-auto pl-0 pr-2">
                    <h4 class="title-opcion" v-text="$t('programas-lista.resultados.titulos.descripcion')">Descripción</h4>
                  </div>
                  <div class="col px-0">
                    <p class="data">
                      {{ programa.presentacionPrograma }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <div class="col container-button-ver">
              <router-link :to="'/faultad-ingenieria/programa-descripcion/' + programa.codigoSnies">
                <button class="btn btn-ver-programa d-flex align-items-center justify-content-center" v-text="$t('programas-lista.resultados.buttons.ver')">Ver</button>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts" src="./programas-lista.component.ts"></script>
