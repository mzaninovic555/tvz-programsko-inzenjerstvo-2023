import UserInfo from '~/common/auth/UserInfo';

interface AuthenticationInfo {
  authenticated: boolean;
  info?: UserInfo;
}

export default AuthenticationInfo;
