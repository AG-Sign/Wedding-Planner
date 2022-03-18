import React from "react";

import Card from "../UI/Card";
import classes from "./ErrorModal.module.css";
import TextField from "@mui/material/TextField";
import AdapterDateFns from "@mui/lab/AdapterDateFns";
import LocalizationProvider from "@mui/lab/LocalizationProvider";
import StaticDatePicker from '@mui/lab/StaticDatePicker';

const ModalDate = (props) => {
  const [value, setValue] = React.useState(null);
  const dateHandler = () =>{
      props.onConfirm(value)
  }
  return (
    <div>
      <div className={classes.backdrop} onClick={props.onClose} />
      <Card className={classes.modal}>
        <header className={classes.header}>
          <h2>Main Reservation Creation</h2>
        </header>
        <div className={classes.content}>
          <p>Choose a date for your Main Reservation</p>
          <LocalizationProvider dateAdapter={AdapterDateFns}>
            <StaticDatePicker
              orientation="landscape"
              openTo="day"
              value={value}
              onChange={(newValue) => {
                setValue(newValue);
              }}
              renderInput={(params) => <TextField {...params} />}
            />
          </LocalizationProvider>
        </div>
        <footer className={classes.actions}>
          <button onClick={dateHandler}>Okay</button>
        </footer>
      </Card>
    </div>
  );
};

export default ModalDate;
