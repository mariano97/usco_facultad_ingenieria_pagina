/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FacultadUpdateComponent from '@/entities/facultad/facultad-update.vue';
import FacultadClass from '@/entities/facultad/facultad-update.component';
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
  describe('Facultad Management Update Component', () => {
    let wrapper: Wrapper<FacultadClass>;
    let comp: FacultadClass;
    let facultadServiceStub: SinonStubbedInstance<FacultadService>;

    beforeEach(() => {
      facultadServiceStub = sinon.createStubInstance<FacultadService>(FacultadService);

      wrapper = shallowMount<FacultadClass>(FacultadUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          facultadService: () => facultadServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.facultad = entity;
        facultadServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(facultadServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.facultad = entity;
        facultadServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(facultadServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFacultad = { id: 123 };
        facultadServiceStub.find.resolves(foundFacultad);
        facultadServiceStub.retrieve.resolves([foundFacultad]);

        // WHEN
        comp.beforeRouteEnter({ params: { facultadId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.facultad).toBe(foundFacultad);
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
