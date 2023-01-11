import React, {useEffect, useState} from 'react';
import {Link, Navigate} from "react-router-dom";
import {Spinner} from '../Spinner/Spinner';
import CustomAppBar from "../CustomAppBar/CustomAppBar";
import {Button, Card, CardActions, CardContent, Container} from "@mui/material";
import {Typography} from "@material-ui/core";

const AllTasksPage = () => {
  const [tasks, setTasks] = useState(null);
  const [spinnerVisible, setSpinnerVisible] = useState(true);
  const [fetchError, setFetchError] = useState(false);

  useEffect(() => {
    setSpinnerVisible(true);
    const url = process.env.REACT_APP_TASK_MANAGEMENT_URL + "/tasks";
    (async () => {
      try {
        const response = await fetch(url);
        const json = await response.json();
        setTasks(json);
      } catch (e) {
        alert(e);
        setFetchError(true);
      } finally {
        setSpinnerVisible(false);
      }
    })();

  }, []);

  if (fetchError) {
    return <Navigate to="/"/>
  }

  if (spinnerVisible) {
    return <Spinner/>;
  }

  return (
      <>
        <CustomAppBar/>
        <Container>
          {
            tasks.map(task => (
                    <Card style={{marginTop: 20}} key={task.id} sx={{minWidth: 275}}>
                      <CardContent>
                        <Typography
                            variant={"h5"}
                            color="primary"
                            gutterBottom
                        >
                          {task.name}
                        </Typography>
                        <Typography
                            variant={"h6"}
                            color="primary"
                        >
                          Created by: {task.creatorName}
                        </Typography>
                      </CardContent>
                      <CardActions>
                        <Button
                            color="primary"
                            variant={"outlined"}
                            component={Link}
                            to={`/all-tasks/task/${task.id}`}
                        >Go to task</Button>
                      </CardActions>
                    </Card>
                )
            )
          }
        </Container>
      </>
  );
};

export default AllTasksPage;