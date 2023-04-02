import React, {useEffect, useState} from 'react';
import useAuthContext from '../../context/AuthContext';
import {createBuild, deleteBuild, getUserBuilds} from './service/BuildService';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';
import {useNavigate} from 'react-router-dom';
import {Button} from 'primereact/button';
import {apiToToast} from '../../common/messages/messageHelper';
import useToastContext from '../../context/ToastContext';
import BuildResponse from '~/views/builds/service/BuildResponse';
import Spinner from '../../components/Spinner';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {linkCopiedError, linkCopiedSuccess} from '../../common/messages/LocalMessages';
import {ConfirmDialog} from 'primereact/confirmdialog';

const Builds = () => {
  const {auth} = useAuthContext();
  const navigate = useNavigate();
  const {toast} = useToastContext();
  const [requesting, setRequesting] = useState(false);
  const [builds, setBuilds] = useState<BuildResponse[]>();
  const [buildToDelete, setBuildToDelete] = useState<BuildResponse>();

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    if (!error.response?.data?.messages?.length) {
      return;
    }
    toast.current?.show(error.response.data.messages.map(apiToToast));
  };

  const createBuildAndNavigate = async () => {
    setRequesting(true);
    const res = await createBuild().catch(handleRequestFailure);
    setRequesting(false);
    if (!res) {
      return;
    }
    navigate(`/builds/${res.link}`);
  };

  useEffect(() => {
    if (!auth.authenticated) {
      return;
    }
    void fetchUserBuilds();
  }, []);

  const fetchUserBuilds = async () => {
    const res = await getUserBuilds().catch(handleRequestFailure);
    if (!res) {
      return;
    }
    setBuilds(res);
  };

  const createButtonClick = () => {
    void createBuildAndNavigate();
  };

  const copyLink = (link: string) => {
    const finalLink = `${(process.env.FRONTEND_URL || 'http://localhost:1234')}/builds/${link}`;

    navigator.clipboard.writeText(finalLink)
      .then(() => toast.current?.show(apiToToast(linkCopiedSuccess)))
      .catch(() => toast.current?.replace(apiToToast(linkCopiedError)));
  };

  const titleTemplate = (obj: BuildResponse) => <span>{obj.title || 'Untitled build'}</span>;
  const publicTemplate = (obj: BuildResponse) => <span>{obj.isPublic ? 'Yes' : 'No'}</span>;

  const linkTemplate = (obj: BuildResponse) => {
    return (
      <div className="flex align-items-center">
        <span>{obj.link}</span>
        <Button className="ml-1" icon="pi pi-copy" onClick={() => copyLink(obj.link)}/>
      </div>);
  };

  const deleteBuildClick = (obj: BuildResponse) => {
    setBuildToDelete(obj);
  };

  const deleteBuildConfirm = () => {
    deleteBuild(buildToDelete!.link).then((res) => {
      toast.current?.show(res.messages.map(apiToToast));
      setBuilds((prev) => prev?.filter((b) => b.link != buildToDelete?.link));
      setBuildToDelete(undefined);
    }).catch(handleRequestFailure);
  };

  const actionsTemplate = (obj: BuildResponse) => {
    return (<div>
      <Button onClick={() => navigate('/builds/' + obj.link)} icon="pi pi-file-edit"/>
      <Button onClick={() => deleteBuildClick(obj)} icon="pi pi-trash" className="ml-1 p-button-danger"/>
    </div>);
  };

  const buildBrowser = (
    <div>
      {builds == undefined && <Spinner text="Loading builds"/> }
      {builds?.length == 0 && <div className="flex flex-column align-items-center justify-content-center text-center">
        <h2>No builds yet</h2>
      </div>}
      {builds && builds.length > 0 &&
        <DataTable value={builds}>
          <Column header="Title" body={titleTemplate}/>
          <Column header="Link" field="link" body={linkTemplate}/>
          <Column header="Public" body={publicTemplate}/>
          <Column header="Actions" body={actionsTemplate}/>
        </DataTable>
      }
      <Button className="mt-2" label="Create a build" icon="pi pi-plus" onClick={createButtonClick} loading={requesting}/>
    </div>
  );

  return (
    <>
      {!auth.authenticated && <div className="flex flex-column align-items-center justify-content-center text-center" style={{height: '60vh'}}>
        <h2>Anonymous users cannot manage builds, only create them</h2>
        <Button label="Create a build" icon="pi pi-plus" onClick={createButtonClick} loading={requesting}/>
      </div>}
      {auth.authenticated && buildBrowser}
      <ConfirmDialog header="Delete build?" message="Are you sure you want to delete this build?" acceptIcon="pi pi-trash"
        reject={() => setBuildToDelete(undefined)} acceptLabel="Yes" visible={buildToDelete != undefined}
        accept={deleteBuildConfirm} acceptClassName="p-button-danger"/>
    </>
  );
};

export default Builds;
