import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { CartesianGrid, Legend, Line, LineChart, XAxis, YAxis } from 'recharts';
import CustomAppBar from '../CustomAppBar/CustomAppBar';
import DistributionTotalScoresBarChart from './DistributionTotalScoresBarChart';
import NumberCircleBar from './NumberCircleBar';
import RuntimeStatusPieChart from './RuntimeStatusPieChart';

const TaskStatsPage = () => {

  const { taskId } = useParams();

  const [avgTotalScore, setAvgTotalScore] = useState(0);
  const [numberOfSubmissions, setNumberOfSubmissions] = useState(0);


  function roundTo2(value){
    const num = Number(value) // The Number() only visualizes the type and is not needed
    const roundedString = num.toFixed(2);
    return Number(roundedString); // toFixed() returns a string (often suitable for printing already)
  }

  async function fetchAvgTotalScore() {
    const options = { method: 'GET' };
    const url = process.env.REACT_APP_CHECK_SERVICE_URL + `/stats/task/${taskId}/average-totalscore`;
    const response = await fetch(url, options);
    const json = await response.json();
    setAvgTotalScore(roundTo2(json.value));
  }

  async function fetchNumberOfSubmissionsOfTask() {
    const options = { method: 'GET' };
    const url = process.env.REACT_APP_CHECK_SERVICE_URL + `/stats/task/${taskId}/number-of-submissions`;
    const response = await fetch(url, options);
    const json = await response.json();
    setNumberOfSubmissions(roundTo2(json.value));
  }

  useEffect(() => {
    (async () => {
      await fetchAvgTotalScore();
      await fetchNumberOfSubmissionsOfTask();
    })();
  }, []);


  return (
    <>
      <CustomAppBar />
      <div style={{ display: "flex", flexDirection: "row" }}>
        <RuntimeStatusPieChart taskId={taskId} />
        <DistributionTotalScoresBarChart taskId={taskId} />
        <NumberCircleBar value={avgTotalScore} description={"Avg totalscore"} />
      </div>
      <div style={{ display: "flex", flexDirection: "row" }}>
        <NumberCircleBar value={numberOfSubmissions} maxValue={numberOfSubmissions} description={"Number of submissions"} />
      </div>
    </>
  );
};

export default TaskStatsPage;