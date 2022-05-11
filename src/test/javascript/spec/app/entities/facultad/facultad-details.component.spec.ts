/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FacultadDetailComponent from '@/entities/facultad/facultad-details.vue';
import FacultadClass from '@/entities/facultad/facultad-details.component';
import FacultadService from '@/entities/facultad/facultad.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Facultad Management Detail Component', () => {
    let wrapper: Wrapper<FacultadClass>;
    let comp: FacultadClass;
    let facultadServiceStub: SinonStubbedInstance<FacultadService>;

    beforeEach(() => {
      facultadServiceStub = sinon.createStubInstance<FacultadService>(FacultadService);

      wrapper = shallowMount<FacultadClass>(FacultadDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { facultadService: () => facultadServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFacultad = { id: 123 };
        facultadServiceStub.find.resolves(foundFacultad);

        // WHEN
        comp.retrieveFacultad(123);
        await comp.$nextTick();

        // THEN
        expect(comp.facultad).toBe(foundFacultad);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFacultad = { id: 123 };
        facultadServiceStub.find.resolves(foundFacultad);

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
