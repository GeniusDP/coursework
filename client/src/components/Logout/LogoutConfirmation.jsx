import React, { useContext, useState } from 'react';
import { Link, Navigate } from 'react-router-dom';
import { AuthContext } from '../../App';
import customFetch from '../../customFetch';
import CustomAppBar from "../CustomAppBar/CustomAppBar";
import {Box, Button} from "@mui/material";
import authRequestWrapper from "../../utils/authRequestWrapper.js";

const LogoutConfirmation = () => {
  const [loggedOut, setLoggedOut] = useState(false);
  const {
    getAccessToken,
    getRefreshToken,
    removeTokens,
  } = useContext(AuthContext);

  const logout = async () => {
    await authRequestWrapper(async () => {
      const url = process.env.REACT_APP_AUTH_URL + "/logout";

      const body = {
        accessToken: getAccessToken(),
        refreshToken: getRefreshToken(),
      };
      try {
        const response = await customFetch(url, body);
        if(!response.ok){
          alert(await response.json());
        }
      } catch(e){
        alert(e);
      }
      removeTokens();
      setLoggedOut(true);
    });
  }
  if(loggedOut){
    return <Navigate to={"/"}/>
  }
  return(
    <div>
      <CustomAppBar/>
      <Box textAlign="center">
        <Button
            variant={"contained"}
            onClick={logout}
            style={{marginTop:300, backgroundColor: "tomato", width: 300, height: 110, fontSize: 25, borderRadius: 50}}
        >
          Logout
        </Button>
      </Box>
    </div>
  );
};

export default LogoutConfirmation;