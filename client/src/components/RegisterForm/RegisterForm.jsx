import React, { useState } from "react";
import { Link, Navigate } from "react-router-dom";
import "./register-form-styles.css";
import "./../component-styles.css";
import customFetch from "../../customFetch";
import CustomFormInput from "../CustomFormInput.jsx/CustomFormInput";

const RegisterForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");

  const [registrationError, setRegistrationError] = useState(false);
  const [redirect, setRedirect] = useState(false);
  const [networkError, setNetworkError] = useState(false);

  async function performRegister() {
    setNetworkError(false);
    setRegistrationError(false);

    const body = {
      username,
      password,
      email,
      firstName,
      lastName,
    };

    try {
      const response = await customFetch(
        "http://localhost:8081/register",
        body
      );
      if (response.ok) {
        setRegistrationError(false);
        setRedirect(true);
      } else {
        setRegistrationError(true);
      }
    } catch (error) {
      console.log(error);
      setNetworkError(true);
    }
  }

  if (networkError) {
    alert("Problem occured! May be internet connection lost.");
  }

  if (registrationError) {
    alert("User with such username already exists:(");
  }

  if (redirect) {
    return <Navigate to={"/login"} />;
  }

  return (
    <div className="register-form-wrapper">
      <div className="register-form">
        <div className="large-font-text">Registration</div>
        <div>
          <CustomFormInput
            id={"username-register-input"}
            label={"Username"}
            onChange={(event) => setUsername(event.target.value)}
            placeholder={"username"}
            value={username}
          />{" "}
          <CustomFormInput
            id={"email-register-input"}
            label={"Email"}
            onChange={(event) => setEmail(event.target.value)}
            placeholder={"email"}
            value={email}
          />
          <CustomFormInput
            id={"password-register-input"}
            label={"Password"}
            onChange={(event) => setPassword(event.target.value)}
            placeholder={"password"}
            value={password}
          />
        </div>
        <div className="middle-font-text">Your first and last names:</div>
        <div>
          <CustomFormInput
            id={"first-name-register-input"}
            label={"First name"}
            onChange={(event) => setFirstName(event.target.value)}
            placeholder={"first name"}
            value={firstName}
          />
          <CustomFormInput
            id={"last-name-register-input"}
            label={"Last name"}
            onChange={(event) => setLastName(event.target.value)}
            placeholder={"last name"}
            value={lastName}
          />
        </div>
        <div>
          <button
            className={"btn btn-outline-success btn-lg login-button"}
            onClick={performRegister}
          >
            Register
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
