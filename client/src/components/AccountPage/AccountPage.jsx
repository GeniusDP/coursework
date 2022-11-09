import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../../App";
import { parseJwt } from "../../jwtUtil";

const AccountPage = () => {
  const { getToken } = useContext(AuthContext);
  const username = parseJwt(getToken());
  return (
    <div>
      <div>Account page</div>
      <div>{username}</div>
      <div>
        {" "}
        <Link to={"/"}>To main</Link>
      </div>
    </div>
  );
};

export default AccountPage;
