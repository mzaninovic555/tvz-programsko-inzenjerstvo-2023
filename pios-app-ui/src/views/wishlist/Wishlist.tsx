import React from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';

const Wishlist = () => {
  return (<AuthAutoRedirect loggedInToHome={false} customLocation="/wishlist">
    <p>Wishlist</p>
  </AuthAutoRedirect>);
};

export default Wishlist;
