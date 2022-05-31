import { helpers } from 'vuelidate/lib/validators';

export function validEmail(value) {
  const regexEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  return regexEmail.test(value);
}
