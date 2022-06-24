/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CursoMateriaDetailComponent from '@/entities/curso-materia/curso-materia-details.vue';
import CursoMateriaClass from '@/entities/curso-materia/curso-materia-details.component';
import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';
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
  describe('CursoMateria Management Detail Component', () => {
    let wrapper: Wrapper<CursoMateriaClass>;
    let comp: CursoMateriaClass;
    let cursoMateriaServiceStub: SinonStubbedInstance<CursoMateriaService>;

    beforeEach(() => {
      cursoMateriaServiceStub = sinon.createStubInstance<CursoMateriaService>(CursoMateriaService);

      wrapper = shallowMount<CursoMateriaClass>(CursoMateriaDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { cursoMateriaService: () => cursoMateriaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCursoMateria = { id: 123 };
        cursoMateriaServiceStub.find.resolves(foundCursoMateria);

        // WHEN
        comp.retrieveCursoMateria(123);
        await comp.$nextTick();

        // THEN
        expect(comp.cursoMateria).toBe(foundCursoMateria);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCursoMateria = { id: 123 };
        cursoMateriaServiceStub.find.resolves(foundCursoMateria);

        // WHEN
        comp.beforeRouteEnter({ params: { cursoMateriaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.cursoMateria).toBe(foundCursoMateria);
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
