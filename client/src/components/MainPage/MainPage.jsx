import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../../App";
import { parseJwt } from "../../jwtUtil";

const MainPage = () => {
  const { getAccessToken } = useContext(AuthContext);
  const jwtPayload = parseJwt(getAccessToken());
  const username = jwtPayload?.sub;

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
      <div>
        <Link to={"/forbidden"}>Forbidden(only for Admin)</Link>
      </div>

    </div>
  );
};

export default MainPage;
