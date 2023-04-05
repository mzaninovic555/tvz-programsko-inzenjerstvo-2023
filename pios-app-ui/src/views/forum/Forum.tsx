import React, {useEffect, useRef, useState} from 'react';
import {Card} from 'primereact/card';
import {deleteForumPost, getForumPosts} from '../../views/forum/ForumService';
import {AxiosError} from 'axios';
import BasicResponse from '../../common/messages/BasicResponse';
import {apiToToast, showMessagesWithoutReference} from '../../common/messages/messageHelper';
import {Messages} from 'primereact/messages';
import {Column} from 'primereact/column';
import {DataTable} from 'primereact/datatable';
import Spinner from '../../components/Spinner';
import Post from '~/views/forum/Post';
import {useNavigate} from 'react-router-dom';
import useToastContext from '../../context/ToastContext';
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';
import {useDebounce} from 'primereact/hooks';
import {clearedFilters} from '../../common/messages/LocalMessages';
import {ConfirmDialog} from 'primereact/confirmdialog';
import useAuthContext from '../../context/AuthContext';

const Forum = () => {
  const [posts, setPosts] = useState<Post[]>();
  const [forumTitleSearch, debouncedForumTitleSearch, setForumTitleSearch] =
      useDebounce('', 500) as [string, string, React.Dispatch<React.SetStateAction<string | undefined>>];
  const [forumToDelete, setForumToDelete] = useState<Post>();

  const auth = useAuthContext();
  const {toast} = useToastContext();
  const navigate = useNavigate();
  const messages = useRef<Messages>(null);

  useEffect(() => void fetchPosts(''), []);

  useEffect(() => void fetchPosts(debouncedForumTitleSearch), [debouncedForumTitleSearch]);

  const fetchPosts = async (search: string) => {
    const list = await getForumPosts(search).catch(handleRequestFailure);
    if (!list) {
      return;
    }
    setPosts(list);
  };

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    const msgs = error.response?.data?.messages ?? [];
    showMessagesWithoutReference(msgs, messages);
  };

  const navigateToBuild = (e: Post) => {
    navigate(`/forum/${e.id}`);
  };

  const clearFilters = () => {
    setForumTitleSearch('');
    toast.current?.show(apiToToast(clearedFilters));
  };

  const header = () => {
    return <>
      <div className="flex align-items-center flex-wrap mb-2">
        <span className="p-input-icon-right mr-2 mb-1">
          <i className="pi pi-search"/>
          <InputText type="text" value={forumTitleSearch} placeholder="Search"
            onChange={(e) => setForumTitleSearch(e.target.value)} />
        </span>
        <Button icon="pi pi-delete-left" label="Clear filters" onClick={clearFilters} className="mb-1"
          disabled={!debouncedForumTitleSearch}/>
      </div>
    </>;
  };

  const actionsTemplate = (post: Post) => {
    if (auth.auth.info?.username != post.authorUsername) {
      return <span></span>;
    }
    return (<div>
      <Button onClick={() => deleteForumClick(post)} icon="pi pi-trash" className="ml-1 p-button-danger"/>
    </div>);
  };

  const deleteForumClick = (obj: Post) => {
    setForumToDelete(obj);
  };

  const deleteForumConfirm = () => {
    deleteForumPost(forumToDelete!.id).then((res) => {
      toast.current?.show(res.messages.map(apiToToast));
      setPosts((prev) => prev?.filter((b) => b.id != forumToDelete?.id));
      setForumToDelete(undefined);
    }).catch(handleRequestFailure);
  };

  const priceBodyTemplate = (post: Post) => {
    return <span>{`${post.totalPrice}€`}</span>;
  };

  const createdAtBodyTemplate = (post: Post) => {
    return <span>{new Date(post.createdAt).toLocaleString()}</span>;
  };

  const body = (<>
    <Messages ref={messages} />
    {header()}
    {posts == undefined && <Spinner text="Loading forum posts"/>}
    {posts && posts.length == 0 &&
      <p className="mt-6 text-center">
        <i className="pi pi-info-circle"/> No forum posts found
      </p>}
    {posts && posts.length > 0 &&
      <DataTable value={posts} paginator rows={10} selectionMode="single"
        onSelectionChange={(e) => navigateToBuild(e.value)}>
        <Column sortable header="Title" field={'title'} />
        <Column header="Author" field={'authorUsername'} />
        <Column sortField={'totalPrice'} sortable header="Price" body={priceBodyTemplate} />
        <Column sortField={'createdAt'} sortable header="Creation Date" body={createdAtBodyTemplate} />
        <Column body={actionsTemplate} />
      </DataTable>}
  </>);

  return (<>
    <Card title="Forum posts">
      {body}
    </Card>
    <ConfirmDialog header="Delete post?" message="Are you sure you want to delete this post?"
      acceptIcon="pi pi-trash"
      reject={() => setForumToDelete(undefined)} acceptLabel="Yes"
      visible={forumToDelete != undefined}
      accept={deleteForumConfirm} acceptClassName="p-button-danger"/>
  </>);
};

export default Forum;
