/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ArchivosProgramaUpdateComponent from '@/entities/archivos-programa/archivos-programa-update.vue';
import ArchivosProgramaClass from '@/entities/archivos-programa/archivos-programa-update.component';
import ArchivosProgramaService from '@/entities/archivos-programa/archivos-programa.service';

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
  describe('ArchivosPrograma Management Update Component', () => {
    let wrapper: Wrapper<ArchivosProgramaClass>;
    let comp: ArchivosProgramaClass;
    let archivosProgramaServiceStub: SinonStubbedInstance<ArchivosProgramaService>;

    beforeEach(() => {
      archivosProgramaServiceStub = sinon.createStubInstance<ArchivosProgramaService>(ArchivosProgramaService);

      wrapper = shallowMount<ArchivosProgramaClass>(ArchivosProgramaUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          archivosProgramaService: () => archivosProgramaServiceStub,
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
        comp.archivosPrograma = entity;
        archivosProgramaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(archivosProgramaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.archivosPrograma = entity;
        archivosProgramaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(archivosProgramaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundArchivosPrograma = { id: 123 };
        archivosProgramaServiceStub.find.resolves(foundArchivosPrograma);
        archivosProgramaServiceStub.retrieve.resolves([foundArchivosPrograma]);

        // WHEN
        comp.beforeRouteEnter({ params: { archivosProgramaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.archivosPrograma).toBe(foundArchivosPrograma);
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
