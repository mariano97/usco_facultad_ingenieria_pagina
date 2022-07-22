/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EscalafonProfesorDetailComponent from '@/entities/escalafon-profesor/escalafon-profesor-details.vue';
import EscalafonProfesorClass from '@/entities/escalafon-profesor/escalafon-profesor-details.component';
import EscalafonProfesorService from '@/entities/escalafon-profesor/escalafon-profesor.service';
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
  describe('EscalafonProfesor Management Detail Component', () => {
    let wrapper: Wrapper<EscalafonProfesorClass>;
    let comp: EscalafonProfesorClass;
    let escalafonProfesorServiceStub: SinonStubbedInstance<EscalafonProfesorService>;

    beforeEach(() => {
      escalafonProfesorServiceStub = sinon.createStubInstance<EscalafonProfesorService>(EscalafonProfesorService);

      wrapper = shallowMount<EscalafonProfesorClass>(EscalafonProfesorDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { escalafonProfesorService: () => escalafonProfesorServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEscalafonProfesor = { id: 123 };
        escalafonProfesorServiceStub.find.resolves(foundEscalafonProfesor);

        // WHEN
        comp.retrieveEscalafonProfesor(123);
        await comp.$nextTick();

        // THEN
        expect(comp.escalafonProfesor).toBe(foundEscalafonProfesor);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEscalafonProfesor = { id: 123 };
        escalafonProfesorServiceStub.find.resolves(foundEscalafonProfesor);

        // WHEN
        comp.beforeRouteEnter({ params: { escalafonProfesorId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.escalafonProfesor).toBe(foundEscalafonProfesor);
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
