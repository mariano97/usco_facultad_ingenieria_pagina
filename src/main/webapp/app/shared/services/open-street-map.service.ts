import { IOpenStreetMap } from './../model/open-street-map.model';
import axios from 'axios';

const baseApi = 'https://nominatim.openstreetmap.org';

export default class OpenStreetMapService {
  public consultarReverseData(format: 'jsonv2' | 'json' | 'xml' | 'geojson' | 'geocodejson', latitud: number, longitud: number): Promise<IOpenStreetMap> {
    return new Promise<IOpenStreetMap>((resolve, reject) => {
      axios
        .get(`${baseApi}/reverse`, {
          params: {
            format: format,
            lat: latitud,
            lon: longitud,
          },
        })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
