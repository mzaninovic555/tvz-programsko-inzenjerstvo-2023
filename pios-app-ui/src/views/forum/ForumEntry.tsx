import React, {useEffect, useState} from 'react';
import {Card} from 'primereact/card';
import Post from '../../views/forum/Post';
import BuildResponse from '../../views/builds/service/BuildResponse';
import {getForumPostById} from '../../views/forum/ForumService';
import classes from '../builds/editor/BuildEditor.module.css';
import {useParams} from 'react-router-dom';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import Type from '../../views/component-search/Type';
import {normalize} from '../../common/dateHelper';
import Spinner from '../../components/Spinner';
import {Image} from 'primereact/image';
import ReactMarkdown from 'react-markdown';

const ForumEntry = () => {
  const [componentDefs] = useState(Object.values(Type).map((x) => ({type: x})));
  const [post, setPost] = useState<Post>();
  const [build, setBuild] = useState<BuildResponse>();

  const params = useParams();

  useEffect(() => void fetchPostBuild(), []);

  const fetchPostBuild = async () => {
    const forumId = params.forumId;
    if (!forumId) {
      console.error('Missing forum id');
      throw new Error('Missing forum id');
    }
    const data= await getForumPostById(params.forumId);
    if (!data) {
      return;
    }
    setBuild(data.build);
    setPost(data.post);
  };

  const typeTemplate = (obj: {type: string}) => {
    return <h4 style={{color: '#6366f1'}} className={'mb-0 mt-1'}>{obj.type}</h4>;
  };

  const selectTemplate = (obj: {type: string}) => {
    const matching = build!.components.filter((comp) => comp.type == obj.type);
    if (matching.length == 0) {
      return <span>No Component Selected</span>;
    }
    return (<>
      {matching.map((x) => <div key={x.id} className="flex align-items-center">
        <Image className="pios-image w-1 mr-1 inline-block" alt={x.name} preview
          src={x.imageBase64 ? `data:image/jpeg;base64,${x.imageBase64}` : `/unknown.jpg`}/>
        {x.name}
      </div>)}
    </>);
  };

  const priceTemplate = (obj: {type: string}) => {
    const matching = build!.components.filter((comp) => comp.type == obj.type);
    return (
      <span>{matching?.length == 0 ? '-' : `${normalize(matching[0].price)}€`}</span>
    );
  };

  return (<>
    {(!post || !build ) && <Spinner text="Loading forum data"/>}
    {post && build && <>
      <Card className={[classes.card, 'mb-2']}>
        <div className={'flex flex-column justify-content-center text-center'}>
          <h4 style={{color: '#6366f1'}} className={'mb-0 mt-1'}>BUILD</h4>
          <h1 className={'mb-1 mt-0'}>{post?.title}</h1>
          <h3 className={'mb-2 mt-0'}>by {post?.authorUsername}</h3>
          <h4 style={{color: '#6366f1'}} className={'mb-2 mt-1'}>Total price: {normalize(post?.totalPrice || 0)}€</h4>
        </div>

        <div style={{wordWrap: 'break-word'}} className={'m-2 mt-4 border-solid border-1 border-round p-2 border-300 surface-200'}>
          <h4 style={{color: '#6366f1'}} className={'mb-0 mt-1'}>Description</h4>
          {(!post.content || post.content === '') && <p>N/A</p>}
          {post.content && post.content !== '' &&
          <ReactMarkdown>
            {post.content}
          </ReactMarkdown>}
        </div>
      </Card>
      <div>
        <h3>Components</h3>
        <DataTable value={componentDefs} className="mb-2">
          <Column header="Type" body={typeTemplate} field="type"/>
          <Column header="Component" body={selectTemplate} field="type"/>
          <Column header="Price" body={priceTemplate}/>
        </DataTable>
      </div>
    </>}
  </>);
};

export default ForumEntry;
