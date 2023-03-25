import React, {useEffect, useRef, useState} from 'react';
import Component from '../../views/component-search/Component';
import {getAllComponents} from '../../views/component-search/ComponentService';
import {AxiosError} from 'axios';
import BasicResponse from '../../common/messages/BasicResponse';
import {showMessagesWithoutReference} from '../../common/messages/messageHelper';
import {Messages} from 'primereact/messages';
import {DataView} from 'primereact/dataview';
import {Card} from 'primereact/card';
import classes from '../../views/wishlist/Wishlist.module.css';
import {Rating} from 'primereact/rating';
import {Button} from 'primereact/button';
import useAuthContext from '../../context/AuthContext';
import {addComponentToWishlist, getUserWishlist} from '../../views/wishlist/WishlistService';

const ComponentSearch = () => {
  const [components, setComponents] = useState<Component[]>([]);
  const [wishlist, setWishlist] = useState<number[]>([]);
  const messages = useRef<Messages>(null);
  const auth = useAuthContext();

  useEffect(() => {
    void fetchComponents();
    void fetchWishlist();
  }, []);

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);
  };

  const fetchComponents = async () => {
    messages.current?.clear();
    const componentList = await getAllComponents().catch(handleRequestFailure);
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
    setWishlist(list.map(w => w.component.id));
  };

  const addToWishlist = (componentId: number) => {
    messages.current?.clear();
    addComponentToWishlist(componentId).then((response) => {
      showMessagesWithoutReference(response.messages, messages);
    }).catch(handleRequestFailure);
  };

  const template= (component: Component) => {
    const product = component;
    return (
      <div className="col-12">
        <div className="flex flex-column xl:flex-row xl:align-items-start p-4 gap-4">
          <img className="w-9 sm:w-16rem xl:w-10rem shadow-2 block xl:block mx-auto border-round"
            src={product.imageBase64 ? `data:image/jpeg;base64,${product.imageBase64}` : `/unknown.jpg`} alt={product.name} />
          <div className="flex flex-column sm:flex-row justify-content-between align-items-center xl:align-items-start flex-1 gap-4">
            <div className="flex flex-column align-items-center sm:align-items-start gap-3">
              <div className="text-2xl font-bold text-900">{product.name}</div>
              <Rating value={4.5} readOnly cancel={false}></Rating>
              <div className="flex align-items-center gap-3">
                <span className="flex align-items-center gap-2">
                  <i className="pi pi-tag"></i>
                  <span className="font-semibold">{product.type}</span>
                </span>
              </div>
            </div>
            <div className="flex sm:flex-column align-items-center sm:align-items-end gap-3 sm:gap-2">
              <span className="text-2xl font-semibold">${product.price}</span>
              <Button icon="pi pi-file-export" className="p-button-rounded"
                disabled={!auth.auth.authenticated || wishlist.includes(component.id)}
                onClick={() => addToWishlist(product.id)}></Button>
            </div>
          </div>
        </div>
      </div>
    );
  };

  const componentList = (
    <>
      <Messages ref={messages} />
      <div className={classes['wishlist-wrapper']}>
        <DataView value={components} itemTemplate={template} paginator rows={10}/>
      </div>
    </>
  );

  return (
    <Card title="Search components">
      {components.length < 1 && <p className="text-center">
        <i className="pi pi-info-circle"/> No components found
      </p>}
      {components.length > 0 && componentList}
    </Card>);
};

export default ComponentSearch;
