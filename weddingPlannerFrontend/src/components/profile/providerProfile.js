import * as React from "react";
import { useState, useContext } from "react";
import Typography from "@mui/material/Typography";
import classes from "./providerProfile.module.css";
import AddIcon from "@mui/icons-material/Add";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import Slide from "@mui/material/Slide";
import Select from "@mui/material/Select";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import InputLabel from "@mui/material/InputLabel";
import OutlinedInput from "@mui/material/OutlinedInput";
import FormControl from "@mui/material/FormControl";
import Box from "@mui/material/Box";
import Checkbox from "@mui/material/Checkbox";
import FormControlLabel from "@mui/material/FormControlLabel";
import ErrorModal from "../Modal/ErrorModal";
import AuthContext from "../context/auth-context";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Item3 from "../items/Item3";
import Alert from "@mui/material/Alert";
import { styled } from "@mui/material/styles";


const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});


const Accord = styled((props) => (
  <Accordion disableGutters elevation={0} square {...props} />
))(({ theme }) => ({
  border: `1.4px solid pink`,
  "&:not(:last-child)": {
    borderBottom: 0,
  },
  "&:before": {
    display: "none",
  },
  borderRadius: "7px",
}));
const AccordDetails = styled(AccordionDetails)(({ theme }) => ({
  padding: theme.spacing(2),
  backgroundColor: "#FEECE9",
  borderRadius: "7px",
  borderTop: "1px solid rgba(0, 0, 0, .125)",
}));

const AccordSummary = styled((props) => <AccordionSummary {...props} />)(
  ({ theme }) => ({
    backgroundImage:
      "url(https://cdn2.vectorstock.com/i/1000x1000/61/66/colorful-flowers-on-white-background-spring-vector-24796166.jpg)",
    borderRadius: "7px",
    backgroundSize: "auto",
    backgroundRepeat: "no-repeat",
    backgroundPosition: "center",
  })
);

const AccordSummary2 = styled((props) => <AccordionSummary {...props} />)(
  ({ theme }) => ({
    backgroundImage:
      "url(https://img.myloview.com/stickers/professional-decorative-cosmetics-makeup-tools-on-white-background-flat-composition-beauty-fashion-flat-lay-top-view-700-242356923.jpg)",
    borderRadius: "7px",
    backgroundSize: "auto",
    backgroundRepeat: "no-repeat",
    backgroundPosition: "center",
  })
);

const AccordSummary3 = styled((props) => <AccordionSummary {...props} />)(
  ({ theme }) => ({
    backgroundImage:
      "url(https://image.shutterstock.com/image-photo/isolated-limousine-on-white-needs-260nw-25879549.jpg)",
    borderRadius: "7px",
    backgroundSize: "250px 210px",
    backgroundRepeat: "no-repeat",
    backgroundPosition: "center"
  })
);

