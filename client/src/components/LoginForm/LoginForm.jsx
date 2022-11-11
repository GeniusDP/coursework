import React, { useContext, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import { AuthContext } from "../../App";
import loginFetch from "../../customFetch";
import CustomFormInput from "../CustomFormInput.jsx/CustomFormInput";
import "./../component-styles.css";
import "./login-form-styles.css";

const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { setAccessToken, setRefreshToken } = useContext(AuthContext);
  const [isLoginError, setIsLoginError] = useState(false);
  const [redirect, setRedirect] = useState(false);

  async function performLogin() {
    setIsLoginError(false);
    setRedirect(false);
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
    <div className="login-form-wrapper">
      <div className="login-form">
        <div className="large-font-text">Login to your account</div>
        {isLoginError && <div>Not valid data!</div>}
        <div>
          <CustomFormInput
            id={"username-login-input"}
            label={"Username"}
            onChange={(event) => setUsername(event.target.value)}
            placeholder={"username"}
            value={username}
          />
          <CustomFormInput
            id={"password-login-input"}
            label={"Password"}
            onChange={(event) => setPassword(event.target.value)}
            placeholder={"password"}
            value={password}
            type={"password"}
          />
        </div>
        <div>
          <button
            className={"btn btn-outline-success btn-lg login-button"}
            onClick={performLogin}
          >
            Login
          </button>
        </div>
        <div>
          <Link to="/register">Don`t have an account? Register now!</Link>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;
