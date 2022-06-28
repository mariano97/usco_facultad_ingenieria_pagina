/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ProfesorUpdateComponent from '@/entities/profesor/profesor-update.vue';
import ProfesorClass from '@/entities/profesor/profesor-update.component';
import ProfesorService from '@/entities/profesor/profesor.service';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';

import FacultadService from '@/entities/facultad/facultad.service';

import ProgramaService from '@/entities/programa/programa.service';

import CursoMateriaService from '@/entities/curso-materia/curso-materia.service';
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
  describe('Profesor Management Update Component', () => {
    let wrapper: Wrapper<ProfesorClass>;
    let comp: ProfesorClass;
    let profesorServiceStub: SinonStubbedInstance<ProfesorService>;

    beforeEach(() => {
      profesorServiceStub = sinon.createStubInstance<ProfesorService>(ProfesorService);

      wrapper = shallowMount<ProfesorClass>(ProfesorUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          profesorService: () => profesorServiceStub,
          alertService: () => new AlertService(),

          tablaElementoCatalogoService: () =>
            sinon.createStubInstance<TablaElementoCatalogoService>(TablaElementoCatalogoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          facultadService: () =>
            sinon.createStubInstance<FacultadService>(FacultadService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          programaService: () =>
            sinon.createStubInstance<ProgramaService>(ProgramaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          cursoMateriaService: () =>
            sinon.createStubInstance<CursoMateriaService>(CursoMateriaService, {
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
        comp.profesor = entity;
        profesorServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(profesorServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.profesor = entity;
        profesorServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(profesorServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProfesor = { id: 123 };
        profesorServiceStub.find.resolves(foundProfesor);
        profesorServiceStub.retrieve.resolves([foundProfesor]);

        // WHEN
        comp.beforeRouteEnter({ params: { profesorId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.profesor).toBe(foundProfesor);
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
