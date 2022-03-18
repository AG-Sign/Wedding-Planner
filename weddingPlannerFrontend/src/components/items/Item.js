import classes from "./Item.module.css";
import Card from "../UI/Card";
import { useContext } from "react";
import Date from "./Date";

import AuthContext from "../context/auth-context";

function ExpenseItem(props) {

  const change = (data) =>{
    props.func(data)
  }
  const ctx = useContext(AuthContext);
  let guests
  let hasRoom
  if(props.type==="hall"){
    let arr = props.desc.split("*")
    guests = arr[0]
    if(arr[1]==="true"){
      hasRoom = "Our wedding has a room with it's Service"
    }else{
      hasRoom = "Our wedding won't be able to offer a room with it's Service"
    }
  }
  return (
      <Card className={classes.item} >
        <div className={classes["item__description"]} >
          <img
            className={classes.img}
            src={props.img}
            alt={props.alt}
            width="200px"
            height="auto"
          />
          {ctx.type === "Customer" && <p>Provider : {props.name}</p>}
          {props.type==="hall" && <div><p>Number of total Guests to attend : {guests}</p> <br></br> <p>Room Service : {hasRoom}</p></div>}
          {props.type==="limousine" && <p>The type of our offered Car : {props.desc}</p>}
          {props.type==="stylist" && <p>Description : {props.desc}</p>}
          <div>
            <Date date={props.timeSlot} id={props.id} type={props.type} func ={change}/>
          </div>
          <div className={classes["item__price"]}>${props.price}</div>
          
        </div>
      </Card>
  );
}

export default ExpenseItem;
