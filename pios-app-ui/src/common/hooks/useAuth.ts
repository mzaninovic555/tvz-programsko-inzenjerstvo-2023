interface AuthenticationInfo {
  authenticated: boolean;
  info?: any; // TODO
}

const useAuth = (): AuthenticationInfo => {
  return {authenticated: true};
};

export default useAuth;
