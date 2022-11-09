import React, { useContext, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../../App";

const AuthProvider = ({ children, roles }) => {
  const { getToken, setToken, removeToken } = useContext(AuthContext);
  const [hasPermission, setHasPermission] = useState(false);
  const [permissionAsked, setPermissionAsked] = useState(false);

  async function checkRolePermission() {
    const myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer " + getToken());
    const requestOptions = {
      method: "GET",
      headers: myHeaders,
    };

    const responseRole = await fetch(
      "http://localhost:8081/get-role",
      requestOptions
    );
    if (!responseRole.ok) {
      setHasPermission(() => false);
      return false;
    }
    const json = await responseRole.json();
    const role = json.role;
    return roles.includes(role);
  }

  async function askForPermission() {
    const myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer " + getToken());
    const requestOptions = {
      method: "GET",
      headers: myHeaders,
    };

    const responseValidate = await fetch(
      "http://localhost:8081/validate-token",
      requestOptions
    );
    if (!responseValidate.ok) {
      const responseRefresh = await fetch(
        "http://localhost:8081/refresh-token",
        requestOptions
      );
      if (responseRefresh.ok) {
        const jsonResponse = await responseRefresh.json();
        setToken(jsonResponse.token);
      }else{
        removeToken();
        setHasPermission(() => false);
      }
    }
    if (roles === "*") {
      setHasPermission(() => true);
      return;
    }
    const rolePermission = await checkRolePermission();
    setHasPermission(() => rolePermission);
  }

  useEffect(() => {
    if (!permissionAsked) {
      askForPermission().then(() => {
        setPermissionAsked(() => true);
      });
    }
  });

  if (!permissionAsked) {
    return <div>Loading spinner...</div>;
  }
  if (hasPermission) {
    return children;
  }
  return <Navigate to="/login" replace />;
};

export default AuthProvider;
