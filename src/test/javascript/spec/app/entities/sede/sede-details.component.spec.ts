/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import SedeDetailComponent from '@/entities/sede/sede-details.vue';
import SedeClass from '@/entities/sede/sede-details.component';
import SedeService from '@/entities/sede/sede.service';
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
  describe('Sede Management Detail Component', () => {
    let wrapper: Wrapper<SedeClass>;
    let comp: SedeClass;
    let sedeServiceStub: SinonStubbedInstance<SedeService>;

    beforeEach(() => {
      sedeServiceStub = sinon.createStubInstance<SedeService>(SedeService);

      wrapper = shallowMount<SedeClass>(SedeDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { sedeService: () => sedeServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSede = { id: 123 };
        sedeServiceStub.find.resolves(foundSede);

        // WHEN
        comp.retrieveSede(123);
        await comp.$nextTick();

        // THEN
        expect(comp.sede).toBe(foundSede);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSede = { id: 123 };
        sedeServiceStub.find.resolves(foundSede);

        // WHEN
        comp.beforeRouteEnter({ params: { sedeId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.sede).toBe(foundSede);
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