const ProviderProfile = () => {
  const ctx = useContext(AuthContext);
  const [hallList, setHallList] = useState([]);
  const [stylistList, setStylistList] = useState([]);
  const [limousineList, setLimousineList] = useState([]);
  

  const requestHalls = () => {
    if (hallList.length === 0) {
      let url;
      url = "http://localhost:8080/api/wedding_hall_activities/provider_activities/";
      fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `${ctx.token}`,
        },
      })
        .then(async (res) => {
          return res.json();
        })
        .then((data) => {
          console.log(data);
          setHallList(data);
        });
    }
  };

  const requestStylists = () => {
    if (stylistList.length === 0) {
      let url;
      url = "http://localhost:8080/api/stylist_activities/provider_activities/";
      fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `${ctx.token}`,
        },
      })
        .then(async (res) => {
          return res.json();
        })
        .then((data) => {
          setStylistList(data);
        });
    }
  };

  const requestLimousines = () => {
    if (limousineList.length === 0) {
      let url;
      url = "http://localhost:8080/api/limousine_activities/provider_activities/";
      fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `${ctx.token}`,
        },
      })
        .then(async (res) => {
          return res.json();
        })
        .then((data) => {
          setLimousineList(data);
        });
    }
  };
  //addition dialog
  const [openDialog, setOpenDialog] = React.useState(false);

  const handleClickOpenDialog = () => {
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  //choosing activity
  const [open, setOpen] = React.useState(false);
  const [activity, setActivity] = React.useState("");

  const handleChange = (event) => {
    setActivity(event.target.value || "");
  };

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = (event, reason) => {
    if (reason !== "backdropClick") {
      setOpen(false);
    }
  };
  const handleOK = (event, reason) => {
    handleClickOpenDialog();
  };

  let text;
  if (activity === "Wedding Hall") text = "Add Wedding Hall Service";
  else if (activity === "Stylist") text = "Add Stylist Service";
  else if (activity === "Limousine") text = "Add Limousine Service";

  const [error, setError] = useState(false);
  const [errorModal, setErrorModal] = useState();

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


  const [price, setPrice] = useState("");
  const [img, setImg] = useState("");
  const [carType, setType] = useState("");
  const [desc, setDesc] = useState("");
  const [noGuests, setGuests] = useState("");

  //has room check box
  const [checked, setChecked] = React.useState(true);

  const handleCheck = (event) => {
    setChecked(event.target.checked);
  };

  const submitHandler = (event) => {
    event.preventDefault();

    console.log("Submitted!");
    console.log(noGuests, checked, price, img);

    let url;
    if (activity === "Wedding Hall") {
      url = "http://localhost:8080/api/wedding_hall_activities/";
      fetch(url, {
        method: "POST",
        body: JSON.stringify({
          numberOfGuests: noGuests,
          hasRoom: checked,
          price: price,
          img: img,
        }),
        headers: {
          "Content-Type": "application/json",
          "Authorization": `${ctx.token}`,
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
    } else if (activity === "Stylist") {
      url = "http://localhost:8080/api/stylist_activities/";
      fetch(url, {
        method: "POST",
        body: JSON.stringify({
          description: desc,
          price: price,
          img: img,
        }),
        headers: {
          "Content-Type": "application/json",
          "Authorization": `${ctx.token}`,
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
    } else if (activity === "Limousine") {
      url = "http://localhost:8080/api/limousine_activities/";
      fetch(url, {
        method: "POST",
        body: JSON.stringify({
          carType: carType,
          price: price,
          img: img,
        }),
        headers: {
          "Content-Type": "application/json",
          "Authorization": `${ctx.token}`,
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

  return (
    <div>
      
      <Button
        variant="outlined"
        sx={{ margin: 2 }}
        color="inherit"
        id="basic-button"
        startIcon={<AddIcon />}
        aria-controls={open ? "basic-menu" : undefined}
        aria-haspopup="true"
        aria-expanded={open ? "true" : undefined}
        onClick={handleClickOpen}
      >
        Add Service
      </Button>
      <Accord>
        <div onClick={requestHalls}>
          <AccordSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel1a-content"
            id="panel1a-header"
          >
            <Typography
              sx={{
                fontSize: "1.3rem",
                margin: 0,
                fontFamily: "'Questrial', sans-serif",
              }}
            >
              Wedding Halls{" "}
              <link
                href="https://fonts.googleapis.com/css2?family=Questrial&display=swap"
                rel="stylesheet"
              ></link>
            </Typography>
          </AccordSummary>
        </div>
        <AccordDetails>
          <ul>
          {hallList.length===0 && <Alert sx={{width :"fit-content"}} severity="warning">No Wedding Halls Added</Alert>}

            {
              hallList.map((ex) => (
                <Item3
                  key={ex.id}
                  id={ex.id}
                  img={ex.img}
                  alt="Wedding Hall"
                  guests={ex.numberOfGuests}
                  desc={`${ex.numberOfGuests}*${ex.hasRoom}`}
                  price={ex.price}
                  time={ex.timeslots}
                  type="WeddingHall"
                />
              ))}
          </ul>
        </AccordDetails>
      </Accord>

      <Accord>
        <div onClick={requestStylists}>
          <AccordSummary2
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel2a-content"
            id="panel2a-header"
          >
            <Typography
            sx={{
              fontSize: "1.3rem",
              margin: 0,
              fontFamily: "'Questrial', sans-serif",
            }}
          >
            Stylists{" "}
            <link
              href="https://fonts.googleapis.com/css2?family=Questrial&display=swap"
              rel="stylesheet"
            ></link>
          </Typography>
          </AccordSummary2>
        </div>
        <AccordDetails>
          <ul>
            {stylistList.length === 0 && (
              <Alert sx={{ width: "fit-content" }} severity="warning">
                No Stylists Added
              </Alert>
            )}
            {stylistList.length !== 0 &&
              stylistList.map((ex) => (
                <Item3
                  key={ex.id}
                  id={ex.id}
                  img={ex.img}
                  alt="Stylist"
                  desc={ex.description}
                  price={ex.price}
                  time={ex.timeslots}
                  type="Stylist"
                />
              ))}
          </ul>
        </AccordDetails>
      </Accord>
      <Accord>
        <div onClick={requestLimousines}>
          <AccordSummary3
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel3a-content"
            id="panel3a-header"
          >
            <Typography
            sx={{
              fontSize: "1.3rem",
              margin: 0,
              fontFamily: "'Questrial', sans-serif",
            }}
          >
            Limousines{" "}
            <link
              href="https://fonts.googleapis.com/css2?family=Questrial&display=swap"
              rel="stylesheet"
            ></link>
          </Typography>
          </AccordSummary3>
        </div>
        <AccordDetails>
          <ul>
            {limousineList.length === 0 && (
              <Alert sx={{ width: "fit-content" }} severity="warning">
                No Limousines Added
              </Alert>
            )}
            {limousineList.length !== 0 &&
              limousineList.map((ex) => (
                <Item3
                  key={ex.id}
                  id={ex.id}
                  img={ex.img}
                  alt="Limousine"
                  desc={ex.carType}
                  price={ex.price}
                  time={ex.timeslots}
                  type="Limousine"
                />
              ))}
          </ul>
        </AccordDetails>
      </Accord>

      <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
        <DialogTitle>Choose Service</DialogTitle>
        <DialogContent>
          <Box component="form" sx={{ display: "flex", flexWrap: "wrap" }}>
            <FormControl sx={{ m: 1, minWidth: 120 }}>
              <InputLabel htmlFor="demo-dialog-native">Service</InputLabel>
              <Select
                native
                value={activity}
                onChange={handleChange}
                input={
                  <OutlinedInput label="Service" id="demo-dialog-native" />
                }
              >
                <option aria-label="None" activity=""></option>
                <option activity="Wedding Hall">Wedding Hall</option>
                <option activity="Stylist">Stylist</option>
                <option activity="Limousine">Limousine</option>
              </Select>
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button sx={{ color: "#5f0a87" }} onClick={handleClose}>
            Cancel
          </Button>
          <Button sx={{ color: "#5f0a87" }} onClick={handleOK}>
            Ok
          </Button>
        </DialogActions>
      </Dialog>
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
              sx={{ ml: 2, flex: 1, fontSize: "2rem", fontFamily: "Pushster" }}
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
        <div className={classes.header}>
          {activity === "Wedding Hall" && (
            <form onSubmit={submitHandler}>
              <div>
                <div className={classes.forms}>
                  <label htmlFor="description">Number Of Guests</label>
                  <input
                    placeholder="Enter number of guests"
                    type="guests"
                    id="guests"
                    value={noGuests}
                    onChange={changeGuests}
                    required
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
                <div className={classes.forms}>
                  <label htmlFor="price">Price</label>
                  <input
                    placeholder="Enter your service's price"
                    type="price"
                    id="price"
                    value={price}
                    onChange={changePrice}
                    required
                  />
                </div>
              </div>
              <div>
                <div className={classes.forms}>
                  <label htmlFor="img">Image URL</label>
                  <input
                    placeholder="Enter image URL"
                    type="img"
                    id="img"
                    value={img}
                    onChange={changeImg}
                    required
                  />
                </div>
              </div>
            </form>
          )}
          {activity === "Stylist" && (
            <form>
              <div>
                <div className={classes.forms}>
                  <label htmlFor="description">Description</label>
                  <input
                    placeholder="Enter service description"
                    type="description"
                    id="description"
                    value={desc}
                    onChange={changeDesc}
                    required
                  />
                </div>
              </div>
              <div>
                <div className={classes.forms}>
                  <label htmlFor="price">Price</label>
                  <input
                    placeholder="Enter your service's price"
                    type="price"
                    id="price"
                    value={price}
                    onChange={changePrice}
                    required
                  />
                </div>
              </div>

              <div>
                <div className={classes.forms}>
                  <label htmlFor="img">Image URL</label>
                  <input
                    placeholder="Enter image URL"
                    type="img"
                    id="img"
                    value={img}
                    onChange={changeImg}
                    required
                  />
                </div>
              </div>
            </form>
          )}
          {activity === "Limousine" && (
            <form>
              <div>
                <div className={classes.forms}>
                  <label htmlFor="description">Car Type</label>
                  <input
                    placeholder="Enter car type"
                    type="cartype"
                    id="cartype"
                    value={carType}
                    onChange={changeType}
                    required
                  />
                </div>
              </div>
              <div>
                <div className={classes.forms}>
                  <label htmlFor="price">Price</label>
                  <input
                    placeholder="Enter your service's price"
                    type="price"
                    id="price"
                    value={price}
                    onChange={changePrice}
                    required
                  />
                </div>
              </div>

              <div>
                <div className={classes.forms}>
                  <label htmlFor="img">Image URL</label>
                  <input
                    placeholder="Enter image URL"
                    type="img"
                    id="img"
                    value={img}
                    onChange={changeImg}
                    required
                  />
                </div>
              </div>
            </form>
          )}
          <div className={classes.actions}>
            <Button autoFocus color="inherit" onClick={submitHandler}>
              Add
            </Button>
          </div>
        </div>
      </Dialog>
    </div>
  );
};

export default ProviderProfile;