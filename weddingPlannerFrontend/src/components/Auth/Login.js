import {useState, useContext} from "react";
import {useHistory} from "react-router-dom";
import AuthContext from "../context/auth-context";
import useInputReducer from "../custom-hook/useInputReducer";
import Alert from "@mui/material/Alert";
import classes from "./Forms.module.css";
import ErrorModal from "../Modal/ErrorModal";
import {cyan} from "@mui/material/colors";
import Radio from "@mui/material/Radio";
import RadioGroup from "@mui/material/RadioGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import FormControl from "@mui/material/FormControl";

const Login = () => {
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

    const history = useHistory();
    let formIsValid = false;

    if (pswIsValid && emailIsValid && value !== "") {
        formIsValid = true;
    }

    const ctx = useContext(AuthContext);

    const switchAuthModeHandler = () => {
        resetEmail();
        resetPsw();
        setValue("");
        setHelperText("");
        setError(false);
        history.replace("/signup");
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
        message: "Please enter valid data",
      });
      return;
    }

        console.log("Submitted! login");
        console.log(emailValue, pswValue);
        let route = ""
        let url;
        if (value === "Customer") {
            url = "http://localhost:8080/api/users/login";
            route = "/profile";
        } else if (value === "Provider") {
            url = "http://localhost:8080/api/providers/login";
            route = "/providerProfile";
        }
        fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": 'Basic ' + window.btoa(emailValue + ':' + pswValue)
            },
        })
            .then(async (res) => {
                if(res.status >= 400) {
                    const data = await res.json();
                    throw new Error(data.message);
                }
                return res.json();
              
            })
            .then((data) => {
                resetEmail();
                resetPsw();
                const sessionTime = new Date(new Date().getTime() + 3600 * 1000);
                ctx.login(data.token, sessionTime.toISOString(), value);
                history.replace(route);
            })
            .catch((err) => {
                setErrorModal({
                    title: "Invalid Request",
                    message: err.message,
                });
            });
    };

    return (
        <div className={classes.auth} >

            {errorModal && (
                <ErrorModal
                    title={errorModal.title}
                    message={errorModal.message}
                    onConfirm={errorHandler}
                />
            )}


            <h1>Login<link href="https://fonts.googleapis.com/css2?family=Pushster&display=swap" rel="stylesheet"></link> </h1>


            <form onSubmit={submitHandler}>
                <div>
                    <div
                        className={`${classes.control} ${emailHasError && classes.invalid}`}
                    >
                        <label htmlFor="email">Email</label>
                        <input placeholder="Enter your email"
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
                            sx={{color: "#ff1744"}}
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
                        <input placeholder="Enter your password"
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
                            sx={{color: "#ff1744"}}
                        >
                            Please enter a Correct Password Format
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
                            sx={{color: "#ff1744"}}
                        >
                            {helperText}
                        </Alert>
                    )}
                </FormControl>
                <div className={classes.actions}>
                    <button>Login</button>
                    <button
                        type="button"
                        className={classes.toggle}
                        onClick={switchAuthModeHandler}
                    >
                        Create new account
                    </button>
                </div>
            </form>
        </div>
);
};

export default Login;
