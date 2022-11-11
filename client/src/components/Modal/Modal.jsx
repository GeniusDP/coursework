import React from "react";
import "./modal-styles.css";

const Modal = ({ children, visible, setVisible }) => {
  return (
    <>
      {visible && (
        <div className="modal-wrapper">
          <div className="modal-content">
            <div>{children}</div>
            <button
              className="btn btn-danger custom-button"
              onClick={setVisible}
            >
              Close
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default Modal;
