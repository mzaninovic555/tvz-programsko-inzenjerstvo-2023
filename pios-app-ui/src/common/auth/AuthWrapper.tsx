import React, {useEffect, useState} from 'react';
import {JSXChildrenProps} from '~/@types';
import {AuthContext} from '../../context/AuthContext';
import ParsedJwt from '~/common/auth/ParsedJwt';
import AuthenticationInfo from '~/common/auth/AuthenticationInfo';

const AuthWrapper = (props: JSXChildrenProps) => {
  const [token, setToken] = useState<string | undefined | null>(localStorage.getItem('token'));

  const computeAuth = (): AuthenticationInfo => {
    if (!token) {
      return {authenticated: false};
    }

    const extracted = extractTokenData(token);
    if (!extracted) {
      return {authenticated: false};
    }
    return prepareReturnValueFromParsed(extracted);
  };

  const [cachedAuth, setCachedAuth] = useState<AuthenticationInfo>(computeAuth());

  // TODO token expiration handling?

  const setTokenIntercept = (token?: string) => {
    if (!token) {
      localStorage.removeItem('token');
    } else {
      localStorage.setItem('token', token);
    }
    setToken(token);
  };

  useEffect(() => setCachedAuth(computeAuth()), [token]);

  return (
    <AuthContext.Provider value={{setToken: setTokenIntercept, auth: cachedAuth}}>
      {props.children}
    </AuthContext.Provider>
  );
};

const extractTokenData = (token?: string): ParsedJwt | null => {
  let parsed: ParsedJwt | null = null;
  try {
    parsed = JSON.parse(atob(token?.split('.')[1] || '')) as ParsedJwt;
  } catch (e) {
    console.warn('Invalid JWT JSON');
  }
  return parsed;
};

const prepareReturnValueFromParsed = (parsed: ParsedJwt): AuthenticationInfo => {
  const exp = new Date(parsed.exp * 1000);
  if (isNaN(exp.getTime()) || !parsed.sub || !parsed.roles) {
    return {authenticated: false};
  }
  return {
    authenticated: true, info: {
      expire: exp,
      role: parsed.roles,
      username: parsed.sub
    }
  };
};

export default AuthWrapper;
