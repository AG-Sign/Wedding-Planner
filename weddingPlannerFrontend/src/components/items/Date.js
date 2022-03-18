import classes from "./Date.module.css";
import FormLabel from "@mui/material/FormLabel";
import { cyan } from "@mui/material/colors";
import React,{ useContext ,useState} from "react";
import AuthContext from "../context/auth-context";
import { Button } from "@mui/material";
import ErrorModal from "../Modal/ErrorModal";


const Date = (props) => {
  const ctx = useContext(AuthContext);
  const [errorModal, setErrorModal] = useState();

  const errorHandler = () => {
    setErrorModal(null);
  };
  const submit = (event) => {
    let arr =event.target.value.split("-")
    let activity_id = arr[0]
    let type = arr[1]
    
    let url
    if(type==="hall"){
        url ="http://localhost:8080/api/reservations/delete_wedding_hall_reservation/"+activity_id
    }else if(type==="stylist"){
        url ="http://localhost:8080/api/reservations/delete_stylist_reservation/"+activity_id
    }else if(type==="limousine"){
        url ="http://localhost:8080/api/reservations/delete_limousine_reservation/"+activity_id
    }
    fetch(url, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `${ctx.token}`
      }
    })
      .then(async (res) => {
        let k = res.status
        if(k >= 400) {
          const data = await res.json();
          throw new Error(data.message);
      }
    
      })
      .then((res) => {
        props.func({type:type,id:activity_id})
      })
      .catch((err) => {
        setErrorModal({
          title: "Invalid Request",
          message: err.message,
        });
      });
    
  };
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
                <div>{`${ex.startTime} : ${ex.endTime}`}</div>
            </div>
          ))}
        </div>
      </div>
      <Button value={`${props.id}-${props.type}`} onClick={submit} style={{ float: "right" }}>
          Cancel Reservation
        </Button>
    </React.Fragment>
  );
};

export default Date;
