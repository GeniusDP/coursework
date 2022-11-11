import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../../App";
import { parseJwt } from "../../jwtUtil";

const AccountPage = () => {
  const { getAccessToken } = useContext(AuthContext);
  const tokenPayload = parseJwt(getAccessToken());
  const username = tokenPayload.sub;
  const role = tokenPayload.role;
  return (
    <div>
      <div>Account page</div>
      <div>Username = {username}</div>
      <div>Role = {role}</div>
      <div>
        {" "}
        <Link to={"/"}>To main</Link>
      </div>
    </div>
  );
};

export default AccountPage;
