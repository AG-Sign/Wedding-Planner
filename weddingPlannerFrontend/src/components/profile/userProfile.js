import { useState, useContext } from "react";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import Typography from "@mui/material/Typography";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Item from "../items/Item";
import AddIcon from "@mui/icons-material/Add";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import Tooltip from "@mui/material/Tooltip";
import AddTaskIcon from "@mui/icons-material/AddTask";
import classes from "./userProfile.module.css";
import { useHistory } from "react-router-dom";
import ModalDate from "../Modal/ModalDate";
import ErrorModal from "../Modal/ErrorModal";
import AuthContext from "../context/auth-context";
import Alert from '@mui/material/Alert';
import { styled } from "@mui/material/styles";
import DeleteIcon from '@mui/icons-material/Delete';

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


const UserProfile = () => {
  const ctx = useContext(AuthContext);
  const [errorModal, setErrorModal] = useState();
  const [modal, setModal] = useState();
  const history = useHistory();
  const [hallList, setHallList] = useState([]);
  const [stylistList, setStylistList] = useState([]);
  const [limousineList, setLimousineList] = useState([]);
  const [,setAux]=useState(null)

  

  const changeAux=(data)=>{
    setAux(true);
    if(data.type==="hall"){
      var temp =hallList.filter(ex=>{
        return ex.id.toString() !== data.id
      })
      setHallList(temp)
    }else if(data.type==="stylist"){
      var temp2 =stylistList.filter(ex=>{
        return ex.id.toString() !== data.id
      })
      setStylistList(temp2)
    }else{
      var temp3=limousineList.filter(ex=>{
        return ex.id.toString() !== data.id
      })
      setLimousineList(temp3)
    }
  }

  const requestHalls = () => {
    if (hallList.length === 0) {
      let url;
      url = "http://localhost:8080/api/reservations/weddingHall_reservations";
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
          setHallList(data);
        });
    }
  };

  const requestStylists = () => {
    if (stylistList.length === 0) {
      let url;
      url = "http://localhost:8080/api/reservations/stylist_reservations";
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
      url = "http://localhost:8080/api/reservations/limousine_reservations";
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

  const cancelCurrent =()=>{
    fetch("http://localhost:8080/api/reservations", {
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
        setHallList([])
        setLimousineList([])
        setStylistList([])
      })
      .catch((err) => {
        setErrorModal({
          title: "Invalid Request",
          message: err.message,
        });
      });
  }

  const sendDate = (date) => {
    var chosen = new Date(date.toDateString());
    var today = new Date(new Date().toDateString());
    setModal(null);
    if (chosen < today) {
      setErrorModal({
        title: "Invalid Date",
        message:
          "Please Enter proper Date : You can't choose a date already in the past",
      });
    } else {
      var value =
        chosen.getDate() +
        "/" +
        chosen.getMonth() +
        1 +
        "/" +
        chosen.getFullYear();
      fetch("http://localhost:8080/api/reservations/", {
        method: "POST",
        body: JSON.stringify({
          date: value,
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
        })
        .catch((err) => {
          setErrorModal({
            title: "Invalid Request",
            message: err.message,
          });
        });
    }
  };

  const modalHandler = () => {
    setModal(null);
  };
  const errorHandler = () => {
    setErrorModal(null);
  };

  return (
    <div className={classes.header}>
      {errorModal && (
        <ErrorModal
          title={errorModal.title}
          message={errorModal.message}
          onConfirm={errorHandler}
        />
      )}
      {modal && <ModalDate onConfirm={sendDate} onClose={modalHandler} />}
      <Tooltip
        title="Create a Main Reservation"
        followCursor
        style={{
          marginLeft: "20px",
          color: "black",
        }}
        onClick={() => {
          setModal({
            dummy: "Invalid Submission",
          });
        }}
      >
        <IconButton>
          <AddTaskIcon />
        </IconButton>
      </Tooltip>
      <Button
        variant="outlined"
        startIcon={<AddIcon />}
        sx={{ margin: 2 }}
        color="inherit"
        onClick={() => {
          history.replace("/reservations");
        }}
      >
        Reserve
      </Button>
      <Button
        variant="outlined"
        startIcon={<DeleteIcon />}
        sx={{ margin: 2 ,'&:hover':{color:'red'}}}
        color="inherit"
        className={classes.delete}
        onClick={cancelCurrent}
      >
        Cancel Current Package
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
          {hallList.length===0 && <Alert sx={{width :"fit-content"}} severity="warning">No Wedding Halls is Reserved</Alert>}
            {hallList.length!==0 && hallList
              .map((ex) => (
                <Item
                  func={changeAux}
                  id={ex.id}
                  type="hall"
                  key={ex.id}
                  name={ex.weddingHallActivityDto.provider.name}
                  img={ex.weddingHallActivityDto.img}
                  alt="Wedding Hall"
                  desc={`${ex.weddingHallActivityDto.numberOfGuests}*${ex.weddingHallActivityDto.hasRoom}`}
                  price={ex.weddingHallActivityDto.price}
                  timeSlot={ex.timeSlot}
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
          {stylistList.length===0 && <Alert sx={{width :"fit-content"}} severity="warning">No Stylists is Reserved</Alert>}
            {stylistList.length!==0 && stylistList
              .map((ex) => (
                <Item
                func={changeAux}
                  id={ex.id}
                  type="stylist"
                  key={ex.id}
                  name={ex.stylistActivityDto.provider.name}
                  img={ex.stylistActivityDto.img}
                  alt="Stylist"
                  desc={ex.stylistActivityDto.description}
                  price={ex.stylistActivityDto.price}
                  timeSlot={ex.timeSlot}
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
          {limousineList.length===0 && <Alert sx={{width :"fit-content"}} severity="warning">No Limousines is Reserved</Alert>}
            {limousineList.length!==0 && limousineList
              .map((ex) => (
                <Item
                id={ex.id}
                  type="limousine"
                   key={ex.id}
                  name={ex.limousineActivityDto.provider.name}
                  img={ex.limousineActivityDto.img}
                  alt="Limousine"
                  desc={ex.limousineActivityDto.description}
                  price={ex.limousineActivityDto.price}
                  timeSlot={ex.timeSlot}
                />
              ))}
          </ul>
        </AccordDetails>
      </Accord>
    </div>
  );
};

export default UserProfile;
