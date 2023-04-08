import React, {useEffect, useRef, useState} from 'react';
import {Link, useParams} from 'react-router-dom';
import {changeBuildComponent, getBuildInfo} from '../service/BuildService';
import BuildResponse from '~/views/builds/service/BuildResponse';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';
import Spinner from '../../../components/Spinner';
import classes from './BuildEditor.module.css';
import {Card} from 'primereact/card';
import {Button} from 'primereact/button';
import {DataTable} from 'primereact/datatable';
import BuildEditDialog from './BuildEditDialog';
import useAuthContext from '../../../context/AuthContext';
import {Message} from 'primereact/message';
import {apiToToast, showMessagesWithoutReference} from '../../../common/messages/messageHelper';
import {linkCopiedError, linkCopiedSuccess} from '../../../common/messages/LocalMessages';
import useToastContext from '../../../context/ToastContext';
import {Column} from 'primereact/column';
import Type from '../../component-search/Type';
import ComponentSelectDialog from './ComponentSelectDialog';
import {Image} from 'primereact/image';
import {normalize} from '../../../common/dateHelper';
import {Messages} from 'primereact/messages';
import ForumCreateDialog from '../../../views/forum/ForumCreateDialog';
import ComponentResponse from '~/views/component-search/ComponentResponse';

