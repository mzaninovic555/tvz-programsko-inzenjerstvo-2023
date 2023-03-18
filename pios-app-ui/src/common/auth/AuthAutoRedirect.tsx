import React from 'react';
import {Navigate, useLocation} from 'react-router-dom';
import {JSXChildrenProps} from '~/@types';
import useAuthContext from '../../context/AuthContext';

const AuthAutoRedirect = (props: JSXChildrenProps & {loggedInToHome: boolean, customLocation?: string}) => {
  const {auth} = useAuthContext();
  const location = useLocation();
  const next = location.pathname;
  const targetLocation = (props.customLocation || '/');

  if (auth.authenticated && props.loggedInToHome) {
    const searchNext = new URLSearchParams(location.search)?.get('next');
    const finalUrl = searchNext || targetLocation;
    console.debug(`Navigating to ${finalUrl} because user is already logged in`);
    return <Navigate to={{pathname: finalUrl}} replace={true}/>;
  }

  if (!auth.authenticated && !props.loggedInToHome) {
    console.debug(`Navigating to ${targetLocation} because user is not logged in`);
    return <Navigate to={{pathname: targetLocation, search: 'next=' + next}} replace={true}/>;
  }

  return (<>{props.children}</>);
};

export default AuthAutoRedirect;
