/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import SedeComponent from '@/entities/sede/sede.vue';
import SedeClass from '@/entities/sede/sede.component';
import SedeService from '@/entities/sede/sede.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Sede Management Component', () => {
    let wrapper: Wrapper<SedeClass>;
    let comp: SedeClass;
    let sedeServiceStub: SinonStubbedInstance<SedeService>;

    beforeEach(() => {
      sedeServiceStub = sinon.createStubInstance<SedeService>(SedeService);
      sedeServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<SedeClass>(SedeComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          sedeService: () => sedeServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      sedeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllSedes();
      await comp.$nextTick();

      // THEN
      expect(sedeServiceStub.retrieve.called).toBeTruthy();
      expect(comp.sedes[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      sedeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(sedeServiceStub.retrieve.called).toBeTruthy();
      expect(comp.sedes[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      sedeServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(sedeServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      sedeServiceStub.retrieve.reset();
      sedeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(sedeServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.sedes[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      sedeServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(sedeServiceStub.retrieve.callCount).toEqual(1);

      comp.removeSede();
      await comp.$nextTick();

      // THEN
      expect(sedeServiceStub.delete.called).toBeTruthy();
      expect(sedeServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
