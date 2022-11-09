import React, { useContext, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../../App";

const AuthProvider = ({ children, roles }) => {
  const { getToken, setToken, removeToken } = useContext(AuthContext);
  const [hasPermission, setHasPermission] = useState(false);
  const [permissionAsked, setPermissionAsked] = useState(false);

  async function askForPermission() {
    const myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer " + getToken());
    const requestOptions = {
      method: "GET",
      headers: myHeaders,
    };


    const responseValidate = await fetch("http://localhost:8081/validate-token", requestOptions);
    if(responseValidate.ok){
      setHasPermission(() => true);
      return;
    }
    const responseRefresh = await fetch("http://localhost:8081/refresh-token", requestOptions);
    if(responseRefresh.ok){
      const jsonResponse = await responseRefresh.json();
      setToken(jsonResponse.token);
      setHasPermission(() => true);
      return;
    }
    removeToken();
    setHasPermission(() => false);
  }

  useEffect(()=>{
    askForPermission().then(()=>{
      setPermissionAsked(true);
    });
  });
  
  if(!permissionAsked){
    return <div>Loading spinner...</div>;
  }
  if(hasPermission){
    return children;
  }
  return <Navigate to="/login" replace/>;
};

export default AuthProvider;