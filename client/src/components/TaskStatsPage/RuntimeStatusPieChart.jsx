import React, { useState, useEffect } from 'react';
import { PieChart, Pie, Sector, ResponsiveContainer } from 'recharts';
import "react-circular-progressbar/dist/styles.css";
import { Box, Typography } from '@material-ui/core';

const renderActiveShape = (props) => {
  const RADIAN = Math.PI / 180;
  const { cx, cy, midAngle, innerRadius, outerRadius, startAngle, endAngle, fill, payload, percent, value } = props;
  const sin = Math.sin(-RADIAN * midAngle);
  const cos = Math.cos(-RADIAN * midAngle);
  const sx = cx + (outerRadius + 10) * cos;
  const sy = cy + (outerRadius + 10) * sin;
  const mx = cx + (outerRadius + 30) * cos;
  const my = cy + (outerRadius + 30) * sin;
  const ex = mx + (cos >= 0 ? 1 : -1) * 22;
  const ey = my;
  const textAnchor = cos >= 0 ? 'start' : 'end';

  return (
    <g>
      <text x={cx} y={cy} dy={8} textAnchor="middle" fill={fill}>
        {payload.name}
      </text>
      <Sector
        cx={cx}
        cy={cy}
        innerRadius={innerRadius}
        outerRadius={outerRadius}
        startAngle={startAngle}
        endAngle={endAngle}
        fill={fill}
      />
      <Sector
        cx={cx}
        cy={cy}
        startAngle={startAngle}
        endAngle={endAngle}
        innerRadius={outerRadius + 6}
        outerRadius={outerRadius + 10}
        fill={fill}
      />
      <path d={`M${sx},${sy}L${mx},${my}L${ex},${ey}`} stroke={fill} fill="none" />
      <circle cx={ex} cy={ey} r={2} fill={fill} stroke="none" />
      <text x={ex + (cos >= 0 ? 1 : -1) * 12} y={ey} textAnchor={textAnchor} fill="#333">{`PV ${value}`}</text>
      <text x={ex + (cos >= 0 ? 1 : -1) * 12} y={ey} dy={18} textAnchor={textAnchor} fill="#999">
        {`(Rate ${(percent * 100).toFixed(2)}%)`}
      </text>
    </g>
  );
};
const RuntimeStatusPieChart = ({taskId}) => {

  const [activeIndex, setActiveIndex] = useState(0);
  const [data, setData] = useState([]);

  const onPieEnter = (_, index) => {
    setActiveIndex(index)
  };


  useEffect(() => {
    const url = process.env.REACT_APP_CHECK_SERVICE_URL + `/stats/task/${taskId}/runtime-statuses-in-groups`;
    (async function() {
      const response = await fetch(url);
      let rawData = await response.json();
      rawData = rawData.map(d => {
        return {name: d.status, value: d.count}
      });
      setData(rawData);
    })();

  }, []);
  
  
  let sum = 0;
  for(let x of data){
    sum += x.value;
  }
  return (
    <>
      {sum > 0 && <ResponsiveContainer width={600} height={400}>
        <PieChart width={400}>
          <Pie
            activeIndex={activeIndex}
            activeShape={renderActiveShape}
            data={data}
            cx="50%"
            cy="50%"
            innerRadius={100}
            outerRadius={120}
            fill="#8884d8"
            dataKey="value"
            onMouseEnter={onPieEnter}
          />
        </PieChart>
      </ResponsiveContainer>}
      {sum === 0 && <Box width={400} style={{display: "flex", justifyContent: "center", alignItems: "center"}}>
        <Typography variant='h5' style={{textAlign: "center"}}>No stats yet</Typography>
      </Box>}
    </>
  );

}

export default RuntimeStatusPieChart;