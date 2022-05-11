/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TablaElementoCatalogoDetailComponent from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo-details.vue';
import TablaElementoCatalogoClass from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo-details.component';
import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
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
  describe('TablaElementoCatalogo Management Detail Component', () => {
    let wrapper: Wrapper<TablaElementoCatalogoClass>;
    let comp: TablaElementoCatalogoClass;
    let tablaElementoCatalogoServiceStub: SinonStubbedInstance<TablaElementoCatalogoService>;

    beforeEach(() => {
      tablaElementoCatalogoServiceStub = sinon.createStubInstance<TablaElementoCatalogoService>(TablaElementoCatalogoService);

      wrapper = shallowMount<TablaElementoCatalogoClass>(TablaElementoCatalogoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { tablaElementoCatalogoService: () => tablaElementoCatalogoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTablaElementoCatalogo = { id: 123 };
        tablaElementoCatalogoServiceStub.find.resolves(foundTablaElementoCatalogo);

        // WHEN
        comp.retrieveTablaElementoCatalogo(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tablaElementoCatalogo).toBe(foundTablaElementoCatalogo);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTablaElementoCatalogo = { id: 123 };
        tablaElementoCatalogoServiceStub.find.resolves(foundTablaElementoCatalogo);

        // WHEN
        comp.beforeRouteEnter({ params: { tablaElementoCatalogoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tablaElementoCatalogo).toBe(foundTablaElementoCatalogo);
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
