import React, {useContext} from 'react';
import {
  AppBar, Button,
  ButtonGroup,
  IconButton,
  Toolbar,
  Typography
} from "@material-ui/core";
import {Link} from "react-router-dom";
import MenuIcon from "@mui/icons-material/Menu";
import {AccountCircle} from "@mui/icons-material";
import {makeStyles} from "@material-ui/core/styles";
import {AuthContext} from "../../App";
import {parseJwt} from "../../jwtUtil";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(3),
  },
  title: {
    flexGrow: 1,
  },
  text: {
    flexGrow: 1,
    paddingTop: 50
  },
}));

const CustomAppBar = () => {
  const {getAccessToken} = useContext(AuthContext);
  const jwtPayload = parseJwt(getAccessToken());
  const username = jwtPayload?.sub;
  const role = jwtPayload?.role;

  const classes = useStyles();
  return (
      <div className={classes.root}>
        <AppBar position="static" color="secondary">
          <Toolbar>
            <IconButton
                edge="start"
                className={classes.menuButton}
                color="inherit"
                aria-label="menu"
                component={Link}
                to="/"
            >
              <MenuIcon/>
            </IconButton>
            <Typography variant="h6" className={classes.title}>
              Main menu
            </Typography>
            <ButtonGroup>
              <Button color="inherit" component={Link} to="/all-tasks">
                All tasks
              </Button>
              {username && (role === 'TEACHER') &&
                  <Button color="inherit" component={Link} to="/create-task">
                    Create new task
                  </Button>
              }
              {username && (role === 'ADMIN') &&
                  <Button color="inherit" component={Link} to="/admin-tool">
                    Admin Tool
                  </Button>
              }

              {!username && <Button color="inherit" component={Link}
                                    to="/register">Register</Button>}
              {username && <Button color="inherit" component={Link}
                                   to="/logout">Logout</Button>}
              {!username && <Button color="inherit" component={Link}
                                    to="/login">Login</Button>}
            </ButtonGroup>
            {username && <IconButton
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                color="inherit"
                component={Link}
                to="/account"
            >
              <div style={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center"
              }}>
                <AccountCircle/>
                <Typography>{username}</Typography>
              </div>
            </IconButton>}
          </Toolbar>
        </AppBar>
      </div>
  );
};

export default CustomAppBar;