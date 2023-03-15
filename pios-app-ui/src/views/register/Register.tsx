import React from 'react';
import AuthAutoRedirect from '../../components/AuthAutoRedirect';

const Register = () => {
  return (<AuthAutoRedirect loggedInToHome={true}>
    <p>login</p>
  </AuthAutoRedirect>);
};

export default Register;

