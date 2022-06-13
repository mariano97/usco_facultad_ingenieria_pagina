/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import RedesProgramaUpdateComponent from '@/entities/redes-programa/redes-programa-update.vue';
import RedesProgramaClass from '@/entities/redes-programa/redes-programa-update.component';
import RedesProgramaService from '@/entities/redes-programa/redes-programa.service';

import ProgramaService from '@/entities/programa/programa.service';

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
  describe('RedesPrograma Management Update Component', () => {
    let wrapper: Wrapper<RedesProgramaClass>;
    let comp: RedesProgramaClass;
    let redesProgramaServiceStub: SinonStubbedInstance<RedesProgramaService>;

    beforeEach(() => {
      redesProgramaServiceStub = sinon.createStubInstance<RedesProgramaService>(RedesProgramaService);

      wrapper = shallowMount<RedesProgramaClass>(RedesProgramaUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          redesProgramaService: () => redesProgramaServiceStub,
          alertService: () => new AlertService(),

          programaService: () =>
            sinon.createStubInstance<ProgramaService>(ProgramaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

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
        comp.redesPrograma = entity;
        redesProgramaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(redesProgramaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.redesPrograma = entity;
        redesProgramaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(redesProgramaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRedesPrograma = { id: 123 };
        redesProgramaServiceStub.find.resolves(foundRedesPrograma);
        redesProgramaServiceStub.retrieve.resolves([foundRedesPrograma]);

        // WHEN
        comp.beforeRouteEnter({ params: { redesProgramaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.redesPrograma).toBe(foundRedesPrograma);
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
