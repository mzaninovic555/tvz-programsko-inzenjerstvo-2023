import React, {useEffect, useState} from 'react';
import {Card} from 'primereact/card';
import Post from '../../views/forum/Post';
import BuildResponse from '../../views/builds/service/BuildResponse';
import {getForumPostById} from '../../views/forum/ForumService';
import {useParams} from 'react-router-dom';

const ForumEntry = () => {
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

  return (
    <Card title={post?.title}>
      <p>
        {post?.content}
      </p>
    </Card>
  );
};

export default ForumEntry;
