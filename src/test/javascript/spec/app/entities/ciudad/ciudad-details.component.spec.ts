/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CiudadDetailComponent from '@/entities/ciudad/ciudad-details.vue';
import CiudadClass from '@/entities/ciudad/ciudad-details.component';
import CiudadService from '@/entities/ciudad/ciudad.service';
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
  describe('Ciudad Management Detail Component', () => {
    let wrapper: Wrapper<CiudadClass>;
    let comp: CiudadClass;
    let ciudadServiceStub: SinonStubbedInstance<CiudadService>;

    beforeEach(() => {
      ciudadServiceStub = sinon.createStubInstance<CiudadService>(CiudadService);

      wrapper = shallowMount<CiudadClass>(CiudadDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { ciudadService: () => ciudadServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCiudad = { id: 123 };
        ciudadServiceStub.find.resolves(foundCiudad);

        // WHEN
        comp.retrieveCiudad(123);
        await comp.$nextTick();

        // THEN
        expect(comp.ciudad).toBe(foundCiudad);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCiudad = { id: 123 };
        ciudadServiceStub.find.resolves(foundCiudad);

        // WHEN
        comp.beforeRouteEnter({ params: { ciudadId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.ciudad).toBe(foundCiudad);
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
