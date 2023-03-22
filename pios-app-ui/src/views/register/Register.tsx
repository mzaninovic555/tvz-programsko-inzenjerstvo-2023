import React, {useEffect, useReducer, useRef, useState} from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';
import InputReducer, {emptyState, reducer} from "../../common/hooks/Reducer";
import {Messages} from "primereact/messages";
import useToastContext from "../../context/ToastContext";
import {apiToMessages} from "../../common/messages/messageHelper";
import {Card} from "primereact/card";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import {InputTextarea} from "primereact/inputtextarea";
import {register} from "../../views/register/RegisterService";

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

  const [submitted, setSubmitted] = useState(false);
  const [requesting, setRequesting] = useState(false);

  const messages = useRef<Messages>(null);
  const {toast} = useToastContext();

  const onFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    dispatchUsername({type: 'validate'});
    dispatchPassword({type: 'validate'});
    dispatchEmail({type: 'validate'});
    dispatchDescription({type: 'validate'});
    setSubmitted(true);
  };

  useEffect(() => {
    if (!submitted || usernameInput.error || passwordInput.error || emailInput.error || descriptionInput.error) {
      return;
    }
    setSubmitted(false);
    void doRegister();
  }, [submitted]);

  const doRegister = async () => {
    messages.current?.clear();
    setRequesting(true);
    const response = await register(emailInput.value, usernameInput.value, passwordInput.value, descriptionInput.value)
                          .catch(console.error);
    setRequesting(false);

    if (!response) {
      return;
    }

    if (response.message) {
      messages.current?.show(apiToMessages(response.message));
    }
  };

  return (<AuthAutoRedirect loggedInToHome={true}>
    <Card title="Register" className="w-4 m-auto card-content-no-bottom-margin">
      <Messages ref={messages}/>
      <form onSubmit={onFormSubmit} className="flex flex-column m-auto">
        <InputText className="mb-1" placeholder="Email" value={emailInput.value} autoComplete="email"
                   onChange={(e) => dispatchEmail({type: 'change', value: e.target.value})} type="email"/>
        <small className="p-error block mb-1">{emailInput.error}</small>
        <InputText placeholder="Username" className="mb-1" value={usernameInput.value} autoComplete="username"
                   onChange={(e) => dispatchUsername({type: 'change', value: e.target.value})}/>
        <small className="p-error block mb-1">{usernameInput.error}</small>
        <InputText className="mb-1" placeholder="Password" value={passwordInput.value} autoComplete="current-password"
                   onChange={(e) => dispatchPassword({type: 'change', value: e.target.value})} type="password"/>
        <small className="p-error block mb-1">{passwordInput.error}</small>
        <InputTextarea placeholder="Description" value={descriptionInput.value} autoComplete="current-password" rows={5}
                       onChange={(e) => dispatchDescription({type: 'change', value: e.target.value})} />
        <small className="p-error block mb-1">{descriptionInput.error}</small>
        <Button className="mt-2" type="submit" label="Register" loading={requesting}/>
      </form>
    </Card>
  </AuthAutoRedirect>);
};

export default Register;
