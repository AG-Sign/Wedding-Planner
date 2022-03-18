import classes from "./reservations.module.css";
import React, { useState, useContext } from "react";
import AuthContext from "../context/auth-context";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import Tooltip from "@mui/material/Tooltip";
import { useHistory } from "react-router-dom";
import Button from "@mui/material/Button";
import HomeIcon from "@mui/icons-material/Home";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import SearchIcon from "@mui/icons-material/Search";
import Item2 from "../items/Item2";
import Alert from "@mui/material/Alert";
import { styled } from "@mui/material/styles";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import OutlinedInput from "@mui/material/OutlinedInput";
import FormControl from "@mui/material/FormControl";
import Box from "@mui/material/Box";
import Dialog from "@mui/material/Dialog";
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
import TextField from '@mui/material/TextField';
import RefreshIcon from '@mui/icons-material/Refresh';


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
    backgroundPosition: "center",
  })
);

const Reservations = () => {
  const history = useHistory();
  const ctx = useContext(AuthContext);
  const [hallList, setHallList] = useState([]);
  const [stylistList, setStylistList] = useState([]);
  const [limousineList, setLimousineList] = useState([]);

  const [open, setOpen] = useState(false);
  const [criteria, setCriteria] = useState("Provider's Name");

  const [name,setName]=useState("")
  const [min,setMin]=useState("")
  const [max,setMax]=useState("")

  const [nameHelper,setNameHelper]=useState("")
  const [minHelper,setMinHelper]=useState("")
  const [maxHelper,setMaxHelper]=useState("")

  const [search,setSearch]=useState(false)
  const [reqHall,setReqHall]=useState(true)
  const [reqStylist,setReqStylist]=useState(true)
  const [reqLim,setReqLim]=useState(true)

  const nameChangeHandler = event =>{
    setNameHelper("")
    setName(event.target.value)
  }
  const minChangeHandler = event =>{
    setMinHelper("")
    setMin(event.target.value)
  }
  const maxChangeHandler = event =>{
    setMaxHelper("")
    setMax(event.target.value)
  }

  const handleChange = (event) => {
    setCriteria(event.target.value);
  };

  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = (event) => {
    setMax("")
    setMin("")
    setName("")
    setMaxHelper("")
    setMinHelper("")
    setNameHelper("")
    setOpen(false);
  };
  const handleOK = (event) => {
    if(criteria==="Price"){
      if(min===""){
        setMinHelper("Enter Min value range")
      }
      if(max===""){
        setMaxHelper("Enter Max value range")
      }
      if(min==="" || max===""){
        return;
      }
      
      setSearch(true)
      setReqHall(true)
      setReqLim(true)
      setReqStylist(true)
      setOpen(false)
    }else if(criteria==="Provider's Name"){
      if(name===""){
        setNameHelper("Enter Provider's Name")
        return;
      }
      setSearch(true)
      setReqHall(true)
      setReqLim(true)
      setReqStylist(true)
      setOpen(false)
    }
  };

  const requestHalls = () => {
    if(reqHall){
      let url;
      url = "http://localhost:8080/api/wedding_hall_activities";
      if(search){
        if(criteria==="Price"){
          url+="?min="+min+"&max="+max
        }else if(criteria==="Provider's Name"){
          url+="?provider="+name
        }
      }
      fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `${ctx.token}`,
        },
      })
        .then((res) => {
          return res.json();
        })
        .then((data) => {
          setReqHall(false)
          setHallList(data);
        });
    
  }
  };

  const requestStylists = () => {
    if(reqStylist){
      let url;
      url = "http://localhost:8080/api/stylist_activities";
      if(search){

        if(criteria==="Price"){
          url+="?min="+min+"&max="+max
        }else if(criteria==="Provider's Name"){
          url+="?provider="+name
        }
      }
      fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `${ctx.token}`,
        },
      })
        .then(async (res) => {
          return res.json();
        })
        .then((data) => {
          setReqStylist(false)
          setStylistList(data);
        });
    
  }
  };

  const requestLimousines = () => {
    if(reqLim){
      let url;
      url = "http://localhost:8080/api/limousine_activities";
      if(search){

        if(criteria==="Price"){
          url+="?min="+min+"&max="+max
        }else if(criteria==="Provider's Name"){
          url+="?provider="+name
        }
      }
      fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `${ctx.token}`,
        },
      })
        .then(async (res) => {
          return res.json();
        })
        .then((data) => {
          setReqLim(false)
          setLimousineList(data);
        });
    
  }
  };

  return (
    <React.Fragment>
      <div className={classes.container}>
        <div className={classes.text}>
          Choose a Service
          <link
            href="https://fonts.googleapis.com/css2?family=Pushster&display=swap"
            rel="stylesheet"
          ></link>
        </div>
        <Tooltip
          style={{
            marginLeft: "20px",
            marginTop:"10px",
            marginBottom: "15px",
          }}
          title="Home Page"
          onClick={() => {
            history.replace("/profile");
          }}
        >
          <IconButton>
            <HomeIcon />
          </IconButton>
        </Tooltip>

        <span className={classes.header}>
          <Button
            variant="outlined"
            sx={{ margin: 2 }}
            color="inherit"
            startIcon={<SearchIcon />}
            onClick={handleClickOpen}
          >
            Search
          </Button>
        </span>
        <Tooltip
          style={{
            marginLeft: "5px",
            marginTop:"10px",
            marginBottom: "15px",
          }}
          title="Refresh"
          onClick={() => {
            window.location.reload();
          }}
        >
          <IconButton>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      </div>
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
            {hallList.length === 0 && (
              <Alert sx={{ width: "fit-content" }} severity="warning">
                No Wedding Halls Available
              </Alert>
            )}
            {hallList.length !== 0 &&
              hallList.map((ex) => (
                <Item2
                  key={ex.id}
                  id={ex.id}
                  name={ex.provider.name}
                  img={ex.img}
                  alt="Wedding Hall"
                  desc={`${ex.numberOfGuests}*${ex.hasRoom}`}
                  price={ex.price}
                  timeslots={ex.timeslots}
                  type="hall"
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
                No Stylists Available
              </Alert>
            )}
            {stylistList.length !== 0 &&
              stylistList.map((ex) => (
                <Item2
                  key={ex.id}
                  id={ex.id}
                  name={ex.provider.name}
                  img={ex.img}
                  alt="Stylist"
                  desc={ex.description}
                  price={ex.price}
                  timeslots={ex.timeslots}
                  type="stylist"
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
                No Limousines Available
              </Alert>
            )}
            {limousineList.length !== 0 &&
              limousineList.map((ex) => (
                <Item2
                  key={ex.id}
                  id={ex.id}
                  name={ex.provider.name}
                  img={ex.img}
                  alt="Limousine"
                  desc={ex.carType}
                  price={ex.price}
                  timeslots={ex.timeslots}
                  type="limousine"
                />
              ))}
          </ul>
        </AccordDetails>
      </Accord>

      <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
        <DialogTitle>Search</DialogTitle>
        <DialogContent>
          <Box component="form" sx={{ display: "flex", flexWrap: "wrap" }}>
            <FormControl sx={{ m: 1, minWidth: 120 }}>
            <InputLabel htmlFor="demo-dialog-native">Criteria</InputLabel>
              <Select
                native
                value={criteria}
                onChange={handleChange}
                input={
                  <OutlinedInput label="Criteria" id="demo-dialog-native" />
                }
              >
                <option criteria="Provider's Name">Provider's Name</option>
                <option criteria="Price">Price</option>
              </Select>
            </FormControl>
          </Box>
        </DialogContent>
        {criteria==="Provider's Name" &&
        <TextField
          label="Name"
          value={name}
          sx={{margin:"15px"}}
          onChange={nameChangeHandler}
          placeholder="Provider's Name"
          helperText={nameHelper}
        />}
        {criteria==="Price" &&
        <TextField
          label="Min"
          value={min}
          sx={{margin:"15px"}}
          onChange={minChangeHandler}
          placeholder="Minimum Range"
          helperText={minHelper}
        />}
        {criteria==="Price" &&
        <TextField
          label="Max"
          value={max}
          sx={{margin:"15px"}}
          onChange={maxChangeHandler}
          placeholder="Maximum range"
          helperText={maxHelper}
        />}
        <DialogActions>
          <Button sx={{ color: "#5f0a87" }} onClick={handleClose}>
            Close
          </Button>
          <Button sx={{ color: "#5f0a87" }} onClick={handleOK}>
            Search
          </Button>
        </DialogActions>
      </Dialog>
    </React.Fragment>
  );
};

export default Reservations;
