import * as React from "react";
import { useState, useContext } from "react";
import classes from "./Item.module.css";
import Card from "../UI/Card";
import AddIcon from "@mui/icons-material/Add";
import Button from "@mui/material/Button";
import AuthContext from "../context/auth-context";
import TextField from "@mui/material/TextField";
import AdapterDateFns from "@mui/lab/AdapterDateFns";
import LocalizationProvider from "@mui/lab/LocalizationProvider";
import TimePicker from "@mui/lab/TimePicker";
import Stack from "@mui/material/Stack";
import classes2 from "../Modal/ErrorModal.module.css";
import Date3 from "./Date3";
import ErrorModal from "../Modal/ErrorModal";
import classes3 from "../profile/providerProfile.module.css";
import Checkbox from "@mui/material/Checkbox";
import FormControlLabel from "@mui/material/FormControlLabel";
import Toolbar from "@mui/material/Toolbar";
import AppBar from "@mui/material/AppBar";
import Typography from "@mui/material/Typography";
import Dialog from "@mui/material/Dialog";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import Slide from "@mui/material/Slide";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

function Item(props) {
  const [errorModal, setErrorModal] = useState();

  const ctx = useContext(AuthContext);
  let guests;
  let hasRoom;

  if (props.type === "WeddingHall") {
    let arr = props.desc.split("*");
    guests = arr[0];
    if (arr[1] === "true") {
      hasRoom = "Our wedding has a room with it's Service";
    } else {
      hasRoom = "Our wedding won't be able to offer a room with it's Service";
    }
  }

  const [openDialog, setOpenDialog] = React.useState(false);
  const [open, setOpen] = React.useState(false);

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };
  const handleClose = (event) => {
    setOpen(false);
  };

  const [openPicker, setOpenPicker] = React.useState(false);

  const handleOpenPicker = () => {
    setOpenPicker(true);
  };
  const handleClosePicker = () => {
    setOpenPicker(false);
  };
  const [valueFrom, setValueFrom] = React.useState("");
  const [valueTo, setValueTo] = React.useState("");

  var timeFrom = new Date(valueFrom);
  var timeTo = new Date(valueTo);

  var hoursFrom = timeFrom.getHours().toString();
  hoursFrom = hoursFrom < 10 ? "0" + hoursFrom : hoursFrom;
  var minsFrom = timeFrom.getMinutes().toString();
  minsFrom = minsFrom < 10 ? "0" + minsFrom : minsFrom;
  var hoursTo = timeTo.getHours().toString();
  hoursTo = hoursTo < 10 ? "0" + hoursTo : hoursTo;
  var minsTo = timeTo.getMinutes().toString();
  minsTo = minsTo < 10 ? "0" + minsTo : minsTo;

  const from = hoursFrom + ":" + minsFrom;
  const to = hoursTo + ":" + minsTo;

  const errorHandler = () => {
    setErrorModal(null);
  };


  let text;
  if (props.type === "WeddingHall") text = "Add Wedding Hall Service";
  else if (props.type === "Stylist") text = "Add Stylist Service";
  else if (props.type === "Limousine") text = "Add Limousine Service";

  const [price, setPrice] = useState(props.price);
  const [img, setImg] = useState(props.img);
  const [carType, setType] = useState(props.desc);
  const [desc, setDesc] = useState(props.desc);
  const [noGuests, setGuests] = useState(props.guests);

  //has room check box
  const [checked, setChecked] = React.useState(true);

  const handleCheck = (event) => {
    setChecked(event.target.checked);
  };

  const handleOpenDialog = () => {
    setOpenDialog(true);
  };

  console.log(props.id);

  const changePrice = (e) => {
    setPrice(e.target.value);
  };
  const changeImg = (e) => {
    setImg(e.target.value);
  };
  const changeType = (e) => {
    setType(e.target.value);
  };
  const changeDesc = (e) => {
    setDesc(e.target.value);
  };
  const changeGuests = (e) => {
    setGuests(e.target.value);
  };



  const submitHandler = (event) => {
    event.preventDefault();
    let url;
    if (price == "") setPrice(props.price);
    if (img == "") setImg(props.img);
    if (carType == "") setType(props.desc);
    if (desc == "") setDesc(props.desc);
    if (noGuests == "" || noGuests == null) setGuests(props.guests);
  
    if (props.type === "WeddingHall") {
      url = "http://localhost:8080/api/wedding_hall_activities/edit/";
      fetch(url, {
        method: "PATCH",
        body: JSON.stringify({
          id: props.id,
          numberOfGuests: noGuests,
          hasRoom: checked,
          price: price,
          img: img,
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
          handleCloseDialog();
          return res.json();
        })
        .then((res) => {
          handleCloseDialog();
        })
        .catch((err) => {
          setErrorModal({
            title: "Invalid Request",
            message: err.message,
          });
        });
    } else if (props.type === "Stylist") {
      url = "http://localhost:8080/api/stylist_activities/edit/";
      fetch(url, {
        method: "PATCH",
        body: JSON.stringify({
          id: props.id,
          description: desc,
          price: price,
          img: img,
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
        .then((res) => {
          handleCloseDialog();
        })
        .catch((err) => {
          setErrorModal({
            title: "Invalid Request",
            message: err.message,
          });
        });
    } else if (props.type === "Limousine") {
      url = "http://localhost:8080/api/limousine_activities/edit/";
      fetch(url, {
        method: "PATCH",
        body: JSON.stringify({
          id: props.id,
          carType: carType,
          price: price,
          img: img,
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
        .then((res) => {
          handleCloseDialog();
        })
        .catch((err) => {
          setErrorModal({
            title: "Invalid Request",
            message: err.message,
          });
        });
    }
  };

  const timeHandler = () => {
    let url;
    url = "http://localhost:8080/api/time_slots/";
    fetch(url, {
      method: "POST",
      body: JSON.stringify({
        activityId: props.id,
        activityType: props.type,
        startTime: from,
        endTime: to,
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
      .then((res) => {
        handleClosePicker();
      })
      .catch((err) => {
        setErrorModal({
          title: "Invalid Request",
          message: err.message,
        });
      });
  };

  return (
    <Card className={classes.item} onClick={props.fn}>
      <div className={classes["item__description"]}>
        <img
          className={classes.img}
          src={props.img}
          alt={props.alt}
          width="200px"
          height="auto"
        />
        {ctx.type === "Customer" && <p>Provider : {props.name}</p>}
        {props.type === "WeddingHall" && (
          <div>
            <p>Number of total Guests to attend : {guests}</p>
            <p>Room Service : {hasRoom}</p>
          </div>
        )}
        {props.type === "Limousine" && (
          <p>The type of our offered Car : {props.desc}</p>
        )}
        {props.type === "Stylist" && <p>Description : {props.desc}</p>}
        <div className={classes["item__price"]}>${props.price}</div>
        <div>
          <Date3 date={props.time} id={props.id} type={props.type} />
        </div>
        <div>
          <Button
            variant="outlined"
            sx={{ margin: 2 }}
            color="inherit"
            id="basic-button"
            startIcon={<AddIcon />}
            onClick={handleOpenPicker}
          >
            Create Time Slot
          </Button>
        </div>
        <div>
          <Button
            variant="outlined"
            sx={{ margin: 2 }}
            color="inherit"
            id="basic-button"
            onClick={handleOpenDialog}
          >
            Edit Service
          </Button>
        </div>
        <Dialog
          fullScreen
          open={openDialog}
          onClose={handleClose}
          TransitionComponent={Transition}
        >
          <AppBar sx={{ position: "relative" }}>
            <Toolbar sx={{ backgroundColor: "#5f0a87" }}>
              <IconButton
                edge="start"
                color="inherit"
                onClick={handleCloseDialog}
                aria-label="close"
              >
                <CloseIcon />
              </IconButton>
              <Typography
                sx={{
                  ml: 2,
                  flex: 1,
                  fontSize: "2rem",
                  fontFamily: "Pushster",
                }}
                variant="h6"
                component="div"
              >
                {text}
                <link
                  href="https://fonts.googleapis.com/css2?family=Pushster&display=swap"
                  rel="stylesheet"
                ></link>
              </Typography>
            </Toolbar>
          </AppBar>
          <div className={classes3.header}>
            {props.type === "WeddingHall" && (
              <form>
                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="description">Number Of Guests</label>
                    <input
                      type="guests"
                      id="guests"
                      defaultValue={guests}
                      //defaultValue={props.numberOfGuests}
                      onChange={changeGuests}
                      required
                      variant="filled"
                    />
                  </div>
                </div>
                <div>
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={checked}
                        onChange={handleCheck}
                        sx={{ color: "white", fontWeight: "bold" }}
                      />
                    }
                    label="Has a room"
                  />
                </div>
                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="price">Price</label>
                    <input
                      placeholder="Enter your service's price"
                      type="price"
                      id="price"
                      defaultValue={price}
                      //value={price}
                      onChange={changePrice}
                      required
                      variant="filled"
                    />
                  </div>
                </div>
                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="img">Image URL</label>
                    <input
                      placeholder="Enter image URL"
                      type="img"
                      id="img"
                      //value={img}
                      defaultValue={img}
                      onChange={changeImg}
                      required
                    />
                  </div>
                </div>
              </form>
            )}
            {props.type === "Stylist" && (
              <form>
                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="description">Description</label>
                    <input
                      placeholder="Enter service description"
                      type="description"
                      id="description"
                      //value={desc}
                      defaultValue={props.desc}
                      onChange={changeDesc}
                      required
                    />
                  </div>
                </div>
                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="price">Price</label>
                    <input
                      placeholder="Enter your service's price"
                      type="price"
                      id="price"
                      //value={price}
                      defaultValue={props.price}
                      onChange={changePrice}
                      required
                    />
                  </div>
                </div>

                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="img">Image URL</label>
                    <input
                      placeholder="Enter image URL"
                      type="img"
                      id="img"
                      //value={img}
                      defaultValue={props.img}
                      onChange={changeImg}
                      required
                    />
                  </div>
                </div>
              </form>
            )}
            {props.type === "Limousine" && (
              <form>
                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="description">Car Type</label>
                    <input
                      placeholder="Enter car type"
                      type="cartype"
                      id="cartype"
                      //value={carType}
                      defaultValue={props.desc}
                      onChange={changeType}
                      required
                    />
                  </div>
                </div>
                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="price">Price</label>
                    <input
                      placeholder="Enter your service's price"
                      type="price"
                      id="price"
                      //value={price}
                      defaultValue={props.price}
                      onChange={changePrice}
                      required
                    />
                  </div>
                </div>

                <div>
                  <div className={classes3.forms}>
                    <label htmlFor="img">Image URL</label>
                    <input
                      placeholder="Enter image URL"
                      type="img"
                      id="img"
                      //value={img}
                      defaultValue={props.img}
                      onChange={changeImg}
                      required
                    />
                  </div>
                </div>
              </form>
            )}
            <div className={classes3.actions}>
              <Button autoFocus color="inherit" onClick={submitHandler}>
                Save
              </Button>
            </div>
          </div>
        </Dialog>
      </div>

      {openPicker && (
        <div>
          <div>
            {errorModal && (
              <ErrorModal
                title={errorModal.title}
                message={errorModal.message}
                onConfirm={errorHandler}
              />
            )}
          </div>
          <div fullScreen className={classes2.backdrop} />
          <Card className={classes2.modal}>
            <header className={classes2.header}>
              <h2>Time Slot Addition</h2>
            </header>
            <div className={classes2.content}>
              <LocalizationProvider dateAdapter={AdapterDateFns}>
                <Stack spacing={2}>
                  <TimePicker
                    ampm={false}
                    openTo="hours"
                    views={["hours", "minutes"]}
                    inputFormat="HH:mm"
                    mask="__:__"
                    label="From"
                    value={valueFrom}
                    onChange={(newValue) => {
                      setValueFrom(newValue.toString());
                    }}
                    renderInput={(params) => <TextField {...params} />}
                  />
                  <TimePicker
                    ampm={false}
                    openTo="hours"
                    views={["hours", "minutes"]}
                    inputFormat="HH:mm"
                    mask="__:__"
                    label="To"
                    value={valueTo}
                    onChange={(newValue) => {
                      setValueTo(newValue.toString());
                    }}
                    renderInput={(params) => <TextField {...params} />}
                  />
                </Stack>
              </LocalizationProvider>
            </div>
            <footer className={classes2.actions}>
              <button onClick={timeHandler}>Add</button>
              <button onClick={handleClosePicker}>Cancel</button>
            </footer>
          </Card>
        </div>
      )}
    </Card>
  );
}
export default Item;
