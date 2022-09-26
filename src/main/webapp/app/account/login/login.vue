<template>
  <div class="container conatiner-login">
    <div class="row mx-0">
      <div class="col container-formulario">
        <div class="d-flex justify-content-center align-items-center container-title">
          <!--<h2 class="" v-text="$t('login.title')">Iniciar Sesión</h2>-->
          <h2 class="">{{ titleIngresar }}</h2>
        </div>
        <div class="container-form-login">
          <form id="fomulario-login" name="formularioLogin" role="form" @submit.prevent="doLogin">
            <div class="form-group mb-4">
              <label class="form-control-label" for="coreo_electronico" v-text="$t('login.form.correo-electronico')">Correo Electronico</label>
              <input
                type="text"
                class="form-control"
                id="coreo_electronico"
                name="email"
                placeholder="usuario@example.com"
                v-model="userLoginAccount.username"
                required
              />
            </div>
            <div class="form-group mb-4">
              <label class="form-control-label" for="contrasena" v-text="$t('login.form.password')">Contraseña</label>
              <input
                type="password"
                class="form-control"
                id="contrasena"
                name="password"
                placeholder="contraseña"
                v-model="userLoginAccount.password"
                required
              />
            </div>
            <div class="container-button-iniciar-sesion d-flex justify-content-center">
              <button type="submit" class="btn " v-text="$t('login.form.button')" >
                Iniciar Sesión
              </button>
            </div>
          </form>
        </div>
      </div>
      <div class="col container-image d-flex justify-content-center align-items-center">
        <img alt="img_ingenieria"
          src="/content/images/logo-facultad/facultad_ingenieria_usco.png"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./login.component.ts"></script>
