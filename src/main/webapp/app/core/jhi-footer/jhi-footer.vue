<template>
  <div id="footer" class="footer">
    <div class="row mx-0">
      <div class="col-lg-8 col-md-3 col-sm-3 row ">
        <div class="col-lg-5 ">
          <div class="d-flex justify-content-center">
            <img class="logo-footer-vertica" alt="usco" src="/content/images/logo-usco/universidad-surcolombiana-logo-vertical-rojo.png"/>
          </div>
          <div class="mt-2 footer-content-informacio-usco">
            <div class="d-flex justify-content-center">
              <h4 v-text="$t('footer.vigilada-minedu')"></h4>
            </div>
            <div class="d-flex justify-content-center">
              <h4 v-text="$t('footer.usco-nit')"></h4>
            </div>
          </div>
        </div>
        <div class="vertical-divider-red"></div>
        <div class="col-sm-5 d-flex align-items-center footer-atencion">
          <div class="d-flex flex-column align-items-center justify-content-center">
            <span>Horario de atención al público:</span>
            <span style="text-align:center;">Lunes a viernes de 8:00 a.m. a 12:00 m. y de 2:00 p.m. a 6:00 p.m</span>
            <span>Línea Gratuita Nacional: 018000 968722</span>
          </div>
        </div>
      </div>
      <div class="col-lg-3 col-md-2 col-sm-2"></div>
      <div class="col-lg-3 col-md-2 col-sm-2"></div>
    </div>
  </div>
</template>

<script lang="ts" src="./jhi-footer.component.ts"></script>
