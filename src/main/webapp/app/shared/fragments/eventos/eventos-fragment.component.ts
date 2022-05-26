import { Navigation, Pagination } from 'swiper';
import { SwiperCore, Swiper, SwiperSlide } from 'swiper-vue2';
import { Component, Vue } from 'vue-property-decorator';
import 'swiper/swiper-bundle.css';
import './eventos-fragment.scss';

SwiperCore.use([Navigation, Pagination]);

@Component({
  components: {
    Swiper,
    SwiperSlide,
  },
})
export default class EventosFragment extends Vue {
  public getImageUrl (imageId) {
    return `https://picsum.photos/600/400/?image=${imageId}`;
  }
}
