import React from 'react';
import {ProgressSpinner} from 'primereact/progressspinner';

interface SpinnerProps {
  text?: string;
  error?: string;
  height?: string;
}

const Spinner = (props: SpinnerProps) => {
  return (
    <div className="pios-loading-spinner" style={{height: props.height || '60vh'}}>
      <div className="text-center">
        {!props.error && <h2 className="p-component">{props.text}</h2>}
        {props.error && <h2 className="p-component p-error">{props.error}</h2>}
        {!props.error && <ProgressSpinner/>}
      </div>
    </div>
  );
};

export default Spinner;
