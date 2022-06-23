import { uuid } from 'vue-uuid';

export default class UtilsService {
  public generateUUIDIdentifcator(): string {
    return uuid.v4();
  }

  public addHeaderBase64(base64: string): Promise<string> {
    return new Promise<string>(resolve => {
      resolve(this.headerBase64(base64));
    });
  }

  public convertirFileDownloadedToBlobNewTab(fileBase64: string): void {
    this.addHeaderBase64(fileBase64).then(resHeaderBase64 => {
      fetch(resHeaderBase64).then(res => {
        res.blob().then(resBlob => {
          const url = URL.createObjectURL(resBlob);
          window.open(url, '_blank');
        });
      });
    });
  }

  public convertFileDowloadedBase64ToDownload(base64: string, nameFile: string): void {
    this.addHeaderBase64(base64).then(resHeaderBase64 => {
      fetch(resHeaderBase64).then(res => {
        res.blob().then(resBlob => {
          const urlObject = URL.createObjectURL(resBlob);
          const donwloadLink = document.createElement('a');
          donwloadLink.href = urlObject;
          donwloadLink.setAttribute('download', nameFile);
          document.body.appendChild(donwloadLink);
          donwloadLink.click();
          donwloadLink.parentNode!.removeChild(donwloadLink);
        });
      });
    });
  }

  public sizeToMB(value: number): string {
    const valuex = value / 1024 ** 2;
    const s = valuex.toString();
    const l = s.length;
    const decimalLength = s.indexOf('.') + 1;
    let newValue = '';
    if (decimalLength > 0) {
      newValue = s.substr(0, decimalLength + 2);
    } else {
      newValue = s;
    }
    return newValue + 'MB';
  }

  public changeTypeFileToString(typeFile: string): string {
    let newValue = '';
    switch (typeFile.toLowerCase()) {
      case 'image/png':
        newValue = 'PNG';
        break;
      case 'image/jpeg':
        newValue = 'JPEG';
        break;
      case 'image/jpg':
        newValue = 'JPG';
        break;
      case 'application/pdf':
        newValue = 'PDF';
        break;
      default:
        newValue = typeFile;
        break;
    }
    return newValue;
  }

  private headerBase64(fileBase64: string): string {
    const headerBase64 = fileBase64.substring(0, 5);
    let headerBase64Type = '';
    switch (headerBase64.toUpperCase()) {
      case 'IVBOR':
        headerBase64Type = 'data:image/png;base64,';
        break;
      case '/9J/4':
        headerBase64Type = 'data:image/jpg;base64,';
        break;
      case 'JVBER':
        headerBase64Type = 'data:application/pdf;base64,';
        break;
      default:
        headerBase64Type = '';
        break;
    }
    return headerBase64Type + fileBase64;
  }
}
