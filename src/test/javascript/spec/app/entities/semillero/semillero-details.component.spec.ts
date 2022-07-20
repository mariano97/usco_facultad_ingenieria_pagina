/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import SemilleroDetailComponent from '@/entities/semillero/semillero-details.vue';
import SemilleroClass from '@/entities/semillero/semillero-details.component';
import SemilleroService from '@/entities/semillero/semillero.service';
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
  describe('Semillero Management Detail Component', () => {
    let wrapper: Wrapper<SemilleroClass>;
    let comp: SemilleroClass;
    let semilleroServiceStub: SinonStubbedInstance<SemilleroService>;

    beforeEach(() => {
      semilleroServiceStub = sinon.createStubInstance<SemilleroService>(SemilleroService);

      wrapper = shallowMount<SemilleroClass>(SemilleroDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { semilleroService: () => semilleroServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSemillero = { id: 123 };
        semilleroServiceStub.find.resolves(foundSemillero);

        // WHEN
        comp.retrieveSemillero(123);
        await comp.$nextTick();

        // THEN
        expect(comp.semillero).toBe(foundSemillero);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSemillero = { id: 123 };
        semilleroServiceStub.find.resolves(foundSemillero);

        // WHEN
        comp.beforeRouteEnter({ params: { semilleroId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.semillero).toBe(foundSemillero);
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
