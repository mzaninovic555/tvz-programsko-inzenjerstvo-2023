import Component from '~/views/component-search/Component';
import {Rating} from 'primereact/rating';
import React from 'react';
import {Image} from 'primereact/image';
import {Chip} from 'primereact/chip';

interface ComponentTemplateProps {
  component: Component;
  button: JSX.Element;
  additionalData?: JSX.Element | JSX.Element[];
}

const ComponentTemplate = (props: ComponentTemplateProps) => {
  const product = props.component;
  // TODO rating
  return (
    <div className="col-12 md:col-12 lg:col-12 xl:col-12 sm:col-12">
      <div className="flex flex-column sm:flex-column md:flex-row xl:flex-row xl:align-items-start p-3 gap-4">
        <Image className="w-9 sm:w-16rem xl:w-10rem shadow-2 block xl:block mx-auto border-round pios-image"
          src={product.imageBase64 ? `data:image/jpeg;base64,${product.imageBase64}` : `/unknown.jpg`}
          alt={product.name} preview/>
        <div className="flex flex-column sm:flex-row justify-content-between align-items-center xl:align-items-start flex-1 gap-4">
          <div className="flex flex-column align-items-center sm:align-items-start gap-3">
            <div className="text-2xl font-bold text-900">{product.name}</div>
            <Rating value={4.5} readOnly cancel={false}></Rating>
            <div className="flex">
              <Chip label={product.manufacturer.name} className="mr-2"/>
              <div className="flex align-items-center gap-3">
                <span className="flex align-items-center gap-2">
                  <i className="pi pi-tag"/>
                  <span className="font-semibold">{product.type}</span>
                </span>
              </div>
            </div>
            {props.additionalData}
          </div>
          <div className="flex flex-column sm:flex-column align-items-center sm:align-items-end gap-3 sm:gap-2">
            <span className="text-2xl font-semibold w-full text-center">{product.price}€</span>
            {props.button}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ComponentTemplate;

