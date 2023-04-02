import React, {useEffect, useReducer, useRef, useState} from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';
import {Card} from 'primereact/card';
import {Button} from 'primereact/button';
import InputReducer, {emptyState, reducer} from '../../common/hooks/Reducer';
import {login} from './LoginService';
import {Messages} from 'primereact/messages';
import {
  apiToMessages,
  apiToToast,
  showMessagesWithoutReference
} from '../../common/messages/messageHelper';
import useAuthContext from '../../context/AuthContext';
import {useNavigate} from 'react-router-dom';
import {loginSuccessMessage} from '../../common/messages/LocalMessages';
import useToastContext from '../../context/ToastContext';
import FormInputText from '../../components/FormInputText';
import ActivationResult from '../activate/ActivationResult';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';

const Login = () => {
  const usernameValidator = (s?: string) => '';
  const passwordValidator = (s?: string) => '';

  const search = window.location.search.substring(1);

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
    if (search) {
      switch (search) {
        case ActivationResult.ACTIVATION_REQUIRED:
          messages.current?.show({
            detail: 'Check your email for the activation link',
            severity: 'info',
            sticky: true
          });
          break;
        case ActivationResult.ALREADY_ACTIVATED:
          messages.current?.show({
            detail: 'User is already activated',
            severity: 'warn',
            sticky: true
          });
          break;
        case ActivationResult.ACTIVATION_SUCCESS:
          messages.current?.show({
            detail: 'Account is successfully activated',
            severity: 'success',
            sticky: true
          });
          break;
        case ActivationResult.ACTIVATION_ERROR:
          messages.current?.show({
            detail: 'An error happened during activation',
            severity: 'error',
            sticky: true
          });
          break;
        case ActivationResult.DEACTIVATED_USER:
          messages.current?.show({
            detail: 'User has been deactivated',
            severity: 'info',
            sticky: true
          });
          break;
      }
    }

    if (!submitted) {
      return;
    }
    setSubmitted(false);

    if (usernameInput.error || passwordInput.error) {
      return;
    }

    void doLogin();
  }, [submitted]);

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);

    const ref = msgs.filter(((x) => x.reference));
    ref.forEach((msg) => {
      switch (msg.reference) {
        case 'username':
          dispatchUsername({type: 'changeError', error: msg.content});
          break;
        case 'password':
          dispatchPassword({type: 'changeError', error: msg.content});
          break;
        default:
          console.warn('Unknown reference', msg);
      }
    });
  };

  const doLogin = async () => {
    messages.current?.clear();
    setRequesting(true);
    const response = await login(usernameInput.value, passwordInput.value).catch(handleRequestFailure);
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

  const navigateToGithub = () => {
    window.location.href = (process.env.BACKEND_URL || '') + '/oauth2/authorization/github';
  };

  return (<AuthAutoRedirect loggedInToHome={true}>
    <Card title="Login" style={{maxWidth: '500px'}} className="m-auto card-content-no-bottom-margin">
      <Messages ref={messages}/>
      <form onSubmit={onFormSubmit} className="flex flex-column m-auto">
        <FormInputText className="mb-1" value={usernameInput.value} name="Username" required inputClassName="w-full"
          error={usernameInput.error} onChange={(e) => dispatchUsername({type: 'change', value: e})}/>
        <FormInputText value={passwordInput.value} required type="password" name="Password" inputClassName="w-full"
          error={passwordInput.error} onChange={(e) => dispatchPassword({type: 'change', value: e})}/>
        <Button className="mb-1" type="submit" label="Login" loading={requesting}/>
        <hr className="w-full"/>
        <Button icon="pi pi-github" label="Login with Github" type="button" onClick={navigateToGithub}
          style={{background: '#24292d', borderColor: '#24292d'}}/>
        <Button className="pb-0" label="Don't have an account? Register now" link
          onClick={() => navigate('/register')}/>
      </form>
    </Card>
  </AuthAutoRedirect>);
};

export default Login;

