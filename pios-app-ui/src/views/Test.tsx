import React from 'react';
import {useNavigate} from 'react-router-dom';
import {Button} from 'primereact/button';

const Test = () => {
  const navigate = useNavigate();
  return (
    <div>
      <p>hi this is test!</p>
      <Button label="Go to home" onClick={() => navigate('/')}/>
    </div>
  );
};

export default Test;
