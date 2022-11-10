import React, { useContext, useState } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../../App";
import loginFetch from "../../customFetch";

const LoginForm = () => {
  const [username, setUsername] = useState("usr");
  const [password, setPassword] = useState("123");
  const { setAccessToken, setRefreshToken } = useContext(AuthContext);
  const [isLoginError, setIsLoginError] = useState(false);
  const [redirect, setRedirect] = useState(false);

  async function performLogin() {
    try {
      const url = "http://localhost:8081/api/auth/login";
      const loginDto = { username, password };
      const response = await loginFetch(url, loginDto);
      const json = await response.json();
      setAccessToken(json.accessToken);
      setRefreshToken(json.refreshToken);
      setRedirect(true);
      setIsLoginError(false);
    } catch (e) {
      setIsLoginError(true);
    }
  }

  if (redirect) {
    return <Navigate to={"/"} />;
  }

  return (
    <div>
      {isLoginError && <div>Not valid data!</div>}
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
