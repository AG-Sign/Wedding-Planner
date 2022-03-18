import { useReducer } from 'react';

const initialState = {
  value: '',
  isTouched: false,
};

const inputReducer = (state, action) => {
  if (action.type === 'INPUT') {
    return { value: action.value, isTouched: state.isTouched };
  }
  if (action.type === 'TOUCH') {
    return { isTouched: true, value: state.value };
  }
  if (action.type === 'RESET') {
    return initialState;
  }
  return inputReducer;
};

const useInputReducer = (validateValue) => {
  const [inputState, perform] = useReducer(
    inputReducer,
    initialState
  );

  const valueIsValid = validateValue(inputState.value);
  const hasError = !valueIsValid && inputState.isTouched;

  const valueChangeHandler = (event) => {
    perform({ type: 'INPUT', value: event.target.value });
  };

  const inputTouchHandler = (event) => {
    perform({ type: 'TOUCH' });
  };

  const reset = () => {
    perform({ type: 'RESET' });
  };

  return {
    value: inputState.value,
    isValid: valueIsValid,
    hasError,
    valueChangeHandler,
    inputTouchHandler,
    reset,
  };
};

export default useInputReducer;