/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EstadosDetailComponent from '@/entities/estados/estados-details.vue';
import EstadosClass from '@/entities/estados/estados-details.component';
import EstadosService from '@/entities/estados/estados.service';
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
  describe('Estados Management Detail Component', () => {
    let wrapper: Wrapper<EstadosClass>;
    let comp: EstadosClass;
    let estadosServiceStub: SinonStubbedInstance<EstadosService>;

    beforeEach(() => {
      estadosServiceStub = sinon.createStubInstance<EstadosService>(EstadosService);

      wrapper = shallowMount<EstadosClass>(EstadosDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { estadosService: () => estadosServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEstados = { id: 123 };
        estadosServiceStub.find.resolves(foundEstados);

        // WHEN
        comp.retrieveEstados(123);
        await comp.$nextTick();

        // THEN
        expect(comp.estados).toBe(foundEstados);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEstados = { id: 123 };
        estadosServiceStub.find.resolves(foundEstados);

        // WHEN
        comp.beforeRouteEnter({ params: { estadosId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.estados).toBe(foundEstados);
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
