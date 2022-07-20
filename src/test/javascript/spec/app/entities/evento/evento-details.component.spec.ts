/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EventoDetailComponent from '@/entities/evento/evento-details.vue';
import EventoClass from '@/entities/evento/evento-details.component';
import EventoService from '@/entities/evento/evento.service';
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
  describe('Evento Management Detail Component', () => {
    let wrapper: Wrapper<EventoClass>;
    let comp: EventoClass;
    let eventoServiceStub: SinonStubbedInstance<EventoService>;

    beforeEach(() => {
      eventoServiceStub = sinon.createStubInstance<EventoService>(EventoService);

      wrapper = shallowMount<EventoClass>(EventoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { eventoService: () => eventoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEvento = { id: 123 };
        eventoServiceStub.find.resolves(foundEvento);

        // WHEN
        comp.retrieveEvento(123);
        await comp.$nextTick();

        // THEN
        expect(comp.evento).toBe(foundEvento);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEvento = { id: 123 };
        eventoServiceStub.find.resolves(foundEvento);

        // WHEN
        comp.beforeRouteEnter({ params: { eventoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.evento).toBe(foundEvento);
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
