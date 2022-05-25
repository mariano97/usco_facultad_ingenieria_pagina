import ProgramaService from '@/entities/programa/programa.service';
import { DATE_FORMAT, DATE_FORMAT_FULL_MONTH } from '@/shared/date/filters';
import { IPrograma } from '@/shared/model/programa.model';
import dayjs from 'dayjs';
import { Vue, Component, Inject } from 'vue-property-decorator';
import './programa.scss';

@Component
export default class Programa extends Vue {

  private codigoSniesTemp = this.$route.params.codigo_snies;

  @Inject('programaService') private programaService: () => ProgramaService;

  public programa: IPrograma = {};

  public created(): void {
    this.consultarPrograma();
  }

  private consultarPrograma(): void {
    this.programa = {};
    this.programaService()
      .findByCodigoSnies(this.$store.getters.authenticated, Number(this.codigoSniesTemp))
      .then(res => {
        res.fechaRegistroCalificado = new Date(res.fechaRegistroCalificado);
        this.programa = res;
      })
      .catch(err => {
        console.error(err);
      });
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_FORMAT_FULL_MONTH);
    }
    return null;
  }
}
