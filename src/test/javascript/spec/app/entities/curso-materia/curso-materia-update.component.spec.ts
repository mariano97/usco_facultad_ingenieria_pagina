/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CursoMateriaUpdateComponent from '@/entities/curso-materia/curso-materia-update.vue';
import CursoMateriaClass from '@/entities/curso-materia/curso-materia-update.component';
import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('CursoMateria Management Update Component', () => {
    let wrapper: Wrapper<CursoMateriaClass>;
    let comp: CursoMateriaClass;
    let cursoMateriaServiceStub: SinonStubbedInstance<CursoMateriaService>;

    beforeEach(() => {
      cursoMateriaServiceStub = sinon.createStubInstance<CursoMateriaService>(CursoMateriaService);

      wrapper = shallowMount<CursoMateriaClass>(CursoMateriaUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          cursoMateriaService: () => cursoMateriaServiceStub,
          alertService: () => new AlertService(),

          tablaElementoCatalogoService: () =>
            sinon.createStubInstance<TablaElementoCatalogoService>(TablaElementoCatalogoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.cursoMateria = entity;
        cursoMateriaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cursoMateriaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.cursoMateria = entity;
        cursoMateriaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cursoMateriaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCursoMateria = { id: 123 };
        cursoMateriaServiceStub.find.resolves(foundCursoMateria);
        cursoMateriaServiceStub.retrieve.resolves([foundCursoMateria]);

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
