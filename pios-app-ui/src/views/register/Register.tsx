import React from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';

const Register = () => {
  return (<AuthAutoRedirect loggedInToHome={true}>
    <p>registration</p>
  </AuthAutoRedirect>);
};

export default Register;

