import React from "react";

const CustomFormInput = ({ id, value, onChange, placeholder, label }) => {
  return (
    <div className="custom-input">
      <div className="label-wrapper">
        <label htmlFor={id}>{label}</label>
      </div>
      <input
        id={id}
        className={"form-control"}
        value={value}
        placeholder={placeholder}
        onChange={onChange}
      />
    </div>
  );
};

export default CustomFormInput;
