import React from "react";
import "./modal-styles.css";

const Modal = ({ children, visible, onClose }) => {
  return (
    <>
      {visible && (
        <div className="modal-wrapper">
          <div className="modal-content">
            <div>{children}</div>
            <button
              className="btn btn-danger custom-button"
              onClick={onClose}
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
