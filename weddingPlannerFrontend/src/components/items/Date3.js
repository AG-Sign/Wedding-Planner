import * as React from "react";
import { styled } from "@mui/material/styles";
import Box from "@mui/material/Box";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import IconButton from "@mui/material/IconButton";
import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";
import DeleteIcon from "@mui/icons-material/Delete";
import AuthContext from "../context/auth-context";
import { useState, useContext } from "react";
import ErrorModal from "../Modal/ErrorModal";


const Demo = styled("div")(({ theme }) => ({
  backgroundColor: theme.palette.background.paper,
}));

const Date = (props) => {
  const [dense, setDense] = React.useState(false);
  const [error, setError] = useState(false);
  const [errorModal, setErrorModal] = useState();
  const [value, setValue] = useState("");
//   const [valueFrom, setValueFrom] = React.useState("");
//   const [valueTo, setValueTo] = React.useState("");

  var valueFrom
  var valueTo


  const ctx = useContext(AuthContext);

  const errorHandler = () => {
    setErrorModal(null);
  };



  function deleteTimeSlot (ex){
    let url;
    url = "http://localhost:8080/api/time_slots/";
    fetch(url, {
      method: "DELETE",
      body: JSON.stringify({
        id: ex.id,
        activityId: props.id,
        activityType: props.type,
        startTime: ex.startTime,
        endTime: ex.endTime,
      }),
      headers: {
        "Content-Type": "application/json",
        Authorization: `${ctx.token}`,
      },
    })
      .then(async (res) => {
        if (res.status >= 400) {
          const data = await res.json();
          throw new Error(data.message);
        }
        return res.json();
      })
      .catch((err) => {
        setErrorModal({
          title: "Invalid Request",
          message: err.message,
        });
      });
  };

  return (
    <Box sx={{ flexGrow: 1, maxWidth: 752 }}>
        <div>
          {errorModal && (
            <ErrorModal 
              title={errorModal.title}
              message={errorModal.message}
              onConfirm={errorHandler}
            />
          )}
          </div>
      <Grid item xs={12} md={6}>
        <Typography
          sx={{
            mt: 4,
            mb: 2,
            fontFamily: "'Questrial', sans-serif",
            color: "#40005d",
          }}
          variant="h6"
          component="div"
        >
          Time Slots{" "}
          <link
            href="https://fonts.googleapis.com/css2?family=Questrial&display=swap"
            rel="stylesheet"
          ></link>
        </Typography>
        <Demo>
          <List dense={dense}>
            {props.date.map((ex) => (
                
              <div key={ex.id}>
                <ListItem 
                key={value}
                  secondaryAction={
                    <IconButton aria-label="delete" onClick={()=>deleteTimeSlot(ex)} >
                      <DeleteIcon />
                    </IconButton>
                  }
                >
                  <ListItemText
                    value={ex.id}
                    primary={`${ex.startTime} : ${ex.endTime}`}
                  />
                </ListItem>
              </div>
            ))}
          </List>
        </Demo>
      </Grid>
    </Box>
  );
};
export default Date;