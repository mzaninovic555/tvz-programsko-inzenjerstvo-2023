import {Reducer} from 'react';

export interface InputFieldState<T> {
  value: T,
  error?: string,
  validator: (s?: T) => string | undefined
}

export type Action<T> =
  | { type: 'change', value: T, error?: string }
  | { type: 'reset' }
  | { type: 'validate' }
  | { type: 'changeError', error?: string };

export function reducer<T>(state: InputFieldState<T>, action: Action<T>, defaultValue: T = '' as any): InputFieldState<T> {
  if (action.type == 'change') {
    if (action.error != undefined) {
      return {value: action.value, error: action.error, validator: state.validator};
    }
    return {value: action.value, error: state.validator(action.value), validator: state.validator};
  }
  if (action.type == 'reset') {
    return {value: defaultValue, error: undefined, validator: state.validator};
  }
  if (action.type == 'validate') {
    return {value: state.value, error: state.validator(state.value), validator: state.validator};
  }
  if (action.type == 'changeError') {
    return {value: state.value, error: action.error, validator: state.validator};
  }
  return {value: state.value, error: state.error, validator: state.validator};
}

export const emptyState: InputFieldState<any> = {value: '', validator: (s) => undefined, error: undefined};

export type InputReducer<T> = Reducer<InputFieldState<T>, Action<T>>;

export default InputReducer;
