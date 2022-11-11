import { useEffect, useState } from "react";

const validateEmail = (email) => {
  return String(email)
    .toLowerCase()
    .match(
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    );
};

const useValidation = (value, validations) => {
  const [isEmpty, setIsEmpty] = useState(true);
  const [minLengthError, setMinLengthError] = useState(false);
  const [maxLengthError, setMaxLengthError] = useState(false);
  const [emailError, setEmailError] = useState(false);

  useEffect(() => {
    console.log(value);
    for (const validation in validations) {
      switch (validation) {
        case "minLength":
          setMinLengthError(value.length < validations[validation]);
          break;
        case "isEmpty":
          setIsEmpty(!value);
          break;
        case "maxLength":
          setMaxLengthError(value.length > validations[validation]);
          break;
        case "isValidEmail":
          setEmailError(!validateEmail(value));
          break;
        default:
          throw new Error("unknown validation exception");
      }
    }
  }, [value]);
  return {
    isEmpty,
    minLengthError,
    maxLengthError,
    emailError,
    isFullValid: () => {
      return !isEmpty && !minLengthError && !maxLengthError && !emailError;
    },
  };
};

export default useValidation;
