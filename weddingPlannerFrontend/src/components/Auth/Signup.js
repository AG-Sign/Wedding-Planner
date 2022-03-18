import { useState } from "react";
import { useHistory } from "react-router-dom";
import useInputReducer from "../custom-hook/useInputReducer";
import Alert from "@mui/material/Alert";
import Radio from "@mui/material/Radio";
import RadioGroup from "@mui/material/RadioGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import FormControl from "@mui/material/FormControl";
import classes from "./Forms.module.css";
import ErrorModal from "../Modal/ErrorModal";
import { cyan } from "@mui/material/colors";

const Signup = (props) => {
  const [value, setValue] = useState("");
  const [helperText, setHelperText] = useState("");
  const [error, setError] = useState(false);
  const [errorModal, setErrorModal] = useState();

  const radioHandler = (event) => {
    setValue(event.target.value);
    setHelperText("");
    setError(false);
  };

  const {
    value: nameValue,
    isValid: nameIsValid,
    hasError: nameHasError,
    valueChangeHandler: nameChangeHandler,
    inputTouchHandler: nameTouchHandler,
    reset: resetName,
  } = useInputReducer((value) => value.trim() !== "");

  const {
    value: emailValue,
    isValid: emailIsValid,
    hasError: emailHasError,
    valueChangeHandler: emailChangeHandler,
    inputTouchHandler: emailTouchHandler,
    reset: resetEmail,
  } = useInputReducer((value) =>
    value.match(
      /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z-]+(?:\.[a-zA-Z-]+)*$/
    )
  );

  const {
    value: pswValue,
    isValid: pswIsValid,
    hasError: pswHasError,
    valueChangeHandler: pswChangeHandler,
    inputTouchHandler: pswTouchHandler,
    reset: resetPsw,
  } = useInputReducer((value) =>
    value.match(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/)
  );

  const {
    value: addressValue,
    isValid: addressIsValid,
    hasError: addressHasError,
    valueChangeHandler: addressChangeHandler,
    inputTouchHandler: addressTouchHandler,
    reset: resetAddress,
  } = useInputReducer((value) => value.trim() !== "");

  const history = useHistory();
  let formIsValid = false;
  if (
    pswIsValid &&
    emailIsValid &&
    addressIsValid &&
    nameIsValid &&
    value !== ""
  ) {
    formIsValid = true;
  }

  const switchAuthModeHandler = () => {
    resetAddress();
    resetEmail();
    resetName();
    resetPsw();
    setValue("");
    setHelperText("");
    setError(false);
    history.replace("/auth");
  };

  const errorHandler = () => {
    setErrorModal(null);
  };

  const submitHandler = (event) => {
    event.preventDefault();
    if (value === "") {
      setHelperText("Please Choose an option");
      setError(true);
    }
    if (!formIsValid) {
      setErrorModal({
        title: "Invalid Submission",
        message: "Please enter data",
      });
      return;
    }

    console.log("Submitted! signup");
    console.log(nameValue, addressValue, emailValue, pswValue, value);
    let url;
    if (value === "Customer") {
      url = "http://localhost:8080/api/users/";
    } else if (value === "Provider") {
      url = "http://localhost:8080/api/providers/";
    }
    fetch(url, {
      method: "POST",
      body: JSON.stringify({
        email: emailValue,
        password: pswValue,
        address: addressValue,
        name: nameValue,
      }),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then(async (res) => {
        if(res.status >= 400) {
          const data = await res.json();
          throw new Error(data.message);
      }
      return res.json();
    
      })
      .then((res) => {
        switchAuthModeHandler();
      })
      .catch((err) => {
        setErrorModal({
          title: "Invalid Request",
          message: err.message,
        });
      });
  };

  return (
    <div className={classes.auth}>
      {errorModal && (
        <ErrorModal
          title={errorModal.title}
          message={errorModal.message}
          onConfirm={errorHandler}
        />
      )}
      <h1>Sign Up<link href="https://fonts.googleapis.com/css2?family=Pushster&display=swap" rel="stylesheet"></link></h1>
      <form onSubmit={submitHandler}>
        <div>
          <div
            className={`${classes.control} ${nameHasError && classes.invalid}`}
          >
            <label htmlFor="name">Full Name</label>
            <input
                placeholder="Enter your name"
              type="name"
              id="name"
              required
              onChange={nameChangeHandler}
              value={nameValue}
              onBlur={nameTouchHandler}
            />
          </div>
          {nameHasError && (
            <Alert
              severity="error"
              variant="outlined"
              sx={{ color: "#ff1744" }}
            >
              Please Enter a name
            </Alert>
          )}
        </div>

        <div>
          <div
            className={`${classes.control} ${
              addressHasError && classes.invalid
            }`}
          >
            <label htmlFor="text">Address</label>
            <input
                placeholder="Enter your address"
              type="text"
              id="text"
              required
              onChange={addressChangeHandler}
              value={addressValue}
              onBlur={addressTouchHandler}
            />
          </div>
          {addressHasError && (
            <Alert
              severity="error"
              variant="outlined"
              sx={{ color: "#ff1744" }}
            >
              Please Enter an Address
            </Alert>
          )}
        </div>

        <div>
          <div
            className={`${classes.control} ${emailHasError && classes.invalid}`}
          >
            <label htmlFor="email">Email</label>
            <input
                placeholder="Enter your email"
              type="email"
              id="email"
              required
              onChange={emailChangeHandler}
              value={emailValue}
              onBlur={emailTouchHandler}
            />
          </div>
          {emailHasError && (
            <Alert
              severity="error"
              variant="outlined"
              sx={{ color: "#ff1744" }}
            >
              Please enter a Valid email.
            </Alert>
          )}
        </div>
        <div>
          <div
            className={`${classes.control} ${pswHasError && classes.invalid}`}
          >
            <label htmlFor="password">Password</label>
            <input
                placeholder="Create a strong password"
              type="password"
              id="password"
              required
              onChange={pswChangeHandler}
              value={pswValue}
              onBlur={pswTouchHandler}
            />
          </div>
          {pswHasError && (
            <Alert
              severity="error"
              variant="outlined"
              sx={{ color: "#ff1744" }}
            >
              Please enter a password with length between 6 and 20 {`&`} atleast
              1 digit {`&`} Uppercase letter {`& `}
              Lowercase letter.
            </Alert>
          )}
        </div>

        <FormControl component="fieldset" error={error}>
          <RadioGroup
            row
            aria-label="type"
            name="controlled-radio-buttons-group"
            value={value}
            onChange={radioHandler}
          >
            <FormControlLabel
              value="Customer"
              control={
                <Radio
                  sx={{
                    "& .MuiSvgIcon-root": {
                      fontSize: 20,
                    },
                    color: cyan[800],
                    "&.Mui-checked": {
                      color: cyan[600],
                    },
                  }}
                />
              }
              label="Customer"
            />
            <FormControlLabel
              value="Provider"
              control={
                <Radio
                  sx={{
                    "& .MuiSvgIcon-root": {
                      fontSize: 20,
                    },
                    color: cyan[800],
                    "&.Mui-checked": {
                      color: cyan[600],
                    },
                  }}
                />
              }
              label="Provider"
            />
          </RadioGroup>
          {error && (
            <Alert
              severity="error"
              variant="outlined"
              sx={{ color: "#ff1744" }}
            >
              {helperText}
            </Alert>
          )}
        </FormControl>

        <div className={classes.actions}>
          <button>Create Account</button>
          <button
            type="button"
            className={classes.toggle}
            onClick={switchAuthModeHandler}
          >
            Login with existing account
          </button>
        </div>
      </form>
    </div>
  );
};

export default Signup;
