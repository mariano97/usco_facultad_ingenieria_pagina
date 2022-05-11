/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import ProgramaUpdateComponent from '@/entities/programa/programa-update.vue';
import ProgramaClass from '@/entities/programa/programa-update.component';
import ProgramaService from '@/entities/programa/programa.service';

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
  describe('Programa Management Update Component', () => {
    let wrapper: Wrapper<ProgramaClass>;
    let comp: ProgramaClass;
    let programaServiceStub: SinonStubbedInstance<ProgramaService>;

    beforeEach(() => {
      programaServiceStub = sinon.createStubInstance<ProgramaService>(ProgramaService);

      wrapper = shallowMount<ProgramaClass>(ProgramaUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          programaService: () => programaServiceStub,
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

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.programa = entity;
        programaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(programaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.programa = entity;
        programaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(programaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPrograma = { id: 123 };
        programaServiceStub.find.resolves(foundPrograma);
        programaServiceStub.retrieve.resolves([foundPrograma]);

        // WHEN
        comp.beforeRouteEnter({ params: { programaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.programa).toBe(foundPrograma);
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
