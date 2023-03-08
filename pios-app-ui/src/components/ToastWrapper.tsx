import {Toast} from 'primereact/toast';
import React, {useRef} from 'react';
import {ToastContext} from '../context/ToastContext';

interface ToastComponentProps {
  children: JSX.Element | JSX.Element[];
}

const ToastWrapper = (props: ToastComponentProps) => {
  const toast = useRef<Toast>(null);
  return (
    <>
      <Toast className="pios-toast" ref={toast}/>
      <ToastContext.Provider value={{toast}}>
        {props.children}
      </ToastContext.Provider>
    </>
  );
};

export default ToastWrapper;
