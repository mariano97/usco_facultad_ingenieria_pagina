<template>
  <div class="">
    <b-navbar data-cy="navbar" toggleable="md" type="dark" class="jh-navbar">
      <b-navbar-brand class="logo" b-link to="/">
        <div class="row mx-0">
          <div class="col-lg-5 col-md-2 col-sm-2 d-flex align-items-center">
            <img class="logo-img" alt="logo"
              src="/content/images/logo-usco/universidad-surcolombiana-horizontal-blanco.png" />
          </div>
          <div class="vertical-divider-white">

          </div>
          <div class="col-sm-auto d-flex align-items-center">
            <div class="d-block">
              <span v-text="$t('global.title-first')" class="navbar-title navbar-title-first"></span>
              <span v-text="$t('global.title-second')" class="navbar-title navbar-title-second"></span>
            </div>
          </div>
        </div>



      </b-navbar-brand>
      <b-navbar-toggle right class="jh-navbar-toggler d-lg-none" href="javascript:void(0);" data-toggle="collapse"
        target="header-tabs" aria-expanded="false" aria-label="Toggle navigation">
        <font-awesome-icon icon="bars" />
      </b-navbar-toggle>

      <b-collapse is-nav id="header-tabs" class="nav-container-items mobile">
        <b-navbar-nav class="ml-auto">
          <b-nav-item to="/faultad-ingenieria/programas-lista" v-if="!authenticated"
            class="nav-item-ingresar d-flex justify-content-center align-items-center mobile">
            <h4 v-text="$t('global.menu.programas')">Programas</h4>
          </b-nav-item>
          <b-nav-item to="/faultad-ingenieria/programa-profesorado-lista" v-if="!authenticated"
            class="nav-item-ingresar d-flex justify-content-center align-items-center mobile">
            <h4 v-text="$t('global.menu.profesorado')"></h4>
          </b-nav-item>
          <b-nav-item to="/faultad-ingenieria/laboratorios-lista" v-if="!authenticated"
            class="nav-item-ingresar d-flex justify-content-center align-items-center mobile">
            <h4>Laboratorios</h4>
          </b-nav-item>
          <b-nav-item to="/faultad-ingenieria/semilleros-lista" v-if="!authenticated"
            class="nav-item-ingresar d-flex justify-content-center align-items-center mobile">
            <h4>Semilleros</h4>
          </b-nav-item>
          <b-nav-item :to="{ name: 'laboratorio_info_granja_public', params: { laboratorioId: labGranja.id } }" v-if="hasLabGranja && !authenticated"
            class="nav-item-ingresar d-flex justify-content-center align-items-center mobile">
            <h4>Granja</h4>
          </b-nav-item>
          <b-nav-item :to="{ name: 'laboratorio_info_museo_public', params: { laboratorioId: labMuseo.id } }" v-if="hasLabMuseo && !authenticated"
            class="nav-item-ingresar d-flex justify-content-center align-items-center mobile">
            <h4>Museo</h4>
          </b-nav-item>
          <!--<b-nav-item to="/faultad-ingenieria/semilleros-lista" v-if="!authenticated"
            class="nav-item-ingresar d-flex justify-content-center align-items-center mobile">
            <h4>Semilleros</h4>
          </b-nav-item>-->
          <b-nav-item v-if="!authenticated" href="https://www.usco.edu.co/es/" target="_blank"
            class="nav-item-ingresar d-flex justify-content-center align-items-center">
            <h4>Usco</h4>
          </b-nav-item>
          <b-nav-item v-if="!authenticated" href="https://www.usco.edu.co/es/sistemas-de-consultas-academicas/" target="_blank"
            class="nav-item-ingresar d-flex justify-content-center align-items-center">
            <h4>Consultas Academicas</h4>
          </b-nav-item>
          <b-nav-item to="/login" exact v-if="!authenticated"
            class="nav-item-ingresar d-flex justify-content-center align-items-center">
            <h4 v-text="$t('global.menu.login')">Ingresar</h4>
          </b-nav-item>

          <!--<b-nav-item to="/" exact>
            <span>
              <font-awesome-icon icon="home" />
              <span v-text="$t('global.menu.home')">Home</span>
            </span>
          </b-nav-item>-->
          <b-nav-item-dropdown right id="entity-menu" v-if="authenticated" active-class="active" class="pointer"
            data-cy="entity">
            <span slot="button-content" class="navbar-dropdown-menu">
              <font-awesome-icon icon="th-list" />
              <span class="no-bold" v-text="$t('global.menu.entities.main')">Entities</span>
            </span>
            <entities-menu></entities-menu>
            <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
          </b-nav-item-dropdown>
          <b-nav-item-dropdown right id="admin-menu" v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
            :class="{ 'router-link-active': subIsActive('/admin') }" active-class="active" class="pointer"
            data-cy="adminMenu">
            <span slot="button-content" class="navbar-dropdown-menu">
              <font-awesome-icon icon="users-cog" />
              <span class="no-bold" v-text="$t('global.menu.admin.main')">Administration</span>
            </span>
            <b-dropdown-item to="/admin/gateway" active-class="active">
              <font-awesome-icon icon="road" />
              <span v-text="$t('global.menu.admin.gateway')">Gateway</span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/user-management" active-class="active">
              <font-awesome-icon icon="users" />
              <span v-text="$t('global.menu.admin.userManagement')">User management</span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/metrics" active-class="active">
              <font-awesome-icon icon="tachometer-alt" />
              <span v-text="$t('global.menu.admin.metrics')">Metrics</span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/health" active-class="active">
              <font-awesome-icon icon="heart" />
              <span v-text="$t('global.menu.admin.health')">Health</span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/configuration" active-class="active">
              <font-awesome-icon icon="cogs" />
              <span v-text="$t('global.menu.admin.configuration')">Configuration</span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/logs" active-class="active">
              <font-awesome-icon icon="tasks" />
              <span v-text="$t('global.menu.admin.logs')">Logs</span>
            </b-dropdown-item>
            <b-dropdown-item v-if="openAPIEnabled" to="/admin/docs" active-class="active">
              <font-awesome-icon icon="book" />
              <span v-text="$t('global.menu.admin.apidocs')">API</span>
            </b-dropdown-item>
          </b-nav-item-dropdown>
          <!--<b-nav-item-dropdown id="languagesnavBarDropdown" right v-if="languages && Object.keys(languages).length > 1">
            <span slot="button-content">
              <font-awesome-icon icon="flag" />
              <span class="no-bold" v-text="$t('global.menu.language')">Language</span>
            </span>
            <b-dropdown-item
              v-for="(value, key) in languages"
              :key="`lang-${key}`"
              v-on:click="changeLanguage(key)"
              :class="{ active: isActiveLanguage(key) }"
            >
              {{ value.name }}
            </b-dropdown-item>
          </b-nav-item-dropdown>-->
          <b-nav-item-dropdown right href="javascript:void(0);" id="account-menu"
            :class="{ 'router-link-active': subIsActive('/account') }" active-class="active" class="pointer"
            data-cy="accountMenu" v-if="hasAnyAuthority('ROLE_ADMIN')">
            <span slot="button-content" class="navbar-dropdown-menu">
              <font-awesome-icon icon="user" />
              <span class="no-bold"> {{$t('global.menu.account.main')}} </span>
            </span>
            <b-dropdown-item data-cy="settings" to="/account/settings" tag="b-dropdown-item" v-if="authenticated"
              active-class="active">
              <font-awesome-icon icon="wrench" />
              <span v-text="$t('global.menu.account.settings')">Settings</span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="passwordItem" to="/account/password" tag="b-dropdown-item" v-if="authenticated"
              active-class="active">
              <font-awesome-icon icon="lock" />
              <span v-text="$t('global.menu.account.password')">Password</span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="logout" v-if="authenticated" v-on:click="logout()" id="logout"
              active-class="active">
              <font-awesome-icon icon="sign-out-alt" />
              <span v-text="$t('global.menu.account.logout')">Sign out</span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="login" v-if="!authenticated" v-on:click="openLogin()" id="login"
              active-class="active">
              <font-awesome-icon icon="sign-in-alt" />
              <span v-text="$t('global.menu.account.login')">Sign in</span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="register" to="/register" tag="b-dropdown-item" id="register" v-if="!authenticated"
              active-class="active">
              <font-awesome-icon icon="user-plus" />
              <span v-text="$t('global.menu.account.register')">Register</span>
            </b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
    <div class="opciones_facultad desktop row mx-0 align-items-center justify-content-end" v-if="!authenticated">
      <div class="col-sm-auto opcion">
        <!--:class="{ 'router-link-active': pathActive('semillero') }"-->
        <router-link to="/faultad-ingenieria/programas-lista">
          <button class="btn btn-opcion d-flex justify-content-center">
            Programas
          </button>
        </router-link>
      </div>
      <div class="col-sm-auto opcion">
        <!--:class="{ 'router-link-active': pathActive('semillero') }"-->
        <router-link to="/faultad-ingenieria/programa-profesorado-lista">
          <button class="btn btn-opcion d-flex justify-content-center">
            Docentes
          </button>
        </router-link>
      </div>
      <div class="col-sm-auto opcion">
        <!--:class="{ 'router-link-active': pathActive('semillero') }"-->
        <router-link :to="{ name: 'laboratorios_lista_public' }">
          <button class="btn btn-opcion d-flex justify-content-center">
            Laboratorios
          </button>
        </router-link>
      </div>
      <div class="col-sm-auto opcion">
        <!--:class="{ 'router-link-active': pathActive('semillero') }"-->
        <router-link :to="{ name: 'semilleros_lista_public' }">
          <button class="btn btn-opcion d-flex justify-content-center">
            Semilleros
          </button>
        </router-link>
      </div>
      <div class="col-sm-auto opcion" v-if="hasLabGranja && !authenticated">
        <!--:class="{ 'router-link-active': pathActive('semillero') }"-->
        <router-link :to="{ name: 'laboratorio_info_granja_public', params: { laboratorioId: labGranja.id } }" replace>
          <button class="btn btn-opcion d-flex justify-content-center">
            Granja
          </button>
        </router-link>
        <!--<a :href="'/faultad-ingenieria/laboratorio-info/' + labGranja.id">
          <button class="btn btn-opcion d-flex justify-content-center">
            Granja
          </button>
        </a>-->
      </div>
      <div class="col-sm-auto opcion" v-if="hasLabMuseo && !authenticated">
        <!--:class="{ 'router-link-active': pathActive('semillero') }"-->
        <router-link :to="{ name: 'laboratorio_info_museo_public', params: { laboratorioId: labMuseo.id } }" replace>
          <button class="btn btn-opcion d-flex justify-content-center">
            Museo
          </button>
        </router-link>
        <!--<a :href="'/faultad-ingenieria/laboratorio-info/' + labMuseo.id">
          <button class="btn btn-opcion d-flex justify-content-center">
            Museo
          </button>
        </a>-->
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./jhi-navbar.component.ts"></script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--<style scoped>

</style>-->
