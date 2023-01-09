import React, { PureComponent, useEffect, useState } from 'react';
import {
  ComposedChart,
  Line,
  Area,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';

const DistributionTotalScoresBarChart = ({taskId}) => {
  const [data, setData] = useState({});
  
  useEffect(() => {
    const url = process.env.REACT_APP_CHECK_SERVICE_URL + `/stats/task/${taskId}/distribution-of-totalscores`;
    (async function() {
      const response = await fetch(url);
      let rawData = await response.json();
      const data1 = [
        {
          name: '0-20',
          submissions: rawData.from0Till20,
          all: rawData.countAll,
        },
        {
          name: '21-40',
          submissions: rawData.from21Till40,
          all: rawData.countAll,
        },
        {
          name: '41-60',
          submissions: rawData.from41Till60,
          all: rawData.countAll
        },
        {
          name: '61-80',
          submissions: rawData.from61Till80,
          all: rawData.countAll
        },
        {
          name: '81-100',
          submissions: rawData.from81Till100,
          all: rawData.countAll
        }
      ];
      setData(data1);
    })();
  }, []);

  return (
    <ResponsiveContainer width={400} height={400}>
      <ComposedChart
        width={500}
        height={400}
        data={data}
        margin={{
          top: 20,
          right: 20,
          bottom: 20,
          left: 20,
        }}
      >
        <CartesianGrid stroke="#f5f5f5" />
        <XAxis dataKey="name" scale="band" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Bar dataKey="submissions" barSize={20} fill="#413ea0" />
        <Line type="monotone" dataKey="submissions" stroke="#ff7300" />
      </ComposedChart>
    </ResponsiveContainer>
  );
};

export default DistributionTotalScoresBarChart;