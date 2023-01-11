import React, { useContext, useEffect, useState } from 'react';
import { Navigate, useParams } from 'react-router-dom';
import { AuthContext } from '../../App';
import MySubmissions from '../MySubmissions/MySubmissions';
import { Spinner } from '../Spinner/Spinner';
import CustomAppBar from "../CustomAppBar/CustomAppBar";
import { Button, ButtonGroup, Container, Input } from "@mui/material";
import { Typography } from "@material-ui/core";
import { parseJwt } from '../../jwtUtil';
import authRequestWrapper from "../../utils/authRequestWrapper.js";


const TaskElement = () => {
  const [task, setTask] = useState(null);
  const [file, setFile] = useState(null);
  const [taskNotFound, setTaskNotFound] = useState(false);
  const [spinnerVisible, setSpinnerVisible] = useState(true);
  const [fetchError, setFetchError] = useState(false);
  const [unauthorized, setUnauthorized] = useState(false);
  const [showStats, setShowStats] = useState(false);
  const [submissionsLeft, setSubmissionsLeft] = useState(null);
  const [openChangeDialog, setOpenChangeDialog] = useState(false);


  let { id } = useParams();
  const { getAccessToken } = useContext(AuthContext);
  
  const jwtPayload = parseJwt(getAccessToken());
  const username = jwtPayload?.sub;
  const role = jwtPayload?.role;

  useEffect(() => {
    fetchTaskDetails();
  }, []);

  useEffect(() => {
    if (task) {
      fetchSubmissionsLeft();
    }
  }, []);


  // fetches task details
  async function fetchTaskDetails() {
    setSpinnerVisible(true);
    const url = process.env.REACT_APP_TASK_MANAGEMENT_URL + `/tasks/${id}`;
    (async () => {
      try {
        const response = await fetch(url);
        if (response.ok) {
          setTask(await response.json());
        } else {
          setTaskNotFound(true);
        }
      } catch (e) {
        alert(e);
        setFetchError(true);
      } finally {
        setSpinnerVisible(false);
        if(username){
          fetchSubmissionsLeft();
        }
      }
    })();
  }

  async function fetchSubmissionsLeft() {
    const result = await authRequestWrapper(async () => {
      const options = {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${getAccessToken()}`
        }
      };

      fetch(process.env.REACT_APP_CHECK_SERVICE_URL + `/tasks/${id}/submissions-left`, options)
        .then(response => response.json())
        .then(number => {
          const { value } = number;
          setSubmissionsLeft(value);
          return number;
        })
        .then(response => console.log(response))
        .catch(err => console.error(err));
    });
    if (!result) {
      setUnauthorized(true);
    }
  }

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]);
    }
  };


  const handleSubmit = async () => {
    if (!file) {
      return;
    }
    await authRequestWrapper(async () => {
      // 👇 Uploading the file using the fetch API to the server
      const url = process.env.REACT_APP_CHECK_SERVICE_URL + `/tasks/${id}/check-solution`;
      const form = new FormData();
      form.append("file", file);

      const options = {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${getAccessToken()}`
        }
      };

      options.body = form;
      setSpinnerVisible(true);
      try {
        const response = await fetch(url, options);
        if (response.ok) {
          const submissionReport = await response.json();
          alert("Your score is: " + submissionReport["totalScore"]);
        } else if (response.status === 400) {
          alert("Submission did not finish successfully");
        } else if (response.status === 429) {
          const submissionReport = await response.json();
          const timeLast = submissionReport.retryAfterSeconds;
          alert(`Too many requests. Wait for ${timeLast} seconds to make another submission`);
        }
      } catch (e) {
        alert("Some error occured");
        console.log(e);
      } finally {
        setSpinnerVisible(false);
        fetchSubmissionsLeft();
        setFile(null);
      }
    });
  };

  const handleDownloadSources = async () => {
    fetch(process.env.REACT_APP_TASK_MANAGEMENT_URL + `/tasks/${id}/sources`)
      .then(response => {
        response.blob().then(blob => {
          let url = window.URL.createObjectURL(blob);
          let a = document.createElement('a');
          a.href = url;
          a.download = 'sources';
          a.click();
        });
      });
  }

  const handleDeleteTask = async () => {
    const result = authRequestWrapper(async () => {
      const reallyDelete = window.confirm("Do you really want to DELETE this task?");
      if (reallyDelete) {
        const options = {
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${getAccessToken()}`
          }
        };

        await fetch(process.env.REACT_APP_TASK_MANAGEMENT_URL + `/tasks/${id}`, options);
        setTaskNotFound(true);
      }
    });
    if (!result) {
      setUnauthorized(true);
    }
  }

  const handleChangeTask = async () => {
    setOpenChangeDialog(true);
  }

  const handleShowStats = () => {
    setShowStats(true);
  }

  if (fetchError) {
    return <Navigate to="/" />
  }

  if (taskNotFound) {
    return <Navigate to={"/all-tasks"} />;
  }

  if (spinnerVisible) {
    return <Spinner />;
  }

  if (unauthorized) {
    return <Navigate to="/login" />;
  }

  if (showStats) {
    return <Navigate to={`/all-tasks/tasks/${id}/stats`} />
  }

  if (openChangeDialog) {
    return <Navigate to={`/all-tasks/tasks/${id}/change-task`} />
  }


  return (
    <>
      <CustomAppBar />
      <Container>
        {!username && <Typography variant={"h6"} style={{ textAlign: "center", color: "tomato" }}>
          You have to be logged in to submit solutions!
        </Typography>}
        <Typography variant={"h4"}
          style={{ textAlign: "center", paddingTop: "20px" }}>
          {task.name}
        </Typography>
        <Typography variant={"h6"}
          style={{ textAlign: "justify", paddingBottom: "20px" }}>
          {task.description}
        </Typography>
        <div>
          <label htmlFor={"solution_upload"}>
            <Typography variant={"h6"}>
              Upload your solution:
            </Typography>
          </label>
          <Input
            id={"solution_upload"}
            type={"file"}
            onChange={handleFileChange}
          />
        </div>
        <div style={{ display: "flex", flexDirection: "row" }}>
          <ButtonGroup>
            <Button style={{
              backgroundColor: "#37b621",
              fontSize: "15px"
            }} variant={"contained"} onClick={handleSubmit} disabled={submissionsLeft === 0 || !username}>Submit</Button>
            <Button style={{
              backgroundColor: "#FFCC00",
              fontSize: "15px"
            }} variant={"contained"} onClick={handleDownloadSources}>Download sources</Button>
            <Button style={{
              backgroundColor: "#FF6600",
              fontSize: "15px"
            }} variant={"contained"} onClick={handleShowStats}>Show stats</Button>
            {username === task.creatorName && (role === "TEACHER") && <Button style={{
              backgroundColor: "#ff00e1",
              fontSize: "15px"
            }} variant={"contained"} onClick={handleChangeTask}>Change task</Button>}
            {username === task.creatorName && (role === "TEACHER") && <Button style={{
              backgroundColor: "red",
              fontSize: "15px"
            }} variant={"contained"} onClick={handleDeleteTask}>Delete task</Button>}
          </ButtonGroup>
          {username && <Typography variant="h6" color="primary">
            Submissions left: {submissionsLeft === 1_000_000_000 ? "No limits" : submissionsLeft}
          </Typography>}
        </div>
        {username && <MySubmissions taskId={id} />}
      </Container>
    </>
  );
};

export default TaskElement;