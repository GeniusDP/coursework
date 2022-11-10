import React, { useState } from "react";
import { Link, Navigate } from "react-router-dom";
import "./register-form-styles.css";
import "./../component-styles.css";

const RegisterForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");

  const [isRegistrationError, setIsRegistrationError] = useState(false);
  const [redirect, setRedirect] = useState(false);

  function performLogin() {
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
      username,
      password,
      email,
      firstName,
      lastName,
    });

    const requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };

    function handleFetchError(request) {
      if (!request.ok) {
        throw new Error("status code is " + request.status);
      }
      return request;
    }

    fetch("http://localhost:8081/register", requestOptions)
      .then(handleFetchError)
      .then((request) => {
        setIsRegistrationError(false);
        setRedirect(true);
      })
      .catch((error) => {
        console.log(error);
        setIsRegistrationError(true);
      });
  }

  if (isRegistrationError) {
    alert("Some problem occured! May be internet connetction lost.");
  }
  if (redirect) {
    return <Navigate to={"/login"} />;
  }

  return (
    <div className="register-form-wrapper">
      <div className="register-form">
        <div className="large-font-text">Registration</div>
        <div>
          <div className="custom-input">
            <div className="label-wrapper">
              <label htmlFor="username-input">Username</label>
            </div>
            <input
              id="username-input"
              className={"form-control"}
              value={username}
              onChange={(event) => setUsername(event.target.value)}
              placeholder={"username"}
            />
          </div>
          <div className="custom-input">
            <div className="label-wrapper">
              <label htmlFor="email-input">Email</label>
            </div>
            <input
              id="email-input"
              className={"form-control"}
              value={email}
              type={"email"}
              placeholder={"email"}
              onChange={(event) => setEmail(event.target.value)}
            />
          </div>
          <div className="custom-input">
            <div className="label-wrapper">
              <label htmlFor="password-input">Password</label>
            </div>
            <input
              id="password-input"
              className={"form-control"}
              type={"password"}
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder={"password"}
            />
          </div>
        </div>
        <div className="middle-font-text">Your first and last names:</div>
        <div>
          <div className="custom-input">
            <div className="label-wrapper">
              <label htmlFor="first-name-input">First name</label>
            </div>
            <input
              id="first-name-input"
              className={"form-control"}
              value={firstName}
              onChange={(event) => setFirstName(event.target.value)}
              placeholder={"firstName"}
            />
          </div>
          <div className="custom-input">
            <div className="label-wrapper">
              <label htmlFor="last-name-input">Last name</label>
            </div>
            <input
              id="last-name-input"
              className={"form-control"}
              value={lastName}
              onChange={(event) => setLastName(event.target.value)}
              placeholder={"lastName"}
            />
          </div>
        </div>
        <div>
          <button
            className={"btn btn-outline-success btn-lg login-button"}
            onClick={performLogin}
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
