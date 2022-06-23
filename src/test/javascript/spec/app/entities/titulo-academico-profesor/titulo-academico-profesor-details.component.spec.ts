/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TituloAcademicoProfesorDetailComponent from '@/entities/titulo-academico-profesor/titulo-academico-profesor-details.vue';
import TituloAcademicoProfesorClass from '@/entities/titulo-academico-profesor/titulo-academico-profesor-details.component';
import TituloAcademicoProfesorService from '@/entities/titulo-academico-profesor/titulo-academico-profesor.service';
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
  describe('TituloAcademicoProfesor Management Detail Component', () => {
    let wrapper: Wrapper<TituloAcademicoProfesorClass>;
    let comp: TituloAcademicoProfesorClass;
    let tituloAcademicoProfesorServiceStub: SinonStubbedInstance<TituloAcademicoProfesorService>;

    beforeEach(() => {
      tituloAcademicoProfesorServiceStub = sinon.createStubInstance<TituloAcademicoProfesorService>(TituloAcademicoProfesorService);

      wrapper = shallowMount<TituloAcademicoProfesorClass>(TituloAcademicoProfesorDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { tituloAcademicoProfesorService: () => tituloAcademicoProfesorServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTituloAcademicoProfesor = { id: 123 };
        tituloAcademicoProfesorServiceStub.find.resolves(foundTituloAcademicoProfesor);

        // WHEN
        comp.retrieveTituloAcademicoProfesor(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tituloAcademicoProfesor).toBe(foundTituloAcademicoProfesor);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTituloAcademicoProfesor = { id: 123 };
        tituloAcademicoProfesorServiceStub.find.resolves(foundTituloAcademicoProfesor);

        // WHEN
        comp.beforeRouteEnter({ params: { tituloAcademicoProfesorId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tituloAcademicoProfesor).toBe(foundTituloAcademicoProfesor);
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
