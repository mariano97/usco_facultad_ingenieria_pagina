/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import NoticiaDetailComponent from '@/entities/noticia/noticia-details.vue';
import NoticiaClass from '@/entities/noticia/noticia-details.component';
import NoticiaService from '@/entities/noticia/noticia.service';
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
  describe('Noticia Management Detail Component', () => {
    let wrapper: Wrapper<NoticiaClass>;
    let comp: NoticiaClass;
    let noticiaServiceStub: SinonStubbedInstance<NoticiaService>;

    beforeEach(() => {
      noticiaServiceStub = sinon.createStubInstance<NoticiaService>(NoticiaService);

      wrapper = shallowMount<NoticiaClass>(NoticiaDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { noticiaService: () => noticiaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundNoticia = { id: 123 };
        noticiaServiceStub.find.resolves(foundNoticia);

        // WHEN
        comp.retrieveNoticia(123);
        await comp.$nextTick();

        // THEN
        expect(comp.noticia).toBe(foundNoticia);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundNoticia = { id: 123 };
        noticiaServiceStub.find.resolves(foundNoticia);

        // WHEN
        comp.beforeRouteEnter({ params: { noticiaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.noticia).toBe(foundNoticia);
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
