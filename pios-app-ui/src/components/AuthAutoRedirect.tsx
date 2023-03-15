import React from 'react';
import useAuth from '../common/hooks/useAuth';
import {Navigate, useLocation} from 'react-router-dom';
import {JSXChildrenProps} from '~/@types';

const AuthAutoRedirect = (props: JSXChildrenProps & {loggedInToHome: boolean, customLocation?: string}) => {
  const auth = useAuth();
  const location = useLocation();
  const next = location.pathname;
  const targetLocation = (props.customLocation || '/');

  if (auth.authenticated && props.loggedInToHome) {
    console.debug(`Navigating to ${targetLocation} because user is already logged in`);
    return <Navigate to={{pathname: targetLocation}} replace={true}/>;
  }

  if (!auth.authenticated && !props.loggedInToHome) {
    console.debug(`Navigating to ${targetLocation} because user is not logged in`);
    return <Navigate to={{pathname: targetLocation, search: 'next=' + next}} replace={true}/>;
  }

  return (<>{props.children}</>);
};

export default AuthAutoRedirect;
