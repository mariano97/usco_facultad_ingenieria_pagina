/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ArchivosProgramaDetailComponent from '@/entities/archivos-programa/archivos-programa-details.vue';
import ArchivosProgramaClass from '@/entities/archivos-programa/archivos-programa-details.component';
import ArchivosProgramaService from '@/entities/archivos-programa/archivos-programa.service';
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
  describe('ArchivosPrograma Management Detail Component', () => {
    let wrapper: Wrapper<ArchivosProgramaClass>;
    let comp: ArchivosProgramaClass;
    let archivosProgramaServiceStub: SinonStubbedInstance<ArchivosProgramaService>;

    beforeEach(() => {
      archivosProgramaServiceStub = sinon.createStubInstance<ArchivosProgramaService>(ArchivosProgramaService);

      wrapper = shallowMount<ArchivosProgramaClass>(ArchivosProgramaDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { archivosProgramaService: () => archivosProgramaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundArchivosPrograma = { id: 123 };
        archivosProgramaServiceStub.find.resolves(foundArchivosPrograma);

        // WHEN
        comp.retrieveArchivosPrograma(123);
        await comp.$nextTick();

        // THEN
        expect(comp.archivosPrograma).toBe(foundArchivosPrograma);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundArchivosPrograma = { id: 123 };
        archivosProgramaServiceStub.find.resolves(foundArchivosPrograma);

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
