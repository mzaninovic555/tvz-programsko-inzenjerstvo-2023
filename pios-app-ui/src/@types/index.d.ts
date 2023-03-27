export interface JSXChildrenProps {
  children: JSX.Element | JSX.Element[];
}

declare module '*.module.css' {
  const classes: { [key: string]: string };
  export default classes;
}

declare module '*.svg';