const BuildEditor = () => {
  const params = useParams();
  const [build, setBuild] = useState<BuildResponse>();
  const [componentDefs] = useState(Object.values(Type).map((x) => ({type: x})));
  const [selectorType, setSelectorType] = useState<Type>();
  const [showEdit, setShowEdit] = useState(false);
  const [showCreate, setShowCreate] = useState(false);
  const [loadError, setLoadError] = useState<string>();
  const {auth} = useAuthContext();
  const {toast} = useToastContext();
  const messages = useRef<Messages>(null);

  const canEdit = build?.isFinalized ? false : build?.ownerUsername ? build?.ownerUsername == auth.info?.username : true;

  const buildLink = params.buildLink || '';
  if (!buildLink) {
    console.warn('Missing buildLink on BuildEditor', params.buildLink);
    throw new Error('Missing buildLink');
  }

  const handleBuildLoadFailure = (error: AxiosError<BasicResponse>) => {
    if (!error.response?.data?.messages?.length) {
      return;
    }
    setLoadError(error.response.data.messages[0].content);
  };

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    if (!error.response?.data?.messages?.length) {
      return;
    }
    showMessagesWithoutReference(error.response.data.messages, messages);
  };

  useEffect(() => {
    void fetchBuildInfo();
  }, []);

  const fetchBuildInfo = async () => {
    const res = await getBuildInfo(buildLink).catch(handleBuildLoadFailure);
    if (!res) {
      return;
    }
    setBuild(res);
  };

  if (!build) {
    return <Spinner text="Build loading..." error={loadError}/>;
  }

  const onDialogHide = (changed: boolean, newBuild?: BuildResponse) => {
    if (changed && newBuild) {
      setBuild(newBuild);
    }
    setShowEdit(false);
  };

  const onSelectHide = (changed: boolean, newBuild?: BuildResponse) => {
    if (changed && newBuild) {
      setBuild(newBuild);
    }
    setSelectorType(undefined);
  };

  const onForumDialogHide = () => {
    setShowCreate(false);
  };

  const finalLink = `${window.location.origin}/builds/${build.link}`;

  const copyLink = () => {
    navigator.clipboard.writeText(finalLink)
      .then(() => toast.current?.show(apiToToast(linkCopiedSuccess)))
      .catch(() => apiToToast(linkCopiedError));
  };

  const linkHeader = (<>
    {!build.ownerUsername && !build.isFinalized &&
      <Message severity="warn" className="w-full mb-2"
        text="This build is not owned by any user, anyone with a link to it can edit it, finalize the build before sharing"/>}
    <Message severity="info" className="w-full mb-2" text={<div className="flex align-items-center flex-wrap">
      <span>Shareable link: {finalLink}</span>
      <Button className="ml-3" icon="pi pi-copy" onClick={copyLink}/>
    </div>}/>
  </>);

  const compatibilityMessagesToList = (
    <div>
      {build.compatibilityMessages.map((message) => <li>{message.content}</li>)}
    </div>
  );

  const compatibilityHeader = (<>
    {(!build.compatibilityMessages || build.compatibilityMessages.length == 0) &&
      <Message severity="success" className="w-full mb-2" text="No detected compatibility issues"/>}
    {build.compatibilityMessages && build.compatibilityMessages.length > 0 &&
        <Message severity="warn" className="w-full mb-2" content={compatibilityMessagesToList}/>}
  </>);

  const typeTemplate = (obj: {type: string}) => {
    return <Link className="pios-link" to={'/component-search?type=' + obj.type}>{obj.type}</Link>;
  };

  const priceTemplate = (obj: {type: string}) => {
    const matching = build.components.filter((comp) => comp.type == obj.type);
    return (
      <span>{matching.length == 0 ? '-' : `${normalize(matching[0].price)}€`}</span>
    );
  };

  const removeComponent = (component: ComponentResponse) => {
    changeBuildComponent({
      add: false,
      componentId: component.id,
      link: build.link
    }).then((res) => {
      if (!res) {
        return;
      }

      toast.current?.show(apiToToast(res.message));
      setBuild(res.build);
    }).catch(handleRequestFailure);
  };

  const actionTemplate = (obj: {type: string}) => {
    const matching = build.components.filter((comp) => comp.type == obj.type);
    if (matching.length == 0) {
      return (<span>-</span>);
    }
    return (
      <Button icon="pi pi-times" className="p-button-danger" label="Remove" disabled={!canEdit}
        onClick={() => removeComponent(matching[0])} />);
  };

  const selectTemplate = (obj: {type: string}) => {
    const matching = build.components.filter((comp) => comp.type == obj.type);

    if (matching.length == 0) {
      return (
        <Button onClick={() => setSelectorType(obj.type as Type)} icon="pi pi-plus" label={`Choose a ${obj.type}`}
          disabled={!canEdit}/>);
    }
    return (<>
      {matching.map((x) => <div key={x.id} className="flex align-items-center">
        <Image className="pios-image w-3rem mr-1 inline-block" alt={x.name} preview
          src={x.imageBase64 ? `data:image/jpeg;base64,${x.imageBase64}` : `/unknown.jpg`}/>
        {x.name}
      </div>)}
    </>);
  };

  const totalPrice = build.components.map((x) => x.price).reduce((a, b) => a + b, 0);

  return (<>
    <BuildEditDialog visible={showEdit} onHide={onDialogHide} build={build} published={build.isPublished}/>
    <ForumCreateDialog build={build} onHide={onForumDialogHide} visible={showCreate} />
    <ComponentSelectDialog visible={selectorType != undefined} onHide={onSelectHide} type={selectorType} build={build}/>
    {linkHeader}
    {compatibilityHeader}
    <Card className={classes.card}>
      <div className="flex flex-wrap">
        <div className="col-4">
          <h3>Title</h3>
          <span>{build.title || 'Untitled build'}</span>
        </div>
        <div className="col-4">
          <h3>Finalized</h3>
          <span>{build.isFinalized ? 'Yes' : 'No'}</span>
        </div>
        <div className="col-4">
          <h3>Public</h3>
          <span>{build.isPublic ? 'Yes' : 'No'}</span>
        </div>
        <div style={{wordWrap: 'break-word'}} className="col-12">
          <h3>Description</h3>
          <span>{build.description || 'N/A'}</span>
        </div>
      </div>
    </Card>
    <div className="flex flex-row align-items-end my-2 justify-content-end">
      {!build.isPublished && auth.authenticated && build.ownerUsername == auth.info?.username &&
        <Button label="Publish" icon="pi pi-upload" className="p-button-success mr-1" onClick={() => setShowCreate(true)}
          disabled={build.isPublished || !auth.authenticated || build.ownerUsername != auth.info?.username || !auth.authenticated || !build.isPublic}
          tooltip="Build has to be public" tooltipOptions={{position: 'top', showOnDisabled: true}}/>}
      <Button label="Edit Build Info" icon="pi pi-file-edit" onClick={() => setShowEdit(true)}
        disabled={!canEdit && (!auth.authenticated || build.ownerUsername != auth.info?.username)}/>
    </div>
    <h3>Components</h3>
    <Messages ref={messages}/>
    <DataTable value={componentDefs} className="mb-2">
      <Column header="Type" body={typeTemplate} field="type"/>
      <Column header="Selection" body={selectTemplate}/>
      <Column header="Price" body={priceTemplate}/>
      <Column header="Action" body={actionTemplate}/>
    </DataTable>
    <div className="text-right">
      <h3>Total: {totalPrice == 0 ? '-' : normalize(totalPrice) + '€'}</h3>
    </div>
  </>);
};

export default BuildEditor;
