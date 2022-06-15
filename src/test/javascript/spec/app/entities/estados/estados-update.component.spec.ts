/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import EstadosUpdateComponent from '@/entities/estados/estados-update.vue';
import EstadosClass from '@/entities/estados/estados-update.component';
import EstadosService from '@/entities/estados/estados.service';

import PaisesService from '@/entities/paises/paises.service';
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
  describe('Estados Management Update Component', () => {
    let wrapper: Wrapper<EstadosClass>;
    let comp: EstadosClass;
    let estadosServiceStub: SinonStubbedInstance<EstadosService>;

    beforeEach(() => {
      estadosServiceStub = sinon.createStubInstance<EstadosService>(EstadosService);

      wrapper = shallowMount<EstadosClass>(EstadosUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          estadosService: () => estadosServiceStub,
          alertService: () => new AlertService(),

          paisesService: () =>
            sinon.createStubInstance<PaisesService>(PaisesService, {
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
        comp.estados = entity;
        estadosServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadosServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.estados = entity;
        estadosServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadosServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEstados = { id: 123 };
        estadosServiceStub.find.resolves(foundEstados);
        estadosServiceStub.retrieve.resolves([foundEstados]);

        // WHEN
        comp.beforeRouteEnter({ params: { estadosId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.estados).toBe(foundEstados);
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
