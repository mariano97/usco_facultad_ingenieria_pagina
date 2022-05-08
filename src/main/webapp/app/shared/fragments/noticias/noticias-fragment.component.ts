import { Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';
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

  public getImageUrl (imageId) {
    return `https://picsum.photos/600/400/?image=${imageId}`;
  }

}
