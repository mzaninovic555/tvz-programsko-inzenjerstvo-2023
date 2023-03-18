import React from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';

const Settings = () => {
  return (<AuthAutoRedirect loggedInToHome={false} customLocation="/login">
    <p>Settings</p>
  </AuthAutoRedirect>);
};

export default Settings;
