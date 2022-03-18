import { useContext } from "react";
import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { useHistory } from "react-router-dom";
import AuthContext from "../context/auth-context";
import LogoutIcon from '@mui/icons-material/Logout';
import classes from "./MainHeader.module.css";

const MainHeader = (props) => {
  const history = useHistory();
  const ctx = useContext(AuthContext);
  const logoutHandler = () => {
    let url;
    url = "http://localhost:8080/api/users/logout";
    fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${ctx.token}`

      },
    })
      .then(async (res) => {
        return res.json();
      })
      .then((data) => {
        ctx.logout();
        history.replace("/auth");
      });
  };
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" >
        <Toolbar className={classes.header}>
          <Typography
            variant="h6"
            component="div"
            sx={{ flexGrow: 1 , fontSize : "2.5rem" , margin:0,fontFamily: 'Pushster'  }}
          >
            Wedding Planner
            <link href="https://fonts.googleapis.com/css2?family=Pushster&display=swap" rel="stylesheet"></link>
          </Typography>
          {ctx.isLoggedIn && (
            <Typography
              variant="h6"
              sx={{ flexGrow: 50 , fontSize :"0.85rem" }}
            >
              { ctx.type }
            </Typography>
          )}
          
          {ctx.isLoggedIn && (
            <Button variant="outlined" startIcon={<LogoutIcon />}onClick={logoutHandler} color="inherit" >
              Logout
            </Button>
          )}
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default MainHeader;
