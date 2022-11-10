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
        <div  className="large-font-text">Registration</div>
        <div>
          <input
            className={"form-control custom-input"}
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            placeholder={"username"}
          />
          <input
            className={"form-control custom-input"}
            value={email}
            placeholder={"email"}
            onChange={(event) => setEmail(event.target.value)}
          />
          <input
            className={"form-control custom-input"}
            type={"password"}
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            placeholder={"password"}
          />
        </div>
        <div className="middle-font-text">Your first and last names:</div>
        <div>
          <input
            className={"form-control custom-input"}
            value={firstName}
            onChange={(event) => setFirstName(event.target.value)}
            placeholder={"firstName"}
          />
          <input
            className={"form-control custom-input"}
            value={lastName}
            onChange={(event) => setLastName(event.target.value)}
            placeholder={"lastName"}
          />
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
