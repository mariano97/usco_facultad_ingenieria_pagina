import { Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';
import { SwiperOptions } from 'swiper-vue2/types/swiper/swiper-options';
import { Component, Prop, Vue } from 'vue-property-decorator';

import './noticias-fragment.scss';

SwiperCore.use([Navigation, Pagination]);

@Component({
  components: {
    Swiper,
    SwiperSlide,
  },
})
export default class NoticiasFragment extends Vue {

  @Prop(String)
  nombre: string;

  public swiperOptions: SwiperOptions = {
    loop: false,
    navigation: true,
    spaceBetween: 30,
    slidesPerView: 3.5,
    breakpoints: {
      100: {
        slidesPerView: 1,
        spaceBetween: 10,
      },
      1000: {
        slidesPerView: 3.5,
        spaceBetween: 30,
      },
    },
  };

  public getImageUrl (imageId) {
    return `https://picsum.photos/600/400/?image=${imageId}`;
  }

}
