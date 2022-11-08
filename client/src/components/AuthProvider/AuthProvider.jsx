import React, { useContext, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../../App";

const AuthProvider = ({ children }) => {
  const {getToken, setToken} = useContext(AuthContext);
  const [tokenValid, setTokenValid] = useState(getToken());

  //validate token
  function validateToken() {
    const myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer " + getToken());

    const requestOptions = {
      method: "GET",
      headers: myHeaders,
      redirect: "follow",
    };


    fetch("http://localhost:8081/validate-token", requestOptions)
      .then(response => {
        if(!response.ok) {
          refreshToken();
        }
        setTokenValid(true);
        return response;
      })
      .catch((error) => {
        
      });
  }



  //refresh token
  function refreshToken() {
    const myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer " + getToken());

    const requestOptions = {
      method: "GET",
      headers: myHeaders,
      redirect: "follow",
    };


    fetch("http://localhost:8081/refresh-token", requestOptions)
      .then(response => {
        if(!response.ok) {
          setTokenValid(false);
          throw new Error();
        }
        return response;
      })
      .then(response => response.json())
      .then(responseJson => responseJson.token)
      .then(token => {
        console.log('auth : ' + getToken());
        setTokenValid(true);
        setToken(token);
      })
      .catch((error) => {
        
      });
  }

  useEffect(() => {
    validateToken();
  }, []);

  return (
    <div>
      <div>{tokenValid ? children : <Navigate to="/login" />}</div>
    </div>
  );
};

export default AuthProvider;
