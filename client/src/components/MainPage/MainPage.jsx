import React, {useContext} from "react";
import {AuthContext} from "../../App";
import {parseJwt} from "../../jwtUtil";
import {
  Typography
} from "@material-ui/core";
import {Container} from "@mui/material";
import java from './java.png';
import {makeStyles} from "@material-ui/core/styles";
import CustomAppBar from "../CustomAppBar/CustomAppBar"; // with import


const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  text: {
    flexGrow: 1,
    paddingTop: 50
  },
}));

const MainPage = () => {
  const {getAccessToken} = useContext(AuthContext);
  const jwtPayload = parseJwt(getAccessToken());

  const classes = useStyles();
  return (
      <div className={classes.root}>
        <CustomAppBar/>
        <Container>
          <Typography variant="h6" className={classes.text}>
            Hello, this is Java apps testing system! Here you have got an
            opportunity to create tasks and solve them. Happy hacking:)
          </Typography>
        </Container>
        <div style={{display: "flex", alignItems: "center", justifyContent: "center"}}>
          <img src={java} width={500}/>
        </div>
      </div>
  );
};

export default MainPage;
