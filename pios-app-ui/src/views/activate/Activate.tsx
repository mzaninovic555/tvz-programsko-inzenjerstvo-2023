import React, {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {activate} from "../../views/activate/ActivateService";

const Activate = () => {

  const navigate = useNavigate();
  const token = window.location.search.substring(1);


  useEffect(() => {
    if (!token) {
      return;
    }
    void doActivate();
  });

  const doActivate = async () => {
    const response = await activate(token);
    if (!response) {
      return;
    }
    console.debug(response);
    navigate("/login?" + response.message?.content);
  }


  return (<> </>);
}

export default Activate;
