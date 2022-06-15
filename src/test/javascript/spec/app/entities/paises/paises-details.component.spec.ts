/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PaisesDetailComponent from '@/entities/paises/paises-details.vue';
import PaisesClass from '@/entities/paises/paises-details.component';
import PaisesService from '@/entities/paises/paises.service';
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
  describe('Paises Management Detail Component', () => {
    let wrapper: Wrapper<PaisesClass>;
    let comp: PaisesClass;
    let paisesServiceStub: SinonStubbedInstance<PaisesService>;

    beforeEach(() => {
      paisesServiceStub = sinon.createStubInstance<PaisesService>(PaisesService);

      wrapper = shallowMount<PaisesClass>(PaisesDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { paisesService: () => paisesServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPaises = { id: 123 };
        paisesServiceStub.find.resolves(foundPaises);

        // WHEN
        comp.retrievePaises(123);
        await comp.$nextTick();

        // THEN
        expect(comp.paises).toBe(foundPaises);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPaises = { id: 123 };
        paisesServiceStub.find.resolves(foundPaises);

        // WHEN
        comp.beforeRouteEnter({ params: { paisesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.paises).toBe(foundPaises);
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
