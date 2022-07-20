/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import SemilleroUpdateComponent from '@/entities/semillero/semillero-update.vue';
import SemilleroClass from '@/entities/semillero/semillero-update.component';
import SemilleroService from '@/entities/semillero/semillero.service';

import FacultadService from '@/entities/facultad/facultad.service';

import ProfesorService from '@/entities/profesor/profesor.service';
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
  describe('Semillero Management Update Component', () => {
    let wrapper: Wrapper<SemilleroClass>;
    let comp: SemilleroClass;
    let semilleroServiceStub: SinonStubbedInstance<SemilleroService>;

    beforeEach(() => {
      semilleroServiceStub = sinon.createStubInstance<SemilleroService>(SemilleroService);

      wrapper = shallowMount<SemilleroClass>(SemilleroUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          semilleroService: () => semilleroServiceStub,
          alertService: () => new AlertService(),

          facultadService: () =>
            sinon.createStubInstance<FacultadService>(FacultadService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          profesorService: () =>
            sinon.createStubInstance<ProfesorService>(ProfesorService, {
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
        comp.semillero = entity;
        semilleroServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(semilleroServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.semillero = entity;
        semilleroServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(semilleroServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSemillero = { id: 123 };
        semilleroServiceStub.find.resolves(foundSemillero);
        semilleroServiceStub.retrieve.resolves([foundSemillero]);

        // WHEN
        comp.beforeRouteEnter({ params: { semilleroId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.semillero).toBe(foundSemillero);
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
