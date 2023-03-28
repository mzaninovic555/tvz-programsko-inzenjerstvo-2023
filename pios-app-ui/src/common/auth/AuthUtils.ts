import ParsedJwt from '~/common/auth/ParsedJwt';
import AuthenticationInfo from '~/common/auth/AuthenticationInfo';

export const extractTokenData = (token?: string): ParsedJwt | null => {
  let parsed: ParsedJwt | null = null;
  try {
    parsed = JSON.parse(atob(token?.split('.')[1] || '')) as ParsedJwt;
  } catch (e) {
    console.warn('Invalid JWT JSON');
  }
  return parsed;
};

export const prepareReturnValueFromParsed = (parsed: ParsedJwt): AuthenticationInfo => {
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

export const getJwtFromCookie = (): string | null => {
  const tokenKeyword = 'temp-jwt-token=';
  if (!document.cookie.includes(tokenKeyword)) {
    return null;
  }

  const values = document.cookie.split(';').map((x) => x.trim());
  const jwtVal = values.find((x) => x.includes(tokenKeyword))?.split('=').map((x) => x.trim());
  if (jwtVal?.[1] && jwtVal[1] != 'N/A') {
    document.cookie = tokenKeyword + 'N/A';
    return jwtVal[1];
  }

  return null;
};

export const getJwtFromQuery = (): string | null => {
  const token = new URLSearchParams(window.location.search).get('jwt');
  if ((token?.length ?? 0) > 2) {
    return token;
  }
  return null;
};

export const initTokenToStorage = () => {
  const cookie = getJwtFromCookie();
  if (cookie) {
    console.debug('Init token from cookie');
    localStorage.setItem('token', cookie);
    return;
  }

  const query = getJwtFromQuery();
  if (query) {
    console.debug('Init token from query');
    localStorage.setItem('token', query);
    const s = new URLSearchParams(window.location.search);
    s.delete('jwt');
    window.location.search = s.toString();
  }
};
