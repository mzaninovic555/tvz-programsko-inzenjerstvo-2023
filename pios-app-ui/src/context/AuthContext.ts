import React, {useContext} from 'react';
import AuthenticationInfo from '~/common/auth/AuthenticationInfo';

export interface AuthCtx {
  setToken: (token?: string) => void;
  auth: AuthenticationInfo;
}


// eslint-disable-next-line @typescript-eslint/no-empty-function
export const AuthContext = React.createContext<AuthCtx>({auth: {authenticated: false}, setToken: () => {}});

const useAuthContext = (): AuthCtx => {
  const val = useContext(AuthContext);
  if (!val) {
    throw new Error('AuthContext Provider is required!');
  }
  return val;
};

export default useAuthContext;
