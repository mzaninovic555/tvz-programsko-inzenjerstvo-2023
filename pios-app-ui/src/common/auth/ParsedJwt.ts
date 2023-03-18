interface ParsedJwt {
  exp: number;
  iat: number;
  roles: string;
  sub: string;
}

export default ParsedJwt;
