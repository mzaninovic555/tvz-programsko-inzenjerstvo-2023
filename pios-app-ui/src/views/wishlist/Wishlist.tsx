import React, {useEffect, useRef, useState} from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';
import WishlistEntry from '~/views/wishlist/WishlistEntry';
import {clearUserWishlist, deleteItemFromWishlist, getUserWishlist} from './WishlistService';
import {Card} from 'primereact/card';
import {DataView} from 'primereact/dataview';
import {Button} from 'primereact/button';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';
import {Messages} from 'primereact/messages';
import {showMessagesWithoutReference} from '../../common/messages/messageHelper';

const Wishlist = () => {
  const [wishlist, setWishlist] = useState<WishlistEntry[]>([]);
  const messages = useRef<Messages>(null);

  useEffect(() => {
    void fetchWishlist();
  }, []);

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);
  };

  const fetchWishlist = async () => {
    messages.current?.clear();
    const list = await getUserWishlist().catch(handleRequestFailure);
    if (!list) {
      return;
    }
    setWishlist(list);
  };

  const deleteItem = (id: number) => {
    messages.current?.clear();
    deleteItemFromWishlist(id).then((response) => {
      showMessagesWithoutReference(response.messages, messages);

      setWishlist((prev) => {
        const copy = prev.slice();
        const f = copy.find((c) => c.id == id);
        if (!f) {
          return copy;
        }
        copy.splice(copy.indexOf(f), 1);
        return copy;
      });
    }).catch(handleRequestFailure);
  };

  const clearWishlist = () => {
    messages.current?.clear();
    clearUserWishlist().then((response) => {
      showMessagesWithoutReference(response.messages, messages);
      setWishlist([]);
    }).catch(handleRequestFailure);
  };

  const template = (d: WishlistEntry) => {
    const product = d.component;
    return (<div className="col-12">
      <div className="flex flex-column xl:flex-row xl:align-items-start p-4 gap-4">
        <div className="flex flex-column sm:flex-row justify-content-between align-items-center xl:align-items-start flex-1 gap-4">
          <div className="flex flex-column align-items-center sm:align-items-start gap-3">
            <div className="text-2xl font-bold text-900">{product.name}</div>
            {/* <Rating value={product.rating} readOnly cancel={false}></Rating>*/}
            <div className="flex align-items-center gap-3">
              <span className="flex align-items-center gap-2">
                <i className="pi pi-tag"/>
                <span className="font-semibold">{product.type}</span>
              </span>
            </div>
          </div>
          <div className="flex sm:flex-column align-items-center sm:align-items-end gap-3 sm:gap-2">
            <span className="text-2xl font-semibold w-full text-center">{product.price}€</span>
            <Button icon="pi pi-trash" className="p-button-rounded p-button-danger" onClick={() => deleteItem(d.id)}/>
          </div>
        </div>
      </div>
    </div>);
  };

  const wishlistFilled = (
    <>
      <Messages ref={messages}/>
      <DataView value={wishlist} itemTemplate={template}/>
      <Button className="mt-1 p-button-danger" label="Clear wishlist" icon="pi pi-trash" onClick={clearWishlist}/>
    </>
  );

  return (<AuthAutoRedirect loggedInToHome={false} customLocation="/wishlist">
    <Card title="Wishlist">
      {wishlist.length < 1 && <p className="text-center">
        <i className="pi pi-info-circle"/> Your wishlist is empty
      </p>}
      {wishlist.length > 0 && wishlistFilled}
    </Card>
  </AuthAutoRedirect>);
};

export default Wishlist;
