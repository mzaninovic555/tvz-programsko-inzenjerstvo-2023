import React, {useEffect, useReducer, useRef, useState} from 'react';
import BuildResponse from '~/views/builds/service/BuildResponse';
import {Dialog} from 'primereact/dialog';
import FormInputText from '../../components/FormInputText';
import InputReducer, {emptyState, reducer} from '../../common/hooks/Reducer';
import FormTextarea from '../../components/FormTextarea';
import {Button} from 'primereact/button';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';
import {apiToToast, showMessagesWithoutReference} from '../../common/messages/messageHelper';
import {Messages} from 'primereact/messages';
import useToastContext from '../../context/ToastContext';
import {createForumPost} from '../../views/forum/ForumService';
import {useNavigate} from 'react-router-dom';

interface ForumCreateDialog {
  build: BuildResponse;
  onHide: () => void;
  visible: boolean;
}

const ForumCreateDialog = (props: ForumCreateDialog) => {
  const postTitleValidator = (s?: string) => s && s.length > 50 ? 'Title must be 50 characters or less' : '';
  const postContentValidator = (s?: string) => s && s.length > 1000 ? 'Description must be 1000 characters or less' : '';

  const [postTitleInput, dispatchTitle] =
    useReducer<InputReducer<string>>(reducer, {...emptyState, validator: postTitleValidator});
  const [postContentInput, dispatchDesc] =
    useReducer<InputReducer<string>>(reducer, {...emptyState, validator: postContentValidator});

  const [requesting, setRequesting] = useState(false);
  const [submitted, setSubmitted] = useState(false);

  const messages = useRef<Messages>(null);
  const {toast} = useToastContext();
  const navigate = useNavigate();

  useEffect(() => {
    if (!props.visible) {
      return;
    }
    dispatchTitle({type: 'change', value: ''});
    dispatchDesc({type: 'change', value: ''});
  }, [props.visible]);

  const hide = () => {
    props.onHide();
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

    if (postTitleInput.error || postContentInput.error) {
      return;
    }

    void fetchCreateForumPost();
  }, [submitted]);

  const fetchCreateForumPost = async () => {
    setRequesting(true);
    const res = await createForumPost({
      id: props.build.id,
      title: postTitleInput.value,
      content: postContentInput.value
    }).catch(handleRequestFailure);
    setRequesting(false);

    if (res) {
      toast.current?.show(apiToToast(res.message));
      navigate(`/forum/${res.id}`);
    }
  };

  const footer = () => {
    return (
      <>
        <Button className="p-button-secondary" onClick={() => hide()} label="Cancel"/>
        <Button form="edit-build-form" icon="pi pi-save" loading={requesting} label="Publish"/>
      </>
    );
  };

  return (<Dialog header="Create Forum Post" onHide={() => hide()} visible={props.visible} resizable={false}
    draggable={false} className="w-7" footer={footer}>
    <Messages ref={messages}/>
    <form id="edit-build-form" onSubmit={onFormSubmit}>
      <FormInputText name="Title" value={postTitleInput.value} onChange={(v) => dispatchTitle({type: 'change', value: v})}
        error={postTitleInput.error} maxLength={50}/>
      <FormTextarea name="Description" value={postContentInput.value} onChange={(v) => dispatchDesc({type: 'change', value: v})}
        maxLength={1000} error={postContentInput.error}/>
    </form>
  </Dialog>);
};

export default ForumCreateDialog;
