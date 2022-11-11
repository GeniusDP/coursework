import React, { useContext, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../../App";
import customFetch from "../../customFetch";
import { parseJwt } from "../../jwtUtil";

const AuthProvider = ({ children, roles }) => {
  const {
    getAccessToken,
    getRefreshToken,
    setAccessToken,
    setRefreshToken,
    removeTokens,
  } = useContext(AuthContext);
  const [hasPermission, setHasPermission] = useState(false);
  const [permissionAsked, setPermissionAsked] = useState(false);
  const [errorOccured, setErrorOccured] = useState(false);


  useEffect(() => {
    if (!permissionAsked) {
      askForPermission().then(() => {
        setPermissionAsked(() => true);
      });
    }
  });

  const askForPermission = async () => {
    const url = "http://localhost:8081/api/auth/validate-token";
    const body = {
      accessToken: getAccessToken(),
      refreshToken: getRefreshToken(),
    };
    try {
      const response = await customFetch(url, body);
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
      setErrorOccured(true);
    }
  };

  const refreshToken = async () => {
    const url = "http://localhost:8081/api/auth/refresh-token";
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

  if(errorOccured){
    alert('Connection with server was not successful:(\nMay be, you are offline.');
    return <Navigate to="/" replace />;
  }

  if (!permissionAsked) {
    return <div>Loading spinner...</div>;
  }
  if (hasPermission) {
    return children;
  }
  return <Navigate to="/login" replace />;
};

export default AuthProvider;
