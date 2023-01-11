import React, { useContext, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../../App";
import customFetch from "../../customFetch";
import { parseJwt } from "../../jwtUtil";
import { Spinner } from "../Spinner/Spinner";

const AuthProvider = ({ children, roles = "*" }) => {
  const {
    getAccessToken,
    getRefreshToken,
    setAccessToken,
    setRefreshToken,
    removeTokens,
  } = useContext(AuthContext);
  const [hasPermission, setHasPermission] = useState(false);
  const [permissionAsked, setPermissionAsked] = useState(false);
  const [errorOccurred, setErrorOccurred] = useState(false);


  useEffect(() => {
    if (!permissionAsked) {
      askForPermission().then(() => {
        setPermissionAsked(() => true);
      });
    }
  });

  const askForPermission = async () => {
    const url = process.env.REACT_APP_AUTH_URL + "/validate-token";
    try {
      const options = {
        headers: {
          "Authorization": "Bearer " + getAccessToken(),
        }
      }
      const response = await fetch(url, options);
      if (response.ok) {
        const { role } = parseJwt(getAccessToken());
        setHasPermission(roles === "*" ? true : roles.includes(role));
      } else {
        const refreshedSuccessfully = await refreshToken();
        if (refreshedSuccessfully) {
          const { role } = parseJwt(getAccessToken());
          setHasPermission(roles === "*" ? true : roles.includes(role));
        } else {
          removeTokens();
          setHasPermission(false);
        }
      }
    } catch (e) {
      setErrorOccurred(true);
    }
  };

  const refreshToken = async () => {
    const url = process.env.REACT_APP_AUTH_URL + "/refresh-token";
    const body = {
      accessToken: getAccessToken(),
      refreshToken: getRefreshToken(),
    };
    try {
      const response = await customFetch(url, body);
      if (response.ok) {
        const json = await response.json();
        setAccessToken(json.accessToken);
        setRefreshToken(json.refreshToken);
        return true;
      }
      return false;
    } catch (e) {
      return false;
    }
  };

  if(errorOccurred){
    alert('Connection with server was not successful:(\nMay be, you are offline.');
    return <Navigate to="/" replace />;
  }

  if (!permissionAsked) {
    return <Spinner/>;
  }
  if (hasPermission) {
    return children;
  }
  return <Navigate to="/login" replace />;
};

export default AuthProvider;
