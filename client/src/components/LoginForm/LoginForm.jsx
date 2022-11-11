import React, { useContext, useEffect, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import { AuthContext } from "../../App";
import loginFetch from "../../customFetch";
import useInput from "../../hooks/useInput";
import CustomFormInput from "../CustomFormInput.jsx/CustomFormInput";
import Modal from "../Modal/Modal";
import { Spinner } from "../Spinner/Spinner";
import "./../component-styles.css";
import "./login-form-styles.css";

const LoginForm = () => {
  const username = useInput("", { isEmpty: true, minLength: 3, maxLength: 50 });
  const password = useInput("", { isEmpty: true, minLength: 5, maxLength: 50 });
  const { setAccessToken, setRefreshToken } = useContext(AuthContext);
  const [redirect, setRedirect] = useState(false);
  const [formIsFullValid, setFormIsFullValid] = useState(false);

  const [modalVisible, setModalVisible] = useState(false);
  const [modalMessage, setModalMessage] = useState("");

  const [spinner, setSpinner] = useState(false);

  useEffect(() => {
    setFormIsFullValid(username.isFullValid() && password.isFullValid());
  }, [username, password]);

  async function performLogin() {
    setModalMessage("");
    setModalVisible(false);
    setSpinner(true);
    setRedirect(false);
    try {
      const url = "http://localhost:8081/api/auth/login";
      const loginDto = { username: username.value, password: password.value };
      const response = await loginFetch(url, loginDto);
      if (!response.ok) {
        setModalMessage("Not correct username or password!");
        setModalVisible(true);

        return;
      }
      const json = await response.json();
      setAccessToken(json.accessToken);
      setRefreshToken(json.refreshToken);
      setRedirect(true);
    } catch (e) {
      setModalMessage("No access to server:( May be internet connection lost.");
      setModalVisible(true);
    } finally {
      setSpinner(false);
    }
  }

  if (redirect) {
    return <Navigate to={"/"} />;
  }

  return (
    <div className="login-form-wrapper">
      <div className="login-form">
        {spinner && <Spinner />}
        <Modal visible={modalVisible} onClose={() => setModalVisible(false)}>
          {modalMessage}
        </Modal>
        <div className="large-font-text">Login to your account</div>
        <div>
          <CustomFormInput
            id={"username-register-input"}
            label={"Username"}
            onChange={username.onChange}
            onBlur={username.onBlur}
            placeholder={"username"}
            value={username.value}
          />
          {username.isDirty && username.isEmpty && (
            <div style={{ color: "red" }}>Username must not be empty</div>
          )}
          {username.isDirty &&
            (username.minLengthError || username.maxLengthError) && (
              <div style={{ color: "red" }}>
                Username size should be from 3 to 50
              </div>
            )}
          <CustomFormInput
            id={"password-register-input"}
            label={"Password"}
            onChange={password.onChange}
            onBlur={password.onBlur}
            placeholder={"password"}
            value={password.value}
            type={"password"}
          />
          {password.isDirty && password.isEmpty && (
            <div style={{ color: "red" }}>Password must not be empty</div>
          )}
          {password.isDirty &&
            (password.minLengthError || password.maxLengthError) && (
              <div style={{ color: "red" }}>
                Paasword size should be from 5 to 50
              </div>
            )}
        </div>
        <div>
          <button
            className={"btn btn-outline-success btn-lg login-button"}
            onClick={performLogin}
            disabled={!formIsFullValid}
          >
            {formIsFullValid ? "Login" : "Input correct data"}
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
