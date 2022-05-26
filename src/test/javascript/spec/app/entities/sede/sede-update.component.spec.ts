/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import SedeUpdateComponent from '@/entities/sede/sede-update.vue';
import SedeClass from '@/entities/sede/sede-update.component';
import SedeService from '@/entities/sede/sede.service';

import ProgramaService from '@/entities/programa/programa.service';
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
  describe('Sede Management Update Component', () => {
    let wrapper: Wrapper<SedeClass>;
    let comp: SedeClass;
    let sedeServiceStub: SinonStubbedInstance<SedeService>;

    beforeEach(() => {
      sedeServiceStub = sinon.createStubInstance<SedeService>(SedeService);

      wrapper = shallowMount<SedeClass>(SedeUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          sedeService: () => sedeServiceStub,
          alertService: () => new AlertService(),

          programaService: () =>
            sinon.createStubInstance<ProgramaService>(ProgramaService, {
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
        comp.sede = entity;
        sedeServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sedeServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.sede = entity;
        sedeServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sedeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSede = { id: 123 };
        sedeServiceStub.find.resolves(foundSede);
        sedeServiceStub.retrieve.resolves([foundSede]);

        // WHEN
        comp.beforeRouteEnter({ params: { sedeId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.sede).toBe(foundSede);
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
