/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TablaTiposCatalogoDetailComponent from '@/entities/tabla-tipos-catalogo/tabla-tipos-catalogo-details.vue';
import TablaTiposCatalogoClass from '@/entities/tabla-tipos-catalogo/tabla-tipos-catalogo-details.component';
import TablaTiposCatalogoService from '@/entities/tabla-tipos-catalogo/tabla-tipos-catalogo.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('TablaTiposCatalogo Management Detail Component', () => {
    let wrapper: Wrapper<TablaTiposCatalogoClass>;
    let comp: TablaTiposCatalogoClass;
    let tablaTiposCatalogoServiceStub: SinonStubbedInstance<TablaTiposCatalogoService>;

    beforeEach(() => {
      tablaTiposCatalogoServiceStub = sinon.createStubInstance<TablaTiposCatalogoService>(TablaTiposCatalogoService);

      wrapper = shallowMount<TablaTiposCatalogoClass>(TablaTiposCatalogoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { tablaTiposCatalogoService: () => tablaTiposCatalogoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTablaTiposCatalogo = { id: 123 };
        tablaTiposCatalogoServiceStub.find.resolves(foundTablaTiposCatalogo);

        // WHEN
        comp.retrieveTablaTiposCatalogo(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tablaTiposCatalogo).toBe(foundTablaTiposCatalogo);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTablaTiposCatalogo = { id: 123 };
        tablaTiposCatalogoServiceStub.find.resolves(foundTablaTiposCatalogo);

        // WHEN
        comp.beforeRouteEnter({ params: { tablaTiposCatalogoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tablaTiposCatalogo).toBe(foundTablaTiposCatalogo);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
