/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PaisesUpdateComponent from '@/entities/paises/paises-update.vue';
import PaisesClass from '@/entities/paises/paises-update.component';
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
  describe('Paises Management Update Component', () => {
    let wrapper: Wrapper<PaisesClass>;
    let comp: PaisesClass;
    let paisesServiceStub: SinonStubbedInstance<PaisesService>;

    beforeEach(() => {
      paisesServiceStub = sinon.createStubInstance<PaisesService>(PaisesService);

      wrapper = shallowMount<PaisesClass>(PaisesUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          paisesService: () => paisesServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.paises = entity;
        paisesServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paisesServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.paises = entity;
        paisesServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paisesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPaises = { id: 123 };
        paisesServiceStub.find.resolves(foundPaises);
        paisesServiceStub.retrieve.resolves([foundPaises]);

        // WHEN
        comp.beforeRouteEnter({ params: { paisesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.paises).toBe(foundPaises);
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
