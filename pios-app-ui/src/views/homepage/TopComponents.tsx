import React from 'react';
import {Carousel, CarouselResponsiveOption} from 'primereact/carousel';
import Spinner from '../../components/Spinner';
import ComponentResponse from '~/views/component-search/ComponentResponse';
import {Card} from 'primereact/card';
import {Rating} from 'primereact/rating';
import {Chip} from 'primereact/chip';
import './Homepage.css';

interface TopComponentsResponse {
  components?: ComponentResponse[];
  error?: string;
}

const TopComponents = (props: TopComponentsResponse) => {
  const carouselResponsiveOptions: CarouselResponsiveOption[] = [
    {
      numVisible: 3,
      breakpoint: '300000px',
      numScroll: 1
    },
    {
      numVisible: 3,
      breakpoint: '1600px',
      numScroll: 1
    },
    {
      numVisible: 2,
      breakpoint: '992px',
      numScroll: 1
    },
    {
      numVisible: 1,
      breakpoint: '576px',
      numScroll: 1
    }
  ];

  if (!props.components) {
    return (<Spinner height="unset" text="Loading top components..." error={props.error}/>);
  }

  const header = (<h1 className="text-center text-0 strikethrough">Top rated components</h1>);

  const template = (item: ComponentResponse) => {
    const max = 5;

    return (
      <Card style={{marginLeft: '0.2rem', marginRight: '0.2rem', marginBottom: '10px'}}>
        <h2 style={{marginTop: 0, height: '2rem', marginBottom: 50}}>{item.name}</h2>
        <img style={{height: '300px', width: '100%', objectFit: 'contain'}} alt={item.name}
          className="pios-image shadow-2 block xl:block mx-auto border-round"
          src={item.imageBase64 ? `data:image/jpeg;base64,${item.imageBase64}` : `/unknown.jpg`}/>
        <div className="flex align-items-start my-3">
          <Rating className="mr-2" cancel={false} readOnly value={Math.round(item.rating)}/>
          <span>{`${item.rating}/${max}`} ({item.reviewCount} review{item.reviewCount == 1 ? '' : 's'})</span>
        </div>
        <div className="flex">
          <Chip label={item.manufacturer.name} className="mr-2"/>
          <div className="flex align-items-center gap-3">
            <span className="flex align-items-center gap-2">
              <i className="pi pi-tag"/>
              <span className="font-semibold">{item.type}</span>
            </span>
          </div>
        </div>
      </Card>
    );
  };

  return (
    <Carousel circular responsiveOptions={carouselResponsiveOptions} header={header} value={props.components}
      itemTemplate={template}/>
  );
};

export default TopComponents;
