/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CiudadUpdateComponent from '@/entities/ciudad/ciudad-update.vue';
import CiudadClass from '@/entities/ciudad/ciudad-update.component';
import CiudadService from '@/entities/ciudad/ciudad.service';

import EstadosService from '@/entities/estados/estados.service';
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
  describe('Ciudad Management Update Component', () => {
    let wrapper: Wrapper<CiudadClass>;
    let comp: CiudadClass;
    let ciudadServiceStub: SinonStubbedInstance<CiudadService>;

    beforeEach(() => {
      ciudadServiceStub = sinon.createStubInstance<CiudadService>(CiudadService);

      wrapper = shallowMount<CiudadClass>(CiudadUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          ciudadService: () => ciudadServiceStub,
          alertService: () => new AlertService(),

          estadosService: () =>
            sinon.createStubInstance<EstadosService>(EstadosService, {
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
        comp.ciudad = entity;
        ciudadServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ciudadServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.ciudad = entity;
        ciudadServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ciudadServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCiudad = { id: 123 };
        ciudadServiceStub.find.resolves(foundCiudad);
        ciudadServiceStub.retrieve.resolves([foundCiudad]);

        // WHEN
        comp.beforeRouteEnter({ params: { ciudadId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.ciudad).toBe(foundCiudad);
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
