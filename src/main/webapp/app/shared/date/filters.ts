import Vue from 'vue';
import dayjs from 'dayjs';

export const DATE_FORMAT = 'YYYY-MM-DD';
export const DATE_FORMAT_FULL_MONTH = 'MMMM DD, YYYY'
export const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm';
export const TIME_FORMAT = "HH:mm";

export const DATE_TIME_LONG_FORMAT = 'YYYY-MM-DDTHH:mm';

export function initFilters() {
  Vue.filter('formatDate', value => {
    if (value) {
      return dayjs(value).format(DATE_TIME_FORMAT);
    }
    return '';
  });
  Vue.filter('formatMillis', value => {
    if (value) {
      return dayjs(value).format(DATE_TIME_FORMAT);
    }
    return '';
  });
  Vue.filter('duration', value => {
    if (value) {
      const formatted = dayjs.duration(value).humanize();
      if (formatted) {
        return formatted;
      }
      return value;
    }
    return '';
  });
  Vue.filter('toCOPCurrency', value => {
    if (typeof value !== 'number') {
      return value;
    }
    const formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'COP',
    });
    return formatter.format(value);
  });
  Vue.filter('formatTelefonoFijo', (value: string) => {
    if (value) {
      return '(608) ' + value;
    }
    return '';
  });
  Vue.filter('sieFileToMB', (value) => {
    if (typeof value !== 'number') {
      return value;
    }
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
  })
  Vue.filter('tipoDocumento', (value) => {
    if (typeof value !== 'string') {
      return value;
    }
    let newValue = '';
    switch (value.toLowerCase()) {
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
        newValue = value;
        break;
    }
    return newValue;
  });
  Vue.filter('headerFileBase64', (value) => {
    if (typeof value !== 'string') {
      return value;
    }
    const headerBase64 = value.substring(0, 5);
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
    return headerBase64Type + value;
  });
  Vue.filter('subString', (value, tamano, clamp) => {
    clamp = clamp || '...';
    if (value && value.length > tamano) {
      value = value.slice(0, tamano) + clamp;
    }
    return value;
  });
}
