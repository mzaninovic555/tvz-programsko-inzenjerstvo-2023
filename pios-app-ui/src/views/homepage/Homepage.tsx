import React, {useEffect, useState} from 'react';
import {getLatestPosts, getTopRatedBuilds} from './HomepageService';
import ComponentResponse from '~/views/component-search/ComponentResponse';
import TopComponents from './TopComponents';
import LatestPosts from './LatestPosts';
import Post from '~/views/forum/Post';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';

const Homepage = () => {
  const [latestPosts, setLatestPosts] = useState<Post[]>();
  const [topComponents, setTopComponents] = useState<ComponentResponse[]>();
  const [postsError, setPostsError] = useState<string>();
  const [componentsError, setComponentsError] = useState<string>();

  useEffect(() => {
    void fetchTopComponents();
    void fetchLatestPosts();
  }, []);

  const handleRequestFailure = (error: AxiosError<BasicResponse | undefined>, fn: (msg: string) => void) => {
    const err = error?.response?.data?.messages?.[0].content || error.message;
    fn(err);
  };

  const fetchTopComponents = async () => {
    const res = await getTopRatedBuilds().catch((e) => handleRequestFailure(e, setComponentsError));
    if (!res) {
      return;
    }
    setTopComponents(res);
  };

  const fetchLatestPosts = async () => {
    const res = await getLatestPosts(5).catch((e) => handleRequestFailure(e, setPostsError));
    if (!res) {
      return;
    }
    setLatestPosts(res);
  };

  return (
    <>
      <div className="mt-2 mb-5">
        <LatestPosts posts={latestPosts} error={postsError}/>
      </div>
      <TopComponents components={topComponents} error={componentsError}/>
    </>
  );
};

export default Homepage;
