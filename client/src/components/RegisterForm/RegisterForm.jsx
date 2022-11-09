import React, { useContext, useState } from "react";
import { Navigate } from "react-router-dom";

const RegisterForm = () => {
  const [username, setUsername] = useState("usr");
  const [password, setPassword] = useState("123");
  const [email, setEmail] = useState("zaranik@gmail.com");
  const [firstName, setFirstName] = useState("bogdan");
  const [lastName, setLastName] = useState("zaranik");

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
    alert('Some problem occured! May be internet connetction lost.');
  }
  if (redirect) {
    return <Navigate to={"/login"} />;
  }

  return (
    <div>
      <input
        value={username}
        onChange={(event) => setUsername(event.target.value)}
        placeholder={"username"}
      />
      <input
        value={email}
        placeholder={"email"}
        onChange={(event) => setEmail(event.target.value)}
      />
      <input
        type={"password"}
        value={password}
        onChange={(event) => setPassword(event.target.value)}
        placeholder={"password"}
      />
      <input
        value={firstName}
        onChange={(event) => setFirstName(event.target.value)}
        placeholder={"firstName"}
      />
      <input
        value={lastName}
        onChange={(event) => setLastName(event.target.value)}
        placeholder={"lastName"}
      />
      <button onClick={performLogin}>Register</button>
    </div>
  );
};

export default RegisterForm;
