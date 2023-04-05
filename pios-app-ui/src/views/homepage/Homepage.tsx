import React, {useEffect, useState} from 'react';
import BuildResponse from '~/views/builds/service/BuildResponse';
import {getTopRatedBuilds} from './HomepageService';
import ComponentResponse from '~/views/component-search/ComponentResponse';
import TopComponents from './TopComponents';

const Homepage = () => {
  const [topBuilds, setTopBuilds] = useState<BuildResponse[]>();
  const [topComponents, setTopComponents] = useState<ComponentResponse[]>();

  useEffect(() => {
    void fetchTopComponents();
    void fetchTopBuilds();
  }, []);

  const fetchTopComponents = async () => {
    const res = await getTopRatedBuilds().catch(console.error);
    if (!res) {
      return;
    }
    setTopComponents(res);
  };

  const fetchTopBuilds = async () => {
    setTopComponents([]);
  };

  return (
    <>
      <TopComponents components={topComponents}/>
    </>
  );
};

export default Homepage;
