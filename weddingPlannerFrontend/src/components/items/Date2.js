import classes from "./Date.module.css";
import Radio from "@mui/material/Radio";
import FormControlLabel from "@mui/material/FormControlLabel";
import FormLabel from "@mui/material/FormLabel";
import { useState, useContext } from "react";
import AuthContext from "../context/auth-context";
import Alert from "@mui/material/Alert";
import { cyan } from "@mui/material/colors";
import { Button } from "@mui/material";
import React from "react";
import {useHistory} from "react-router-dom";
import ErrorModal from "../Modal/ErrorModal";


const Date2 = (props) => {
  /*const month=props.date.toLocaleString('en-US',{month :'long'})
    const day=props.date.toLocaleString('en-US',{day :'2-digit'})
    const year=props.date.getFullYear()*/
  //var hours = props.date.getHours()
  /*var hours = props.date.timeFrom;
  const ampm = hours >= 12 ? "pm" : "am";
  hours = hours % 12;
  hours = hours ? hours : 12;

  var hours2 = props.date.timeTo;
  const ampm2 = hours2 >= 12 ? "pm" : "am";
  hours2 = hours2 % 12;
  hours2 = hours2 ? hours2 : 12;*/
  const [value, setValue] = useState("");
  const [helperText, setHelperText] = useState("");
  const [error, setError] = useState(false);
  const [errorModal, setErrorModal] = useState();


  const ctx = useContext(AuthContext);
  const history = useHistory();

  const radioHandler = (event) => {
    setValue(event.target.value);
    setHelperText("");
    setError(false);
  };
  const submit = (event) => {
    event.preventDefault();
    if (value === "") {
      setHelperText("Please Choose a Time slot");
      setError(true);
      return;
    }
    console.log("hello  ", event.target.value, " ", value);
    let arr =event.target.value.split("-")
    let activity_id = arr[0]
    let type = arr[1]
    let url
    if(type==="hall"){
        url ="http://localhost:8080/api/reservations/reserve_weddingHall/"+activity_id+"/"+value
    }else if(type==="stylist"){
        url ="http://localhost:8080/api/reservations/reserve_stylist/"+activity_id+"/"+value
    }else if(type==="limousine"){
        url ="http://localhost:8080/api/reservations/reserve_limousine/"+activity_id+"/"+value
    }
    fetch(url, {
      method: "POST",
      body:{
        activityId : activity_id,
        timeSlotId : value
      },
      headers: {
        "Content-Type": "application/json",
        "Authorization": `${ctx.token}`
      }
    })
      .then(async (res) => {
        let k = res.status
        console.log(k)
        if(k >= 400) {
          console.log('1')
          const data = await res.json();
          throw new Error(data.message);
      }
    
      })
      .then((res) => {
        history.replace("/profile");
      })
      .catch((err) => {
        console.log('2')
        setErrorModal({
          title: "Invalid Request",
          message: err.message,
        });
      });
    
  };
  const errorHandler = () => {
    setErrorModal(null);
  };

  return (
    <React.Fragment>
      {errorModal && (
        <ErrorModal
          title={errorModal.title}
          message={errorModal.message}
          onConfirm={errorHandler}
        />
      )}
      <div className={classes["date"]}>
        <FormLabel
          component="legend"
          sx={{
            color: cyan[800],
            "&.Mui-checked": {
              color: cyan[600],
            },
          }}
        >
          From : To
        </FormLabel>

        <div className={classes["date__hour"]}>
          {props.date.map((ex) => (
            <div key={ex.id}>
                <FormControlLabel
                  value={ex.id}
                  control={
                    <Radio
                      checked={value === `${ex.id}`}
                      onChange={radioHandler}
                      value={`${ex.id}`}
                      name="radio-buttons"
                      sx={{
                        color: cyan[800],
                        "&.Mui-checked": {
                          color: cyan[600],
                        },
                      }}
                    />
                  }
                  label={`${ex.startTime} : ${ex.endTime}`}
                />
            </div>
          ))}
        </div>
      </div>
      {error && (
        <Alert severity="error" variant="outlined" sx={{ color: "#ff1744" , width:"fit-content" }}>
          {helperText}
        </Alert>
      )}
        <Button value={`${props.id}-${props.type}`} onClick={submit} style={{ float: "right" }}>
          Confirm
        </Button>
    </React.Fragment>
  );
};

export default Date2;
