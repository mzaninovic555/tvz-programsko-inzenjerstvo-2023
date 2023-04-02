import React, {useEffect, useReducer, useRef, useState} from 'react';
import AuthAutoRedirect from '../../common/auth/AuthAutoRedirect';
import {Card} from 'primereact/card';
import {
  deactivateAccount,
  getUserSettings,
  updateSettings,
  validatePassword
} from './UserSettingsService';
import FormInputText from '../../components/FormInputText';
import InputReducer, {emptyState, reducer} from '../../common/hooks/Reducer';
import FormTextarea from '../../components/FormTextarea';
import {Button} from 'primereact/button';
import useToastContext from '../../context/ToastContext';
import {
  apiToMessages,
  apiToToast,
  showMessagesWithoutReference
} from '../../common/messages/messageHelper';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';
import {Messages} from 'primereact/messages';
import {formatDate} from '../../common/dateHelper';
import AccountType from './AccountType';
import Spinner from '../../components/Spinner';
import {Dialog} from 'primereact/dialog';
import {useDebounce} from 'primereact/hooks';
import useAuthContext from '../../context/AuthContext';
import {userDeactivatedMessage} from '../../common/messages/LocalMessages';

const UserSettings = () => {
  const emailValidator = (s?: string) => !s ? undefined : s.length > 100 ? 'Email has to be less than 100 characters long' : undefined;
  const descValidator = (s?: string) => !s ? undefined : s.length > 200 ? 'Description has to be less than 200 characters long' : undefined;
  const passwordValidator = (req: boolean, s?: string) => !s && req ? 'This field is required' :
    req && ((s?.length || 0) > 100 || (s?.length || 0) < 8) ? 'Password should be between 8 and 100 characters' : undefined;

  const {toast} = useToastContext();
  const messages = useRef<Messages>(null);
  const [submitted, setSubmitted] = useState(false);
  const [anyPasswordPresent, setAnyPasswordPresent] = useState(false);

  const [activationVisible, setActivationVisible] = useState(false);
  const [requesting, setRequesting] = useState(false);
  const [username, setUsername] = useState('');
  const [creationDate, setCreationDate] = useState<Date>();
  const [emailInput, dispatchEmail] = useReducer<InputReducer<string>>(reducer, {...emptyState, validator: emailValidator});
  const [descriptionInput, dispatchDescription] = useReducer<InputReducer<string>>(reducer, {...emptyState, validator: descValidator});
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [newPasswordRepeat, setNewPasswordRepeat] = useState('');
  const [passwordErrors, setPasswordErrors] = useState<(string | undefined)[]>(['', '', '']);
  const [accountType, setAccountType] = useState<AccountType>();

  const auth = useAuthContext();
  const accountDeletionMessages = useRef<Messages>(null);
  const [deactivateButtonEnabled, setDeactivateButtonEnabled] = useState(false);
  const [deactivatePasswordConfirm, debouncedDeactivatePasswordConfirm, setDeactivatePasswordConfirm] =
      useDebounce('', 200) as [string, string, React.Dispatch<React.SetStateAction<string>>];

  useEffect(() => void fetchSettings(), []);

  useEffect(() => {
    const fields = [oldPassword, newPassword, newPasswordRepeat];
    const anyValue = fields.some((x) => x);

    setAnyPasswordPresent(anyValue);

    const newVal: (string | undefined)[] = [];
    if (anyValue) {
      for (let i = 0; i < fields.length; i++) {
        if (!fields[i]) {
          newVal[i] = 'This field is required.';
        }
      }
    }

    for (const i of [0, 1, 2]) {
      if (!newVal[i]) {
        newVal[i] = passwordValidator(anyValue, fields[i]);
      }
    }

    if (!newVal[1] && !newVal[2] && newPassword != newPasswordRepeat) {
      newVal[1] = newVal[2] = 'Passwords do not match';
    }

    setPasswordErrors(newVal);
  }, [oldPassword, newPassword, newPasswordRepeat]);

  useEffect(() => {
    if (!submitted) {
      return;
    }
    setSubmitted(false);

    void runUpdateSettings();
  }, [submitted]);

  useEffect(() => {
    if (!debouncedDeactivatePasswordConfirm || passwordValidator(true, debouncedDeactivatePasswordConfirm)) {
      setDeactivateButtonEnabled(false);
      return;
    }
    void fetchValidatePassword();
  }, [debouncedDeactivatePasswordConfirm]);

  const fetchValidatePassword = async () => {
    const result = await validatePassword(debouncedDeactivatePasswordConfirm).catch(handleValidateFailure);
    if (!result) {
      setDeactivateButtonEnabled(false);
      return;
    }
    accountDeletionMessages.current?.clear();
    setDeactivateButtonEnabled(true);
  };

  const handleValidateFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    accountDeletionMessages.current?.replace(msgs.map((x) => apiToMessages(x)));
    setDeactivateButtonEnabled(false);
  };

  const fetchDeactivateAccount = async () => {
    const result = await deactivateAccount(auth.auth.info?.username).catch(handleValidateFailure);
    if (!result) {
      return;
    }
    auth.setToken(undefined);
    toast.current?.show(apiToToast(userDeactivatedMessage));
  };

  const updateFailHandler = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);

    const ref = msgs.filter(((x) => x.reference));
    ref.forEach((msg) => {
      switch (msg.reference) {
        case 'email':
          dispatchEmail({type: 'changeError', error: msg.content});
          break;
        case 'description':
          dispatchDescription({type: 'changeError', error: msg.content});
          break;
        case 'oldPassword':
        case 'newPassword':
        case 'newPasswordRepeat':
          setPasswordErrors((prev) => {
            const i = ['oldPassword', 'newPassword', 'newPasswordRepeat'].findIndex(((x) => x == msg.reference));
            const copy = prev.slice();
            copy[i] = msg.content;
            return copy;
          });
          break;
        default:
          console.warn('Unknown reference', msg);
      }
    });
  };

  const runUpdateSettings = async () => {
    messages.current?.clear();

    setRequesting(true);
    const response = await updateSettings({
      description: descriptionInput.value,
      email: emailInput.value || null,
      oldPassword: oldPassword || null,
      newPassword: newPassword || null,
      newPasswordRepeat: newPasswordRepeat || null
    }).catch(updateFailHandler);
    setRequesting(false);

    if (!response) {
      return;
    }

    toast.current?.show(apiToToast(response));
    void fetchSettings();
  };

  const fetchSettings = async () => {
    const settings = await getUserSettings().catch(updateFailHandler);
    if (!settings) {
      return;
    }

    setUsername(settings.username);
    dispatchEmail({type: 'change', value: settings.email || ''});
    dispatchDescription({type: 'change', value: settings.description || ''});
    setOldPassword('');
    setNewPassword('');
    setNewPasswordRepeat('');
    setAccountType(settings.accountType);
    setCreationDate(new Date(settings.creationDate));
  };

  const onFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setSubmitted(true);
  };

  const title = (
    <>
      <i className={`pi pi-${accountType == AccountType.GITHUB ? 'github' : 'user'} mr-1`}/>
      <span>User settings</span>
    </>
  );

  return (<AuthAutoRedirect loggedInToHome={false} customLocation="/login">
    <>
      {!username && <Spinner text="Loading user settings..."/>}
      {username &&
          <Card title={title}>
            <Messages ref={messages}/>
            <form onSubmit={onFormSubmit}>
              <div className="flex">
                <FormInputText className="w-6 pr-1" inputClassName="w-full" name="Username" value={username} disabled/>
                <FormInputText className="w-6 pl-1" inputClassName="w-full" name="Created at" value={formatDate(creationDate)} disabled/>
              </div>
              {accountType && accountType == AccountType.LOCAL &&
                  <FormInputText inputClassName="w-full" name="Email" value={emailInput.value} maxLength={100}
                    onChange={(v) => dispatchEmail({type: 'change', value: v})} type="email"
                    error={emailInput.error} disabled={accountType && accountType != AccountType.LOCAL}/>}
              <FormTextarea name="Description" value={descriptionInput.value} maxLength={200}
                error={descriptionInput.error} rows={7} inputClassName="w-full"
                onChange={(e) => dispatchDescription({type: 'change', value: e})} />
              {accountType && accountType == AccountType.LOCAL && <hr className="my-4"/>}
              {accountType && accountType == AccountType.LOCAL &&
                  <FormInputText inputClassName="w-full" name="Old Password" value={oldPassword}
                    error={passwordErrors[0]} onChange={setOldPassword} maxLength={100} type="password"
                    disabled={accountType && accountType != AccountType.LOCAL} required={anyPasswordPresent}/>}
              {accountType && accountType == AccountType.LOCAL &&
                  <FormInputText inputClassName="w-full" name="New Password" value={newPassword}
                    error={passwordErrors[1]} onChange={setNewPassword} maxLength={100} type="password"
                    disabled={accountType && accountType != AccountType.LOCAL} required={anyPasswordPresent}/>}
              {accountType && accountType == AccountType.LOCAL &&
                  <FormInputText inputClassName="w-full" name="Repeat New Password" maxLength={100}
                    error={passwordErrors[2]} type="password" required={anyPasswordPresent}
                    onChange={setNewPasswordRepeat} value={newPasswordRepeat}
                    disabled={accountType && accountType != AccountType.LOCAL}/>}
              <Button loading={requesting} className="w-9rem mr-3" type="submit" label="Save" icon="pi pi-save"/>
              <Button className="w-9rem" type="button" label="Deactivate" icon="pi pi-trash"
                severity="danger" onClick={() => setActivationVisible(true)} />
            </form>
            <Dialog header="Account deactivation" visible={activationVisible} onHide={() => setActivationVisible(false)}>
              <p className="p-0 mt-0 mb-5 font-semibold">
                Are you sure you want to deactivate your account?
                THIS CANNOT BE UNDONE!
              </p>
              <form>
                <Messages ref={accountDeletionMessages} />
                {accountType && accountType !== AccountType.GITHUB &&
                  <FormInputText className="mt-3" name={'Enter your password'} value={deactivatePasswordConfirm}
                    onChange={setDeactivatePasswordConfirm} />}
                <Button className="w-9rem" type="button" label="Deactivate" icon="pi pi-trash"
                  severity="danger" disabled={!deactivateButtonEnabled && (accountType && accountType !== AccountType.GITHUB)}
                  onClick={fetchDeactivateAccount} />
              </form>
            </Dialog>
          </Card>}
    </>
  </AuthAutoRedirect>);
};

export default UserSettings;
