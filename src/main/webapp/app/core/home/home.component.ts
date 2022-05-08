// import Component from 'vue-class-component';
import { Inject, Vue, Component } from 'vue-property-decorator';
import LoginService from '@/account/login.service';

import { Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';

import 'swiper/swiper-bundle.css';
import './home.scss';
import NoticiasFragment from '@/shared/fragments/noticias/noticias-fragment.vue';


SwiperCore.use([Navigation, Pagination]);
@Component({
  components: {
    Swiper,
    SwiperSlide,
    'fragment-noticias': NoticiasFragment,
  },
})
export default class Home extends Vue {
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
