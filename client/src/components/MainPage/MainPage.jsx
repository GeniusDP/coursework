import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../../App";
import { parseJwt } from "../../jwtUtil";

const MainPage = () => {
  const { getToken } = useContext(AuthContext);
  let username = null;
  try {
    username = parseJwt(getToken());
  } catch (e) {
    username = null;
  }

  return (
    <div>
      <div>Main Page</div>
      <div>
        <Link to={"/login"}>Login</Link>
      </div>
      <div>
        <Link to={"/register"}>Register</Link>
      </div>
      <div>
        <Link to={"/account-settings"}>{username}</Link>
      </div>

    </div>
  );
};

export default MainPage;
