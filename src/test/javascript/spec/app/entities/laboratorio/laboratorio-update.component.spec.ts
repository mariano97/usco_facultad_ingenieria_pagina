/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import LaboratorioUpdateComponent from '@/entities/laboratorio/laboratorio-update.vue';
import LaboratorioClass from '@/entities/laboratorio/laboratorio-update.component';
import LaboratorioService from '@/entities/laboratorio/laboratorio.service';

import TablaElementoCatalogoService from '@/entities/tabla-elemento-catalogo/tabla-elemento-catalogo.service';

import FacultadService from '@/entities/facultad/facultad.service';
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
  describe('Laboratorio Management Update Component', () => {
    let wrapper: Wrapper<LaboratorioClass>;
    let comp: LaboratorioClass;
    let laboratorioServiceStub: SinonStubbedInstance<LaboratorioService>;

    beforeEach(() => {
      laboratorioServiceStub = sinon.createStubInstance<LaboratorioService>(LaboratorioService);

      wrapper = shallowMount<LaboratorioClass>(LaboratorioUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          laboratorioService: () => laboratorioServiceStub,
          alertService: () => new AlertService(),

          tablaElementoCatalogoService: () =>
            sinon.createStubInstance<TablaElementoCatalogoService>(TablaElementoCatalogoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          facultadService: () =>
            sinon.createStubInstance<FacultadService>(FacultadService, {
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
        comp.laboratorio = entity;
        laboratorioServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(laboratorioServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.laboratorio = entity;
        laboratorioServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(laboratorioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLaboratorio = { id: 123 };
        laboratorioServiceStub.find.resolves(foundLaboratorio);
        laboratorioServiceStub.retrieve.resolves([foundLaboratorio]);

        // WHEN
        comp.beforeRouteEnter({ params: { laboratorioId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.laboratorio).toBe(foundLaboratorio);
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
