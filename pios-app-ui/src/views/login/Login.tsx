import React from 'react';
import AuthAutoRedirect from '../../components/AuthAutoRedirect';

const Login = () => {
  return (<AuthAutoRedirect loggedInToHome={true}>
    <p>login</p>
  </AuthAutoRedirect>);
};

export default Login;

