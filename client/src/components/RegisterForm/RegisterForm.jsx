import React, { useEffect, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import "./register-form-styles.css";
import "./../component-styles.css";
import customFetch from "../../customFetch";
import CustomFormInput from "../CustomFormInput.jsx/CustomFormInput";
import Modal from "../Modal/Modal";
import { Spinner } from "../Spinner/Spinner";
import useInput from "../../hooks/useInput";

const RegisterForm = () => {
  const username = useInput("", { isEmpty: true, minLength: 3, maxLength: 50 });
  const password = useInput("", { isEmpty: true, minLength: 5, maxLength: 50 });
  const email = useInput("", {
    isEmpty: true,
    maxLength: 50,
    isValidEmail: true,
  });
  const firstName = useInput("", {
    isEmpty: true,
    minLength: 5,
    maxLength: 50,
  });
  const lastName = useInput("", { isEmpty: true, minLength: 5, maxLength: 50 });

  const [formIsFullValid, setFormIsFullValid] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [modalMessage, setModalMessage] = useState("");

  const [redirect, setRedirect] = useState(false);

  const [spinner, setSpinner] = useState(false);

  useEffect(() => {
    setFormIsFullValid(
      username.isFullValid() &&
        password.isFullValid() &&
        email.isFullValid() &&
        firstName.isFullValid() &&
        lastName.isFullValid()
    );
  }, [username, password, email, firstName, lastName]);

  async function performRegister() {
    setModalMessage("");
    setModalVisible(false);
    setSpinner(true);
    const body = {
      username,
      password,
      email,
      firstName,
      lastName,
    };

    try {
      const response = await customFetch(
        "http://localhost:8081/api/auth/register",
        body
      );
      if (response.ok) {
        setRedirect(true);
      } else {
        setModalMessage("Registration failed.");
        setModalVisible(true);
      }
    } catch (error) {
      setModalMessage("No access to server:( May be internet connection lost.");
      setModalVisible(true);
    } finally {
      setSpinner(false);
    }
  }

  if (redirect) {
    return <Navigate to={"/login"} />;
  }

  return (
    <div className="register-form-wrapper">
      {spinner && <Spinner />}
      <Modal visible={modalVisible} onClose={() => setModalVisible(false)}>
        {modalMessage}
      </Modal>
      <div className="register-form">
        <div className="large-font-text">Registration</div>
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
            id={"email-register-input"}
            label={"Email"}
            onChange={email.onChange}
            onBlur={email.onBlur}
            placeholder={"email"}
            value={email.value}
          />
          {email.isDirty && email.isEmpty && (
            <div style={{ color: "red" }}>Email must not be empty</div>
          )}
          {email.isDirty && email.emailError && (
            <div style={{ color: "red" }}>Value must be valid email</div>
          )}
          {email.isDirty && email.maxLengthError && (
            <div style={{ color: "red" }}>
              Email size should not be bigger than 50
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
        <div className="middle-font-text">Your first and last names:</div>
        <div>
          <CustomFormInput
            id={"first-name-register-input"}
            label={"First name"}
            onChange={firstName.onChange}
            placeholder={"first name"}
            value={firstName.value}
          />
          {firstName.isDirty && firstName.isEmpty && (
            <div style={{ color: "red" }}>First name must not be empty</div>
          )}
          {firstName.isDirty &&
            (firstName.minLengthError || firstName.maxLengthError) && (
              <div style={{ color: "red" }}>
                First name size should be from 5 to 50
              </div>
            )}

          <CustomFormInput
            id={"last-name-register-input"}
            label={"Last name"}
            onChange={lastName.onChange}
            placeholder={"last name"}
            value={lastName.value}
          />
          {lastName.isDirty && lastName.isEmpty && (
            <div style={{ color: "red" }}>Last name must not be empty</div>
          )}
          {lastName.isDirty &&
            (lastName.minLengthError || lastName.maxLengthError) && (
              <div style={{ color: "red" }}>
                Last name size should be from 5 to 50
              </div>
            )}
        </div>
        <div>
          <button
            className={"btn btn-outline-success btn-lg login-button"}
            onClick={performRegister}
            disabled={!formIsFullValid}
          >
            {formIsFullValid ? "Register now!" : "Input correct data"}
          </button>
        </div>
        <div>
          <Link to="/login">Already have an account? Login now!</Link>
        </div>
      </div>
    </div>
  );
};

export default RegisterForm;
