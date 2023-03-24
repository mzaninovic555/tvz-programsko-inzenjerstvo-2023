import React, {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {activate} from './ActivateService';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';

const Activate = () => {
  const navigate = useNavigate();
  const token = window.location.search.substring(1);


  useEffect(() => {
    if (!token) {
      return;
    }
    void doActivate();
  });

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    if (!error.response?.data.messages.length) {
      return;
    }

    navigate('/login?' + error.response?.data.messages[0].content);
  };

  const doActivate = async () => {
    const response = await activate(token).catch(handleRequestFailure);
    if (!response) {
      return;
    }
    navigate('/login?' + response.message?.content);
  };

  return (<h2>Activation...</h2>);
};

export default Activate;
