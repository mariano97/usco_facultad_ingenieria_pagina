
export default class UtilsService {
  public addHeaderBase64(base64: string): Promise<string> {
    return new Promise<string>(resolve => {
      resolve(this.headerBase64(base64));
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
