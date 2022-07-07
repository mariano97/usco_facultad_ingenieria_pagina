<template>
  <div class="container_profesor_informacion">
    <section class="section_return_arrow">
      <div class="d-flex justify-content-start arraow_return">
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
    </section>
    <section class="section_informacion_profesor">
      <div class="container_informacion">
        <div class="row mx-0">
          <div class="col col-container-imagen d-flex justify-content-center align-items-center">
            <img alt="imagen_profesor"
              :src="obtenerImagen(usuarioProfesor.profesorDTO.urlFotoProfesor) | headerFileBase64" />
          </div>
          <div class="col col-container-data-info">
            <h1 class="name_profesor" v-if="usuarioProfesor.adminUserDTO.nameComplete">{{
              usuarioProfesor.adminUserDTO.nameComplete }}</h1>
            <div class="row mx-0 container_data_info" v-if="usuarioProfesor.adminUserDTO.email">
              <div class="col-sm-auto px-0">
                <img alt="aimgen_email" src="/content/images/iconos/email.png" />
              </div>
              <div class="col texto_info">
                <h2 class="texto m-0">{{ usuarioProfesor.adminUserDTO.email }}</h2>
              </div>
            </div>
            <div class="row mx-0 container_data_info" v-if="usuarioProfesor.profesorDTO.emailAlternativo">
              <div class="col-sm-auto px-0">
                <img alt="aimgen_email" src="/content/images/iconos/email.png" />
              </div>
              <div class="col texto_info">
                <h2 class="texto m-0">{{ usuarioProfesor.profesorDTO.emailAlternativo }}</h2>
              </div>
            </div>
            <div class="row mx-0 container_data_info" v-if="usuarioProfesor.profesorDTO.tablaElementoCatalogo">
              <div class="col-sm-auto px-0">
                <img alt="aimgen_email" src="/content/images/iconos/id-card-red.png" />
              </div>
              <div class="col texto_info">
                <h2 class="texto m-0">{{ usuarioProfesor.profesorDTO.tablaElementoCatalogo.nombre }}</h2>
              </div>
            </div>
            <div class="row mx-0 container_data_info" v-if="usuarioProfesor.profesorDTO.oficina">
              <div class="col-sm-auto px-0">
                <img alt="aimgen_email" src="/content/images/iconos/office-building-red.png" />
              </div>
              <div class="col texto_info">
                <h2 class="texto m-0">{{ 'Oficina: ' + usuarioProfesor.profesorDTO.oficina }}</h2>
              </div>
            </div>
            <div class="row mx-0 container_data_info" v-if="usuarioProfesor.profesorDTO.telefonoCelular">
              <div class="col-sm-auto px-0">
                <img alt="aimgen_email" src="/content/images/iconos/phone-call-red.png" />
              </div>
              <div class="col texto_info">
                <h2 class="texto m-0">{{ 'Télefono: ' + usuarioProfesor.profesorDTO.telefonoCelular }}</h2>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="section_informacion_tabs_profesor">
      <div class="d-flex justify-content-center align-items-center container_tabs_info_profesor">
        <div class="row mx-0">
          <div class="col-sm-auto container_item_tab tab_extremos"></div>
          <div class="col-sm-auto container_item_tab">
            <a class="d-flex justify-content-center align-items-center"
              :class="{ active: showTabsInfoProfesor('PERFIL') }" @click="activeTab('PERFIL')">
              <h3 class="m-0" v-text="$t('profesor.tabs.labels.perfil')"></h3>
            </a>
          </div>
          <div class="col-sm-auto container_item_tab">
            <a class="d-flex justify-content-center align-items-center"
              :class="{ active: showTabsInfoProfesor('ASIGNATURAS') }" @click="activeTab('ASIGNATURAS')">
              <h3 class="m-0" v-text="$t('profesor.tabs.labels.asignaturas')"></h3>
            </a>
          </div>
          <div class="col-sm-auto container_item_tab">
            <a class="d-flex justify-content-center align-items-center"
              :class="{ active: showTabsInfoProfesor('TITULOS_ACADEMICOS') }" @click="activeTab('TITULOS_ACADEMICOS')">
              <h3 class="m-0" v-text="$t('profesor.tabs.labels.titulos')"></h3>
            </a>
          </div>
          <div class="col-sm-auto container_item_tab tab_extremos"></div>
        </div>
      </div>
      <div class="container_resultado_select_tab">
        <div class="" v-if="showTabsInfoProfesor('PERFIL')">
          <p class="m-0">{{ usuarioProfesor.profesorDTO.perfil }}</p>
        </div>
        <div class="" v-if="showTabsInfoProfesor('ASIGNATURAS')">
          <div class="container_items_asignaturas row mx-0 my-3"
            v-for="(asignaturas, index) in cursosMateriasProfesorLista" :key="index">
            <div class="col col_item_asignatura mx-3" v-for="(asignatura, index2) in asignaturas" :key="index2">
              <div class="">
                <div class="d-flex justify-content-center align-items-center">
                  <h2 class="m-0 name_curso">{{ asignatura.nombre }}</h2>
                </div>
                <div class="row mx-0">
                  <div class="col row mx-0 container_info">
                    <div class="col-sm-auto p-0">
                      <img alt="imagen_asignatura" src="/content/images/iconos/speedometer-red.png" />
                    </div>
                    <div class="col">
                      <h3 class="texto_descripcion">{{ asignatura.numeroCreditos + ' Créditos' }}</h3>
                    </div>
                  </div>
                  <div class="col row mx-0 container_info">
                    <div class="col-sm-auto p-0">
                      <img alt="imagen_asignatura" src="/content/images/iconos/work-time-red.png" />
                    </div>
                    <div class="col">
                      <h3 class="texto_descripcion">{{ asignatura.semestreImpartida + ' Semestre' }}</h3>
                    </div>
                  </div>
                </div>
                <div class="row mx-0 col container_info" v-if="asignatura.nivelAcademico">
                  <div class="col-sm-auto p-0">
                    <img alt="imagen_asignatura" src="/content/images/iconos/chip-red.png" />
                  </div>
                  <div class="col">
                    <h3 class="texto_descripcion">{{ asignatura.nivelAcademico.nombre }}</h3>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="" v-if="showTabsInfoProfesor('TITULOS_ACADEMICOS')">
          <div class="container_items_titulos_academicos row mx-0 my-3"
            v-for="(titulosAcademicos, index) in titulosAcademicosProfesorLista" :key="index">
            <div class="col col_item_titulo_academico mx-3" v-for="(tituloAcademico, index2) in titulosAcademicos"
              :key="index2">
              <div class="row mx-0">
                <div class="col-sm-auto col-year p-2 d-flex justify-content-center align-items-center">
                  <div class="d-flex justify-content-center align-items-center">
                    <h3 class="m-0">{{ convertDateTimeFromServer(tituloAcademico.yearTitulo) }}</h3>
                  </div>
                </div>
                <div class="col col-data-info p-2">
                  <div class="d-flex justify-content-center align-items-center">
                    <div class="">
                      <h2 class="m-0 name_titulo">{{ tituloAcademico.nombreTitulo }}</h2>
                      <div class="row mx-0 container_info" v-if="tituloAcademico.tablaElementoCatalogo">
                        <div class="col-sm-auto p-0">
                          <img alt="image_ingo" src="/content/images/iconos/chip-red.png" />
                        </div>
                        <div class="col">
                          <h3 class="texto_descripcion">{{ tituloAcademico.tablaElementoCatalogo.nombre }}</h3>
                        </div>
                      </div>
                      <div class="row mx-0 container_info">
                        <div class="col-sm-auto p-0">
                          <img alt="image_ingo" src="/content/images/iconos/university-red.png" />
                        </div>
                        <div class="col">
                          <h3 class="texto_descripcion">{{ tituloAcademico.nombreUniversidadTitulo }}</h3>
                        </div>
                      </div>
                      <div class="row mx-0 container_info" v-if="tituloAcademico.paises">
                        <div class="col-sm-auto p-0">
                          <img alt="image_ingo" src="/content/images/iconos/placeholder.png" />
                        </div>
                        <div class="col">
                          <h3 class="texto_descripcion">{{ tituloAcademico.paises.nombrePais }}</h3>
                        </div>
                      </div>
                    </div>
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
<script lang="ts" src="./profesor-info.component.ts"></script>
