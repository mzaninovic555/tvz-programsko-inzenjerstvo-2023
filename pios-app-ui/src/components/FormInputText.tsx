import {InputText} from 'primereact/inputtext';
import React from 'react';

interface FormInputTextProps {
  className?: string | string[];
  inputClassName?: string | string[];
  name: string;
  value: string;
  error?: string;
  required?: boolean;
  onChange?: (val: string) => void;
  maxLength?: number;
  type?: string;
  disabled?: boolean;
}

const FormInputText = (props: FormInputTextProps) => {
  const className = ['field'].concat(props.className || []);
  const inputClassName = [''].concat(props.inputClassName || []);

  return (<div className={className.join(' ')}>
    <label htmlFor={props.name} className="block font-bold">{props.name} {props.required ? '*' : ''} </label>
    <InputText id={props.name} aria-describedby={props.name + '-error'} maxLength={props.maxLength}
      className={inputClassName.join(' ') + ' ' + (props.error ? 'p-invalid block' : '')} type={props.type}
      value={props.value} onChange={(e) => props.onChange?.(e.target.value)} disabled={props.disabled}/>
    <small id={props.name + '-error'} className="p-error block">{props.error}</small>
  </div>);
};

export default FormInputText;
