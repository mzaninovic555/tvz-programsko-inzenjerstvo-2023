import React, {useEffect, useReducer, useRef, useState} from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';
import {Card} from 'primereact/card';
import {Button} from 'primereact/button';
import InputReducer, {emptyState, reducer} from '../../common/hooks/Reducer';
import {login} from './LoginService';
import {Messages} from 'primereact/messages';
import {apiToMessages, apiToToast} from '../../common/messages/messageHelper';
import useAuthContext from '../../context/AuthContext';
import {useNavigate} from 'react-router-dom';
import {loginSuccessMessage} from '../../common/messages/LocalMessages';
import useToastContext from '../../context/ToastContext';
import FormInputText from '../../components/FormInputText';

const Login = () => {
  const usernameValidator = (s?: string) => !s ? 'Username is required' : s.length < 3 || s.length > 20 ?
    'Username should be between 3 and 20 characters' : '';
  const passwordValidator = (s?: string) => !s ? 'Password is required' : s.length < 8 || s.length > 100 ?
    'Username should be between 3 and 20 characters' : '';

  const [usernameInput, dispatchUsername] = useReducer<InputReducer<string>>(reducer, {...emptyState, validator: usernameValidator});
  const [passwordInput, dispatchPassword] = useReducer<InputReducer<string>>(reducer, {...emptyState, validator: passwordValidator});

  const [submitted, setSubmitted] = useState(false);
  const [requesting, setRequesting] = useState(false);

  const messages = useRef<Messages>(null);
  const {setToken} = useAuthContext();
  const navigate = useNavigate();
  const {toast} = useToastContext();

  const onFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    dispatchUsername({type: 'validate'});
    dispatchPassword({type: 'validate'});
    setSubmitted(true);
  };

  useEffect(() => {
    if (!submitted || usernameInput.error || passwordInput.error) {
      return;
    }
    setSubmitted(false);
    void doLogin();
  }, [submitted]);

  const doLogin = async () => {
    messages.current?.clear();
    setRequesting(true);
    // TODO error handling
    const response = await login(usernameInput.value, passwordInput.value).catch(console.error);
    setRequesting(false);

    if (!response) {
      return;
    }

    if (response.token) {
      setToken(response.token);
      toast.current?.show(apiToToast(loginSuccessMessage));
    }
    if (response.message) {
      messages.current?.show(apiToMessages(response.message));
    }
  };

  return (<AuthAutoRedirect loggedInToHome={true}>
    <Card title="Login" style={{maxWidth: '500px'}} className="m-auto card-content-no-bottom-margin">
      <Messages ref={messages}/>
      <form onSubmit={onFormSubmit} className="flex flex-column m-auto">
        <FormInputText className="mb-1" value={usernameInput.value} name="Username" required inputClassName="w-full"
          error={usernameInput.error} onChange={(e) => dispatchUsername({type: 'change', value: e})}/>
        <FormInputText value={passwordInput.value} required type="password" name="Password" inputClassName="w-full"
          error={passwordInput.error} onChange={(e) => dispatchPassword({type: 'change', value: e})}/>
        <Button type="submit" label="Login" loading={requesting}/>
        <Button label="Don't have an account? Register now" link onClick={() => navigate('/register')}/>
      </form>
    </Card>
  </AuthAutoRedirect>);
};

export default Login;

