import React, {useEffect, useReducer, useRef, useState} from 'react';
import BuildResponse from '~/views/builds/service/BuildResponse';
import {Dialog} from 'primereact/dialog';
import FormInputText from '../../../components/FormInputText';
import InputReducer, {emptyState, reducer} from '../../../common/hooks/Reducer';
import FormTextarea from '../../../components/FormTextarea';
import {InputSwitch} from 'primereact/inputswitch';
import {Button} from 'primereact/button';
import {changeBuildInfo} from '../service/BuildService';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';
import {apiToToast, showMessagesWithoutReference} from '../../../common/messages/messageHelper';
import {Messages} from 'primereact/messages';
import useToastContext from '../../../context/ToastContext';
import useAuthContext from '../../../context/AuthContext';

interface BuildEditDialog {
  build: BuildResponse;
  onHide: (changed: boolean, newBuild?: BuildResponse) => void;
  visible: boolean;
}

const BuildEditDialog = (props: BuildEditDialog) => {
  const titleValidator = (s?: string) => s && s.length > 50 ? 'Title must be 50 characters or less' : '';
  const descValidator = (s?: string) => s && s.length > 500 ? 'Description must be 500 characters or less' : '';

  const [titleInput, dispatchTitle] =
    useReducer<InputReducer<string>>(reducer, {...emptyState, validator: titleValidator});
  const [descInput, dispatchDesc] =
    useReducer<InputReducer<string>>(reducer, {...emptyState, validator: descValidator});
  const [isFinalized, setIsFinalized] = useState(false);
  const [isPublic, setIsPublic] = useState(false);

  const [requesting, setRequesting] = useState(false);
  const [submitted, setSubmitted] = useState(false);

  const messages = useRef<Messages>(null);
  const {toast} = useToastContext();
  const {auth} = useAuthContext();

  useEffect(() => {
    if (!props.visible) {
      return;
    }

    dispatchTitle({type: 'change', value: props.build.title || ''});
    dispatchDesc({type: 'change', value: props.build.description || ''});
    setIsFinalized(props.build.isFinalized);
    setIsPublic(props.build.isPublic);
  }, [props.visible]);

  const hide = (changed: boolean, newBuild?: BuildResponse) => {
    props.onHide(changed, newBuild);
  };

  const onFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    dispatchDesc({type: 'validate'});
    dispatchTitle({type: 'validate'});
    setSubmitted(true);
  };

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);
  };

  useEffect(() => {
    if (!submitted) {
      return;
    }
    setSubmitted(false);

    if (titleInput.error || descInput.error) {
      return;
    }

    void changeBuild();
  }, [submitted]);

  const changeBuild = async () => {
    setRequesting(true);
    const res = await changeBuildInfo({
      link: props.build.link,
      isFinalized: isFinalized,
      isPublic: isPublic,
      title: titleInput.value,
      description: descInput.value
    }).catch(handleRequestFailure);
    setRequesting(false);

    if (res) {
      hide(true, res.build);
      toast.current?.show(apiToToast(res.message));
    }
  };

  const footer = () => {
    return (
      <>
        <Button className="p-button-secondary" onClick={() => hide(false)} label="Cancel"/>
        <Button form="edit-build-form" icon="pi pi-save" loading={requesting} label="Save"/>
      </>
    );
  };

  return (<Dialog header="Edit Build Info" onHide={() => hide(false)} visible={props.visible} resizable={false}
    draggable={false} className="w-7" footer={footer}>
    <Messages ref={messages}/>
    <form id="edit-build-form" onSubmit={onFormSubmit}>
      <FormInputText name="Title" value={titleInput.value} onChange={(v) => dispatchTitle({type: 'change', value: v})}
        error={titleInput.error} maxLength={50}/>
      <FormTextarea name="Description" value={descInput.value} onChange={(v) => dispatchDesc({type: 'change', value: v})}
        maxLength={500} error={descInput.error}/>
      <div className="field">
        <label htmlFor="isFinalized" className="block font-bold">Finalized</label>
        <InputSwitch inputId="isFinalized" checked={isFinalized} onChange={(e) => setIsFinalized(Boolean(e.value))}/>
        {!props.build.ownerUsername && <>
          <br/>
          <small className="p-warning">Warning: Builds made by anonymous users cannot be edited after being finalized</small>
        </>}
        {props.build.ownerUsername && <>
          <label htmlFor="isPublic" className="block font-bold">Public</label>
          <InputSwitch readOnly={!auth.authenticated} inputId="isPublic" checked={isPublic}
            onChange={(e) => setIsPublic(Boolean(e.value))}/>
          <br/>
          <small className="p-info">When a build is marked as public anyone can see it, but only you can edit it</small>
        </>}
      </div>
    </form>
  </Dialog>);
};

export default BuildEditDialog;
