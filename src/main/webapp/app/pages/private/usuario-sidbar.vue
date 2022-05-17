<template>
  <div class="container-sidbar-usuario row mx-0">
    <div class="sidbar-usuario">
      <section class="conatiner-encabezado">
        <div class="row mx-0">
          <div class="col-sm-auto">
            <img alt="imagen_perfil_usuario" class="imagen_usuario" src="" />
          </div>
          <div class="col">
            <h3 class="nombre_usuario"></h3>
            <div class="row mx-0">
              <div class="col-sm-auto">
                <img alt="mini_usuario_email" class="mini_imagen_sidbar" src="/content/images/iconos/email.png" />
              </div>
              <div class="col">
                <h4 class="texto_usuario_sidbar"></h4>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="container-opciones-rutas">

      </section>
    </div>
    <div class="col">
      <router-view></router-view>
    </div>
  </div>
</template>

<script lang="ts" src="./usuario-sidbar.component.ts"></script>
