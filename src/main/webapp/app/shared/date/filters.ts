import Vue from 'vue';
import dayjs from 'dayjs';

export const DATE_FORMAT = 'YYYY-MM-DD';
export const DATE_FORMAT_FULL_MONTH = 'MMMM DD, YYYY'
export const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm';

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
  Vue.filter('headerFileBase64', (value: string) => {
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
      default:
        headerBase64Type = '';
        break;
    }
    return headerBase64Type + value;
  });
}
