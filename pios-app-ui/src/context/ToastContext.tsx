import React, {useContext} from 'react';
import {Toast} from 'primereact/toast';

export interface ToastCtx {
  toast: React.RefObject<Toast>;
}

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
export const ToastContext = React.createContext<ToastCtx>({toast: null});

const useToastContext = (): ToastCtx => {
  const val = useContext(ToastContext);
  if (!val) {
    throw new Error('ToastContext Provider is required!');
  }
  return val;
};

export default useToastContext;
