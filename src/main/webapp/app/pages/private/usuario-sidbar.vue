<template>
  <div class="container-sidbar-usuario row mx-0">
    <div class="sidbar-usuario">
      <section class="conatiner-encabezado d-flex align-items-center justify-content-start">
        <div class="horizontal-divides-red row mx-0 w-100">
          <div class="col-sm-auto">
            <img alt="imagen_perfil_usuario" class="imagen_usuario rounded-circle" src="/content/images/jhipster_family_member_1_head-512.png" />
          </div>
          <div class="col">
            <h3 class="nombre_usuario">ghgjghgj</h3>
            <div class="row mx-0">
              <div class="col-sm-auto px-0">
                <img alt="mini_usuario_email" class="mini_imagen_sidbar" src="/content/images/iconos/email.png" />
              </div>
              <div class="col pr-0 d-flex align-items-center">
                <h4 class="texto_usuario_sidbar">dsffsf</h4>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="container-opciones-rutas">
        <div class="horizontal-divides-red w-100">
          <div class="">
            <router-link :to="{ name: 'usuario_programas_lista' }" class="opcion-sidbar py-4">
              <button class="btn w-100">
                <div class="row mx-0 align-items-center justify-content-center">
                  <div class="col-sm-auto">
                    <img alt="programa" class="mini_imagen_icon_option" src="/content/images/iconos/brainstorm_red.png" />
                  </div>
                  <div class="col d-flex align-items-center">
                    <h4 class="texto-opcion" v-text="$t('paginas.privado.sidbar.opciones.programas')">Programas</h4>
                  </div>
                </div>
              </button>
            </router-link>
          </div>
          <div class="">
            <router-link :to="{ name: 'inicio_usuario' }" class="opcion-sidbar">
              <button class="btn w-100">
                <div class="row mx-0 align-items-center justify-content-center">
                  <div class="col-sm-auto">
                    <img alt="programa" class="mini_imagen_icon_option" src="/content/images/iconos/teaching_red.png" />
                  </div>
                  <div class="col d-flex align-items-center">
                    <h4 class="texto-opcion" v-text="$t('paginas.privado.sidbar.opciones.profesores')">Profesores</h4>
                  </div>
                </div>
              </button>
            </router-link>
          </div>
          <div class="">
            <router-link :to="{ name: 'inicio_usuario' }" class="opcion-sidbar">
              <button class="btn w-100">
                <div class="row mx-0 align-items-center justify-content-center">
                  <div class="col-sm-auto">
                    <img alt="programa" class="mini_imagen_icon_option" src="/content/images/iconos/place_red.png" />
                  </div>
                  <div class="col d-flex align-items-center">
                    <h4 class="texto-opcion" v-text="$t('paginas.privado.sidbar.opciones.sedes')">Sedes</h4>
                  </div>
                </div>
              </button>
            </router-link>
          </div>
        </div>
      </section>
      <section class="container-opciones-config">
        <div class="w-100">
          <div class="">
            <button class="btn w-100" v-on:click="logout()">
              <div class="row mx-0 align-items-center justify-content-center">
                <div class="col-sm-auto">
                  <img alt="programa" class="mini_imagen_icon_option" src="/content/images/iconos/sign-out.png" />
                </div>
                <div class="col d-flex align-items-center">
                  <h4 class="texto-opcion" v-text="$t('paginas.privado.sidbar.opciones.salir')">Sedes</h4>
                </div>
              </div>
            </button>
          </div>
        </div>
      </section>
    </div>
    <div class="col">
      <router-view></router-view>
    </div>
  </div>
</template>

<script lang="ts" src="./usuario-sidbar.component.ts"></script>
