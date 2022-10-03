// import Component from 'vue-class-component';
import { Inject, Vue, Component, Provide } from 'vue-property-decorator';
import LoginService from '@/account/login.service';

import { Autoplay, Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';

import 'swiper/swiper-bundle.css';
import './home.scss';
import NoticiasFragment from '@/shared/fragments/noticias/noticias-fragment.vue';
import EventosFragment from '@/shared/fragments/eventos/eventos-fragment.vue';
import EventoService from '@/entities/evento/evento.service';
import NoticiaService from '@/entities/noticia/noticia.service';
import GoogleStorageService from '@/shared/services/google-storage.service';


SwiperCore.use([Navigation, Pagination, Autoplay]);
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

  public imgArray = [
    '/content/images/imagenes_usco/ussco_entrada_principal.jpeg',
    '/content/images/imagenes_usco/estudiantes_usco.jpeg',
    '/content/images/imagenes_usco/facultad_ingenieria.jpeg',
    '/content/images/imagenes_usco/ESTUDIANTES-QUIMBO-PARA-SUBIR-A-LA-WEB1.jpeg',
    '/content/images/imagenes_usco/museo_usco.jpeg',
    '/content/images/imagenes_usco/granja_experimental.jpeg',
    '/content/images/imagenes_usco/granaja.jpeg',
  ];

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

  public getImageUrl(imageId) {
    /* const imgArray = ['https://www.uv.mx/prensa/files/2016/11/291116.-Laboratorio-de-ingenieria-aplicada-5-ok.jpg', 'https://cms.tecnalia.com/uploads/2020/06/tecnalia-laboratorio-ingenieria-superficies-1920x1270.jpg',
      'https://www.uv.mx/prensa/files/2016/11/291116.-Laboratorio-de-ingenieria-aplicada-5-ok.jpg', 'https://2.bp.blogspot.com/-GxJFShNRmkU/WL2Ur1-hwmI/AAAAAAAAEGQ/rxeLRi2whzsuOz5Bbxgoyk75_u05AdqHACLcB/s1600/IMG_0713.JPG',
      'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRELeb1lKYRNe08Xwzy3n-AerlOHPX4oyC9xQ&usqp=CAU', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSW15kvtSkjsz7ECmm-msBBpyin85-j3W9WUg&usqp=CAU',
      'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQe8Z4lbQder9qoNM9rmtvZ_DKNkMX9p2CmtA&usqp=CAU']; */
    // return `https://picsum.photos/600/400/?image=${imageId}`;


    return this.imgArray[imageId - 1];
  }

  public onSwiper (swiper) {
    console.warn(swiper);
  }

  public onSlideChange () {
    console.warn('slide change');
  }

}
