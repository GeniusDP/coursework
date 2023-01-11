import { Button, Container, Typography } from "@material-ui/core";
import React, { useContext, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import { AuthContext } from "../../App";
import { parseJwt } from "../../jwtUtil";
import CustomAppBar from "../CustomAppBar/CustomAppBar";
import userIcon from './computer-user-icon.png';
import authRequestWrapper from "../../utils/authRequestWrapper.js";

const AccountPage = () => {
  const { getAccessToken } = useContext(AuthContext);
  const tokenPayload = parseJwt(getAccessToken());
  const username = tokenPayload.sub;
  const role = tokenPayload.role;

  const [changePassword, setChangePassword] = useState(false);
  const [unauthorized, setUnauthorized] = useState(false);

  async function becomeTeacherHandler() {
    const result = await authRequestWrapper(async () => {
      const options = {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${getAccessToken()}`
        }
      };
      const url = process.env.REACT_APP_USER_MANAGEMENT_URL + `/requests-for-teacher-grant/request`;
      const response = await fetch(url, options);
      try {
        if (response.ok) {
          window.alert("Request was successfully sent to admin");
        } else if(response.status === 429) {
          const body = await response.json();
          const timeLeftToWaitInMillis = body.retryAfterSeconds * 1000;
          const dateNow = new Date().getTime() ;
          const nextAvailableDate = new Date(dateNow + timeLeftToWaitInMillis);
          window.alert(`Next time you wii be able to send request at ${nextAvailableDate.toString()}`)
        }
      } catch (e) {
        window.alert("Some error occured.\nOperation did not finish successfully.");
      }
    });
    if(!result){
      setUnauthorized(true);
    }
  }
  
  if (changePassword) {
    return <Navigate to={"/change-password"} replace/>;
  }

  if (unauthorized) {
    return <Navigate to={"/login"} replace/>;
  }

  return (
    <>
      <CustomAppBar />
      <Container>
        <div style={{ display: "flex", flexDirection: "row", alignItems: "center", justifyContent: "center" }}>
          <img src={userIcon} style={{ marginTop: "100px" }} width={200} alt={"user fake icon"} />
          <div style={{ width: "80%", textAlign: "center" }}>
            <Typography variant="h3" style={{ width: "80%", textAlign: "center" }}>
              Username: {username}
            </Typography>
            <Typography variant="h5" style={{ width: "80%", textAlign: "center" }}>
              Role: {role}
            </Typography>
          </div>
          {role === "STUDENT" && <Button color={"primary"} variant={"contained"} onClick={becomeTeacherHandler}>Request teacher grants</Button>}
        </div>

      </Container>
    </>
  );
};
export default AccountPage;
