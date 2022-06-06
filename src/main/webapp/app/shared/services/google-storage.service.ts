import axios from 'axios';

const baseApiUrlOpen = 'api/open/google-cloud-storage';
const baseApiUrl = 'api/google-cloud-storage';

export default class GoogleStorageService {
  public downloadFile(fileName: string, generation: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrlOpen}`, {
          params: {
            fileName:
              'programas/software/docs3/08807ffe-7cd5-47a7-afde-b60f49444133_Captura_de_Pantalla_2022-04-28_a_la_s__6.15.02_a._m..png',
            generation: 1654461577871886,
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

  public uploadFile(file: File, nameCarpeta: string): Promise<string> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return new Promise<string>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/upload`, formdata, {
          params: {
            carpeta: nameCarpeta,
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
