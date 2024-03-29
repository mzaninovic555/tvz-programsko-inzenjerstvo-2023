import React, {useEffect, useRef, useState} from 'react';
import {getComponents, getManufacturers} from '../../views/component-search/ComponentService';
import {AxiosError} from 'axios';
import BasicResponse from '../../common/messages/BasicResponse';
import {apiToToast, showMessagesWithoutReference} from '../../common/messages/messageHelper';
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
import {useSearchParams} from 'react-router-dom';
import ManufacturerResponse from '~/views/component-search/ManufacturerResponse';
import useToastContext from '../../context/ToastContext';
import {clearedFilters} from '../../common/messages/LocalMessages';
import ComponentResponse from '~/views/component-search/ComponentResponse';

interface ComponentSearchProps {
  type?: Type;
  modalMode?: boolean;
  onComponentSelected?: (component: ComponentResponse) => void;
}

const ComponentSearch = (props: ComponentSearchProps) => {
  const [components, setComponents] = useState<ComponentResponse[]>([]);
  const [wishlist, setWishlist] = useState<number[]>([]);
  const [manufacturers, setManufacturers] = useState<ManufacturerResponse[]>([]);
  const [params] = useSearchParams();

  const [componentSearch, debouncedComponentSearch, setComponentSearch] =
    useDebounce('', 500) as [string, string, React.Dispatch<React.SetStateAction<string | undefined>>];
  const [componentType, setComponentType] = useState(() => {
    const wanted = params?.get('type') || props.type;
    return wanted && wanted in Type ? wanted : undefined;
  });

  const [manufacturerSearch, setManufacturerSearch] = useState<string>();
  const [priceRange, setPriceRange] = useState<[number, number]>([1, 5000]);
  const [priceRangeDebounced, setPriceRangeDebounced] = useState<[number, number]>(priceRange);
  const [rangeTimeout, setRangeTimeout] = useState<number>();

  const {toast} = useToastContext();
  const messages = useRef<Messages>(null);
  const auth = useAuthContext();

  const [sortKey, setSortKey] = useState<string>();
  const [sortOrder, setSortOrder] = useState<0 | 1 | -1 | null | undefined>(0);
  const [sortField, setSortField] = useState<string>();
  const sortOptions = [
    {label: 'Price High to Low', value: '!price'},
    {label: 'Price Low to High', value: 'price'},
    {label: 'Rating High to Low', value: '!rating'},
    {label: 'Rating Low to High', value: 'rating'},
    {label: 'Name A-Z', value: 'name'},
    {label: 'Name Z-A', value: '!name'},
  ];

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

  useEffect(() => void fetchManufacturers(), []);

  useEffect(() => {
    void fetchComponents();
    if (auth.auth.authenticated) {
      void fetchWishlist();
    }
  }, [debouncedComponentSearch, componentType, priceRangeDebounced, manufacturerSearch]);

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);
  };

  const fetchComponents = async () => {
    messages.current?.clear();
    const componentList = await getComponents(
      debouncedComponentSearch,
      priceRangeDebounced,
      componentType,
      manufacturerSearch).catch(handleRequestFailure);
    if (!componentList) {
      return;
    }
    setComponents(componentList);
  };

  const fetchManufacturers = async () => {
    const manufacturerList = await getManufacturers().catch(handleRequestFailure);
    if (!manufacturerList) {
      return;
    }
    setManufacturers(manufacturerList);
  };

  const fetchWishlist = async () => {
    messages.current?.clear();
    const list = await getUserWishlist().catch(handleRequestFailure);
    if (!list) {
      return;
    }
    setWishlist(list.map((w) => w.component.id));
  };

  useEffect(() => {
    if (!sortKey) {
      setSortOrder(0);
      setSortField(undefined);
      return;
    }

    setSortOrder(sortKey.indexOf('!') === 0 ? -1 : 1);
    setSortField(sortKey.replace('!', ''));
  }, [sortKey]);

  const clearFilters = () => {
    setComponentSearch('');
    if (!props.modalMode) {
      setComponentType(undefined);
    }
    setPriceRange([1, 5000] as [number, number]);
    setManufacturerSearch(undefined);
    setSortKey(undefined);
    setSortOrder(0);
    setSortField(undefined);
    toast.current?.show(apiToToast(clearedFilters));
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
        <span className="p-input-icon-right mr-2 mb-1">
          <i className="pi pi-search"/>
          <InputText type="text" value={componentSearch} onChange={(e) => setComponentSearch(e.target.value)}
            placeholder="Search"/>
        </span>
        {!props.modalMode &&
        <Dropdown className="mr-2 w-9rem mb-1" value={componentType} options={Object.values(Type)} showClear
          placeholder="Type" onChange={(e) => setComponentType(e.target.value as string)}/>}
        <Dropdown className="mr-2 w-12rem mb-1" value={manufacturerSearch} options={manufacturers.map((m) => m.manufacturer.name)}
          placeholder="Manufacturer" showClear
          onChange={(e) => setManufacturerSearch(e.target.value as string)}/>
        <Dropdown className="mr-2 w-14rem mb-1" value={sortKey} options={sortOptions} placeholder="Sort" showClear
          onChange={(e) => setSortKey(e.value)}/>
        <div className="flex align-items-center flex-row w-full w-12rem mr-2 mb-1">
          <Tag severity="info" value={priceRange[0]} />
          <Slider min={1} max={5000} range value={priceRange} className="w-full mx-3"
            onChange={(e) => setPriceRange(e.value as [number, number])}/>
          <Tag severity="info" value={priceRange[1]} />
        </div>
        <Button icon="pi pi-delete-left" label="Clear filters" onClick={clearFilters} className="mb-1"
          disabled={!debouncedComponentSearch && !componentType && !priceRangeDebounced && !manufacturerSearch}/>
      </div>
    </>;
  };

  const template = (product: ComponentResponse) => {
    const disabledText = !auth.auth.authenticated ? 'You need to log in to wishlist items' :
      wishlist.includes(product.id) ? 'Item is already wishlisted' : undefined;
    const wishlistButton = (
      <>
        <Button icon="pi pi-plus" label="Wishlist" disabled={!!disabledText} tooltip={disabledText}
          tooltipOptions={{position: 'top', showOnDisabled: true}} onClick={() => addToWishlist(product.id)}/>
      </>
    );
    return <ComponentTemplate component={product} button={wishlistButton}/>;
  };

  const modalTemplate = (product: ComponentResponse) => {
    const button = (
      <Button icon="pi pi-plus" label="Select" onClick={() => props.onComponentSelected?.(product)}/>
    );
    return <ComponentTemplate component={product} button={button} hideReviews/>;
  };

  const body = (<>
    <Messages ref={messages} />
    <div className={!props.modalMode ? classes['wishlist-wrapper'] : ''}>
      {header()}
      {components.length < 1 && <p className="mt-6 text-center">
        <i className="pi pi-info-circle"/> No components found
      </p>}
      {components.length > 0 &&
      <DataView value={components} itemTemplate={props.modalMode ? modalTemplate : template}
        paginator rows={10} sortField={sortField} sortOrder={sortOrder} />}
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
