import React from 'react';
import {useNavigate} from 'react-router-dom';
import {Button} from 'primereact/button';

const Homepage = () => {
  const navigate = useNavigate();
  return (
    <div>
      <p>Homepage!</p>
      <Button label="Go to /test" onClick={() => navigate('/test')}/>
    </div>
  );
};

export default Homepage;
