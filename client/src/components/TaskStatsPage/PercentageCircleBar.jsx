import { Typography } from '@material-ui/core';
import React from 'react';
import {
  CircularProgressbar,
  CircularProgressbarWithChildren,
  buildStyles
} from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";

const PercentageCircleBar = ({value, description}) => {
  return (
    <div style={{ width: 300, height: 250, display: "flex", justifyContent: "center", alignItems: "center" }}>
      <div style={{ width: 200, height: 200 }}>
        <CircularProgressbar
          value={value}
          text={`${value}%`}
          styles={buildStyles({
            strokeLinecap: "butt"
          })}
          strokeWidth={5}
        />
        <Typography style={{textAlign: "center"}} variant='h5' color='secondary'>{description}</Typography>
      </div>
    </div>);
};

export default PercentageCircleBar