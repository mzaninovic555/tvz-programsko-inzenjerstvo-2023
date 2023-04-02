import React, {useEffect, useRef, useState} from 'react';
import {getComponents} from '../../views/component-search/ComponentService';
import {AxiosError} from 'axios';
import BasicResponse from '../../common/messages/BasicResponse';
import {showMessagesWithoutReference} from '../../common/messages/messageHelper';
import {Messages} from 'primereact/messages';
import {DataView} from 'primereact/dataview';
import {Card} from 'primereact/card';
import classes from '../../views/wishlist/Wishlist.module.css';
import {Button} from 'primereact/button';
import useAuthContext from '../../context/AuthContext';
import {addComponentToWishlist, getUserWishlist} from '../../views/wishlist/WishlistService';
import {InputText} from 'primereact/inputtext';
import {useDebounce} from 'primereact/hooks';
import {Dropdown} from 'primereact/dropdown';
import Type from '../../views/component-search/Type';
import {Slider} from 'primereact/slider';
import {Tag} from 'primereact/tag';
import ComponentTemplate from './ComponentTemplate';
import ComponentSearchResponse from '~/views/component-search/ComponentSearchResponse';
import {useSearchParams} from 'react-router-dom';
import Component from '~/views/component-search/Component';

interface ComponentSearchProps {
  type?: Type;
  modalMode?: boolean;
  onComponentSelected?: (component: Component) => void;
}

const ComponentSearch = (props: ComponentSearchProps) => {
  const [components, setComponents] = useState<ComponentSearchResponse[]>([]);
  const [wishlist, setWishlist] = useState<number[]>([]);
  const [params] = useSearchParams();

  const [componentSearch, debouncedComponentSearch, setComponentSearch] =
    useDebounce('', 500) as [string, string, React.Dispatch<React.SetStateAction<string>>];
  const [componentType, setComponentType] = useState(() => {
    const wanted = params?.get('type') || props.type;
    return wanted && wanted in Type ? wanted : '';
  });
  const [priceRange, setPriceRange] = useState<[number, number]>([1, 5000]);
  const [priceRangeDebounced, setPriceRangeDebounced] = useState<[number, number]>(priceRange);
  const [rangeTimeout, setRangeTimeout] = useState<number>();

  const messages = useRef<Messages>(null);
  const auth = useAuthContext();

  useEffect(() => {
    if (rangeTimeout) {
      clearTimeout(rangeTimeout);
    }

    const newVal = setTimeout(() => {
      setPriceRangeDebounced(priceRange);
      setRangeTimeout(undefined);
    }, 400);

    // @ts-ignore
    setRangeTimeout(newVal);
  }, [priceRange]);

  useEffect(() => {
    void fetchComponents();
    if (auth.auth.authenticated) {
      void fetchWishlist();
    }
  }, [debouncedComponentSearch, componentType, priceRangeDebounced]);

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);
  };

  const fetchComponents = async () => {
    messages.current?.clear();
    const componentList = await getComponents(debouncedComponentSearch, componentType, priceRangeDebounced)
      .catch(handleRequestFailure);
    if (!componentList) {
      return;
    }
    setComponents(componentList);
  };

  const fetchWishlist = async () => {
    messages.current?.clear();
    const list = await getUserWishlist().catch(handleRequestFailure);
    if (!list) {
      return;
    }
    setWishlist(list.map((w) => w.component.id));
  };

  const addToWishlist = (componentId: number) => {
    messages.current?.clear();
    addComponentToWishlist(componentId).then((response) => {
      setWishlist((prev) => [...prev, componentId]);
      showMessagesWithoutReference(response.messages, messages);
    }).catch(handleRequestFailure);
  };

  const header = () => {
    return <>
      <div className="flex align-items-center flex-wrap mb-2">
        <span className="p-input-icon-right mr-2">
          <i className="pi pi-search"/>
          <InputText type="text" value={componentSearch} onChange={(e) => setComponentSearch(e.target.value)}
            placeholder="Search"/>
        </span>
        {!props.modalMode &&
        <Dropdown className="mr-2" value={componentType} options={Object.values(Type)}
          placeholder="Choose a type"
          onChange={(e) => setComponentType(e.target.value as string)}/>}
        <div className="flex align-items-center flex-row w-full w-15rem">
          <Tag severity="info" value={priceRange[0]} />
          <Slider min={1} max={5000} range value={priceRange} className="w-full mx-3"
            onChange={(e) => setPriceRange(e.value as [number, number])}/>
          <Tag severity="info" value={priceRange[1]} />
        </div>
      </div>
    </>;
  };

  const template = (product: ComponentSearchResponse) => {
    const disabledText = !auth.auth.authenticated ? 'You need to log in to wishlist items' :
      wishlist.includes(product.component.id) ? 'Item is already wishlisted' : undefined;
    const wishlistButton = (
      <>
        <Button icon="pi pi-plus" label="Wishlist" disabled={!!disabledText} tooltip={disabledText}
          tooltipOptions={{position: 'top', showOnDisabled: true}} onClick={() => addToWishlist(product.component.id)}/>
      </>
    );
    return <ComponentTemplate component={product.component} button={wishlistButton}/>;
  };

  const modalTemplate = (product: ComponentSearchResponse) => {
    const button = (
      <Button icon="pi pi-plus" label="Select" onClick={() => props.onComponentSelected?.(product.component)}/>
    );
    return <ComponentTemplate component={product.component} button={button} hideReviews/>;
  };

  const body = (<>
    <Messages ref={messages} />
    <div className={!props.modalMode ? classes['wishlist-wrapper'] : ''}>
      {header()}
      {components.length < 1 && <p className="mt-6 text-center">
        <i className="pi pi-info-circle"/> No components found
      </p>}
      {components.length > 0 && <DataView value={components} itemTemplate={props.modalMode ? modalTemplate : template}
        paginator rows={10}/>}
    </div>
  </>);

  if (props.modalMode) {
    return body;
  }

  return (
    <Card title="Search components">
      {body}
    </Card>);
};

export default ComponentSearch;
