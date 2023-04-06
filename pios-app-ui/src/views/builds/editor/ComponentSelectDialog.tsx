import React, {useRef} from 'react';
import {Dialog} from 'primereact/dialog';
import Type from '../../../views/component-search/Type';
import ComponentSearch from '../../../views/component-search/ComponentSearch';
import {changeBuildComponent} from '../service/BuildService';
import BuildResponse from '~/views/builds/service/BuildResponse';
import useToastContext from '../../../context/ToastContext';
import {apiToToast, showMessagesWithoutReference} from '../../../common/messages/messageHelper';
import {Messages} from 'primereact/messages';
import {AxiosError} from 'axios/index';
import BasicResponse from '~/common/messages/BasicResponse';
import ComponentResponse from '~/views/component-search/ComponentResponse';

interface ComponentSelectDialogProps {
  build: BuildResponse;
  onHide: (added: boolean, build?: BuildResponse) => void;
  visible: boolean;
  type?: Type;
}

const ComponentSelectDialog = (props: ComponentSelectDialogProps) => {
  const {toast} = useToastContext();
  const messages = useRef<Messages>(null);

  const hide = (added: boolean, build?: BuildResponse) => {
    props.onHide(added, build);
  };

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);
  };

  const onComponentSelected = (component: ComponentResponse) => {
    changeBuildComponent({
      link: props.build.link,
      componentId: component.id,
      add: true
    }).then((res) => {
      if (!res) {
        return;
      }

      toast.current?.show(apiToToast(res.message));
      hide(true, res.build);
    }).catch(handleRequestFailure);
  };

  return (
    <Dialog header={`Select a ${props.type}`} onHide={() => hide(false)} visible={props.visible} resizable={false}
      draggable={false} style={{width: '90vw'}}>
      <Messages ref={messages}/>
      <ComponentSearch modalMode type={props.type} onComponentSelected={onComponentSelected}/>
    </Dialog>
  );
};

export default ComponentSelectDialog;
