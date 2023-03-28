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
import classes from './Wishlist.module.css';
import ComponentTemplate from '../component-search/ComponentTemplate';
import {formatDate} from '../../common/dateHelper';

const Wishlist = () => {
  const [wishlist, setWishlist] = useState<WishlistEntry[]>([]);
  const messages = useRef<Messages>(null);

  useEffect(() => void fetchWishlist(), []);

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
    const addedAt = (<span>Added at {formatDate(d.addedAt)}</span>);
    const wishlistButton = (
      <Button icon="pi pi-trash" className="p-button-danger" onClick={() => deleteItem(d.id)}/>
    );
    return <ComponentTemplate component={d.component} button={wishlistButton} additionalData={addedAt}/>;
  };

  const wishlistFilled = (
    <>
      <div className={classes['wishlist-wrapper']}>
        <DataView value={wishlist} itemTemplate={template}/>
      </div>
      <Button className="mt-3 p-button-danger" label="Clear wishlist" icon="pi pi-trash" onClick={clearWishlist}/>
    </>
  );

  return (<AuthAutoRedirect loggedInToHome={false} customLocation="/wishlist">
    <Card title="Wishlist">
      <Messages ref={messages}/>
      {wishlist.length < 1 && <p className="text-center">
        <i className="pi pi-info-circle"/> Your wishlist is empty
      </p>}
      {wishlist.length > 0 && wishlistFilled}
    </Card>
  </AuthAutoRedirect>);
};

export default Wishlist;
