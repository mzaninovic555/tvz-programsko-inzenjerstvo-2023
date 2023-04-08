import React from 'react';
import Spinner from '../../components/Spinner';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import Post from '~/views/forum/Post';
import './Homepage.css';
import {normalize} from '../../common/dateHelper';
import {useNavigate} from 'react-router-dom';

interface LatestBuildsProps {
  posts?: Post[];
  error?: string;
}

const LatestPosts = (props: LatestBuildsProps) => {
  const navigate = useNavigate();

  if (!props.posts) {
    return (<Spinner height="unset" text="Loading recent posts..." error={props.error}/>);
  }

  const header = (<h1 className="text-center text-0 strikethrough">Latest posts</h1>);

  const createdAtBodyTemplate = (post: Post) => (<span>{new Date(post.createdAt).toLocaleString()}</span>);
  const priceBodyTemplate = (post: Post) => (<span>{normalize(post.totalPrice || 0)}€</span>);
  const navigateToBuild = (e: Post) => navigate(`/forum/${e.id}`);

  return (
    <>
      {header}
      <DataTable value={props.posts} selectionMode="single" onSelectionChange={(e) => navigateToBuild(e.value)}>
        <Column header="Title" field="title"/>
        <Column header="Price" body={priceBodyTemplate}/>
        <Column header="Author" field="authorUsername"/>
        <Column header="Created at" body={createdAtBodyTemplate}/>
      </DataTable>
    </>
  );
};

export default LatestPosts;
