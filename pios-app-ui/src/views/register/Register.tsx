import React, {useEffect, useReducer, useRef, useState} from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';
import InputReducer, {emptyState, reducer} from '../../common/hooks/Reducer';
import {Messages} from 'primereact/messages';
import {showMessagesWithoutReference} from '../../common/messages/messageHelper';
import {Card} from 'primereact/card';
import {Button} from 'primereact/button';
import {register} from '../../views/register/RegisterService';
import {useNavigate} from 'react-router-dom';
import {AxiosError} from 'axios/index';
import BasicResponse from '~/common/messages/BasicResponse';
import FormInputText from '../../components/FormInputText';
import FormTextarea from '../../components/FormTextarea';

const Register = () => {
  const usernameValidator = (s?: string) => !s ? 'Username is required' : s.length < 3 || s.length > 20 ?
    'Username should be between 3 and 20 characters' : '';
  const passwordValidator = (s?: string) => !s ? 'Password is required' : s.length < 8 || s.length > 20 ?
    'Password should be between 8 and 20 characters' : '';
  const emailValidator = (s?: string) => !s ? 'Email is required' : '';
  const descriptionValidator = (s?: string) => !s ? '' : s.length > 250 ? 'Description should be maximum 250 characters' : '';

  const [usernameInput, dispatchUsername] = useReducer<InputReducer<string>>(reducer, {...emptyState, validator: usernameValidator});
  const [passwordInput, dispatchPassword] = useReducer<InputReducer<string>>(reducer, {...emptyState, validator: passwordValidator});
  const [emailInput, dispatchEmail] = useReducer<InputReducer<string>>(reducer, {...emptyState, validator: emailValidator});
  const [descriptionInput, dispatchDescription] = useReducer<InputReducer<string>>(reducer, {...emptyState, validator: descriptionValidator});

  const navigate = useNavigate();
  const [submitted, setSubmitted] = useState(false);
  const [requesting, setRequesting] = useState(false);

  const messages = useRef<Messages>(null);

  const onFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    dispatchUsername({type: 'validate'});
    dispatchPassword({type: 'validate'});
    dispatchEmail({type: 'validate'});
    dispatchDescription({type: 'validate'});
    setSubmitted(true);
  };

  useEffect(() => {
    if (!submitted) {
      return;
    }
    setSubmitted(false);

    if (usernameInput.error || passwordInput.error || emailInput.error || descriptionInput.error) {
      return;
    }

    void doRegister();
  }, [submitted]);

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);

    const ref = msgs.filter(((x) => x.reference));
    ref.forEach((msg) => {
      switch (msg.reference) {
        case 'email':
          dispatchEmail({type: 'changeError', error: msg.content});
          break;
        case 'username':
          dispatchUsername({type: 'changeError', error: msg.content});
          break;
        case 'password':
          dispatchPassword({type: 'changeError', error: msg.content});
          break;
        case 'description':
          dispatchDescription({type: 'changeError', error: msg.content});
          break;
        default:
          console.warn('Unknown reference', msg);
      }
    });
  };

  const doRegister = async () => {
    messages.current?.clear();
    setRequesting(true);
    const response = await register(emailInput.value, usernameInput.value, passwordInput.value, descriptionInput.value)
      .catch(handleRequestFailure);
    setRequesting(false);

    if (!response) {
      return;
    }

    navigate('/login?' + (response.message?.content || ''));
  };

  return (<AuthAutoRedirect loggedInToHome={true}>
    <Card title="Register" style={{maxWidth: '500px'}} className="m-auto card-content-no-bottom-margin">
      <Messages ref={messages}/>
      <form onSubmit={onFormSubmit} className="flex flex-column m-auto">
        <FormInputText name="Email" value={emailInput.value} type="email" error={emailInput.error}
          onChange={(e) => dispatchEmail({type: 'change', value: e})} required inputClassName="w-full"/>
        <FormInputText name="Username" value={usernameInput.value} error={usernameInput.error}
          onChange={(e) => dispatchUsername({type: 'change', value: e})} required inputClassName="w-full"/>
        <FormInputText name="Password" type="password" value={passwordInput.value} error={passwordInput.error}
          onChange={(e) => dispatchPassword({type: 'change', value: e})} required inputClassName="w-full"/>
        <FormTextarea name="Description" value={descriptionInput.value} error={descriptionInput.error}
          onChange={(e) => dispatchDescription({type: 'change', value: e})}
          inputClassName="w-full" rows={5}/>
        <Button className="mt-2" type="submit" label="Register" loading={requesting}/>
        <Button label="Already have an account? Login now" link onClick={() => navigate('/login')}/>
      </form>
    </Card>
  </AuthAutoRedirect>);
};

export default Register;
