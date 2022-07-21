// import Component from 'vue-class-component';
import { Inject, Vue, Component, Provide } from 'vue-property-decorator';
import LoginService from '@/account/login.service';

import { Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';

import 'swiper/swiper-bundle.css';
import './home.scss';
import NoticiasFragment from '@/shared/fragments/noticias/noticias-fragment.vue';
import EventosFragment from '@/shared/fragments/eventos/eventos-fragment.vue';
import EventoService from '@/entities/evento/evento.service';
import NoticiaService from '@/entities/noticia/noticia.service';
import GoogleStorageService from '@/shared/services/google-storage.service';


SwiperCore.use([Navigation, Pagination]);
@Component({
  components: {
    Swiper,
    SwiperSlide,
    'fragment-noticias': NoticiasFragment,
    'fragment-eventos': EventosFragment,
  },
})
export default class Home extends Vue {
  @Provide('noticiaService') private noticiaService = () => new NoticiaService();
  @Provide('eventoService') private eventoService = () => new EventoService();
  @Provide('googleStorageService') private googleStorageService = () => new GoogleStorageService();

  @Inject('loginService')
  private loginService: () => LoginService;

  public openLogin(): void {
    this.loginService().openLogin((<any>this).$root);
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }

  public getImageUrl (imageId) {
    return `https://picsum.photos/600/400/?image=${imageId}`;
  }

  public onSwiper (swiper) {
    console.warn(swiper);
  }

  public onSlideChange () {
    console.warn('slide change');
  }

}
