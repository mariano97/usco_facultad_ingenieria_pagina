/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import RedesProgramaDetailComponent from '@/entities/redes-programa/redes-programa-details.vue';
import RedesProgramaClass from '@/entities/redes-programa/redes-programa-details.component';
import RedesProgramaService from '@/entities/redes-programa/redes-programa.service';
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
  describe('RedesPrograma Management Detail Component', () => {
    let wrapper: Wrapper<RedesProgramaClass>;
    let comp: RedesProgramaClass;
    let redesProgramaServiceStub: SinonStubbedInstance<RedesProgramaService>;

    beforeEach(() => {
      redesProgramaServiceStub = sinon.createStubInstance<RedesProgramaService>(RedesProgramaService);

      wrapper = shallowMount<RedesProgramaClass>(RedesProgramaDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { redesProgramaService: () => redesProgramaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundRedesPrograma = { id: 123 };
        redesProgramaServiceStub.find.resolves(foundRedesPrograma);

        // WHEN
        comp.retrieveRedesPrograma(123);
        await comp.$nextTick();

        // THEN
        expect(comp.redesPrograma).toBe(foundRedesPrograma);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRedesPrograma = { id: 123 };
        redesProgramaServiceStub.find.resolves(foundRedesPrograma);

        // WHEN
        comp.beforeRouteEnter({ params: { redesProgramaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.redesPrograma).toBe(foundRedesPrograma);
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
