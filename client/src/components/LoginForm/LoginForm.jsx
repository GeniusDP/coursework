import React, { useContext, useState } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../../App";

const LoginForm = () => {
  const [username, setUsername] = useState("usr");
  const [password, setPassword] = useState("123");
  const {setToken} = useContext(AuthContext);
  const [isLoginError, setIsLoginError] = useState(false);
  const [redirect, setRedirect] = useState(false);

  function performLogin() {
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
      username,
      password,
    });

    const requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };

    function handleErrors(response) {
      if (!response.ok) {
        throw Error(response.statusText);
      }
      return response;
    }

    fetch("http://localhost:8081/login", requestOptions)
      .then(handleErrors)
      .then((response) => response.json())
      .then(responseJson => responseJson.token)
      .then((token) => {
        setToken(token);
        setRedirect(true);
        setIsLoginError(false);
      })
      .catch((error) => {
        setIsLoginError(true);
      });
  }

  if(redirect){
    return <Navigate to={"/"}/>;
  }

  if(isLoginError){
    return <div>Not valid data!</div>;
  }

  return (
    <div>
      <input
        value={username}
        onChange={(event) => setUsername(event.target.value)}
      />
      <input
        type={"password"}
        value={password}
        onChange={(event) => setPassword(event.target.value)}
      />
      <button onClick={performLogin}>Login</button>
    </div>
  );
};

export default LoginForm;
