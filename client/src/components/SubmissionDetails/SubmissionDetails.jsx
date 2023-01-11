import React, {useContext, useEffect, useState} from 'react';
import {Navigate, useParams} from "react-router-dom";
import {AuthContext} from "../../App";
import CustomAppBar from "../CustomAppBar/CustomAppBar";
import {Spinner} from "../Spinner/Spinner";
import {Typography} from "@material-ui/core";
import {Card, CardContent, Container} from "@mui/material";
import {parseJwt} from "../../jwtUtil";

const SubmissionDetails = () => {

  const [submission, setSubmission] = useState(null);
  const [spinner, setSpinner] = useState(true);

  const {submissionId} = useParams();

  const {getAccessToken} = useContext(AuthContext);

  const jwtPayload = parseJwt(getAccessToken());
  const username = jwtPayload?.sub;

  useEffect(()=>{
    const options = {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${getAccessToken()}`
      }
    };
    setSpinner(true);
    const url = process.env.REACT_APP_CHECK_SERVICE_URL + `/my-submissions/${submissionId}`;
    fetch(url, options)
    .then(response => response.json())
    .then(object => setSubmission(object))
    .then(response => console.log(response))
    .catch(err => console.error(err))
    .finally(() => setSpinner(false))
  }, []);

  if(spinner){
    return <Spinner/>;
  }
  if(submission) {
    const s = submission;
    const testsScore = s.testsPassed / s.testsRun * s.task.testPoints;
    const dto = {
      id: s.id,
      compilationStatus: s.compilationStatus,
      runtimeStatus: s.runtimeStatus,
      totalScore: s.totalScore == null ? "N/A" : s.totalScore,
      testsScore: s.testsRun === -1 ? "N/A" : (testsScore + "/"
          + s.task.testPoints),
    };

    const getPmdScore = (solution) => {
      if (solution.task.pmdNeeded) {
        if (solution.pmdReportEntity == null) {
          return "N/A";
        }
        if (solution.pmdReportEntity.pmdReportSourceFiles.length === 0) {
          return solution.task.pmdPoints + "/" + solution.task.pmdPoints;
        }
        return 0 + "/" + solution.task.pmdPoints;
      }
      return "-";
    };

    const getCheckstyleScore = (solution) => {
      if (solution.task.checkstyleNeeded) {
        if (solution.checkstyleReportEntity == null) {
          return "N/A";
        }
        if (solution.checkstyleReportEntity.checkstyleSourceFile.length === 0) {
          return solution.task.checkstylePoints + "/"
              + solution.task.checkstylePoints;
        }
        return 0 + "/" + solution.task.checkstylePoints;
      }
      return "-";
    };

    dto.pmdScore = getPmdScore(s);
    dto.checkstyleScore = getCheckstyleScore(s);
    return (
        <>
          <CustomAppBar/>
          <Container style={{marginTop: 30}}>
            <table className="table table-light">
              <thead className="thead-dark">
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Compilation status</th>
                <th scope="col">Runtime status</th>
                <th scope="col">Total score</th>
                <th scope="col">Tests score</th>
                <th scope="col">PMD score</th>
                <th scope="col">Checkstyle score</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <th scope="col">{dto.id}</th>
                <th scope="col">{dto.compilationStatus}</th>
                <th scope="col">{dto.runtimeStatus}</th>
                <th scope="col">{dto.totalScore}</th>
                <th scope="col">{dto.testsScore}</th>
                <th scope="col">{dto.pmdScore}</th>
                <th scope="col">{dto.checkstyleScore}</th>
              </tr>
              </tbody>
            </table>
            {
                submission.pmdReportEntity &&
                <Typography variant={"h4"} align={"center"}>
                  Pmd warnings
                </Typography>
            }
            {
                submission.pmdReportEntity && submission.pmdReportEntity.pmdReportSourceFiles.length === 0 &&
                <Typography variant={"h6"} align={"center"}>
                  no warnings
                </Typography>
            }
            {
              submission.pmdReportEntity &&
                submission.pmdReportEntity.pmdReportSourceFiles
                .map(file => <Card key={file.id}>
                  <CardContent>
                    <Typography
                        variant={"h6"}
                        color="primary"
                        gutterBottom
                    >
                      <u>File name: {file.name}</u>
                    </Typography>
                    {
                      file.violations.map(violation => <Card key={violation.id}>
                        <CardContent>
                          <Typography
                              color="secondary"
                              gutterBottom
                          >
                            {violation.value}
                          </Typography>
                          <Typography
                              color="secondary"
                              gutterBottom
                          >
                            <u>Begin line: {violation.beginLine}</u>
                          </Typography>
                          <Typography
                              color="secondary"
                              gutterBottom
                          >
                            <u>End line: {violation.endLine}</u>
                          </Typography>

                        </CardContent>
                      </Card>)
                    }
                  </CardContent>
                </Card>)
            }
            {
                submission.checkstyleReportEntity &&
                <Typography variant={"h4"} align={"center"}>
                  Checkstyle warnings
                </Typography>
            }
            {
                submission.checkstyleReportEntity && submission.checkstyleReportEntity.checkstyleSourceFile.length === 0 &&
                <Typography variant={"h6"} align={"center"}>
                  no warnings
                </Typography>
            }
            {
                submission.checkstyleReportEntity &&
                submission.checkstyleReportEntity.checkstyleSourceFile
                .map(file => <Card key={file.id}>
                  <CardContent>
                    <Typography
                        variant={"h6"}
                        color="primary"
                        gutterBottom
                    >
                      <u>File name: {file.name}</u>
                    </Typography>
                    {
                      file.errors.map(error => <Card key={error.id}>
                        <CardContent>
                          <Typography
                              color="secondary"
                              gutterBottom
                          >
                            {error.message}
                          </Typography>
                          <Typography
                              color="secondary"
                              gutterBottom
                          >
                            <u>Line: {error.line}</u>
                          </Typography>
                        </CardContent>
                      </Card>)
                    }
                  </CardContent>
                </Card>)
            }

          </Container>

        </>
    );
  }
};

export default SubmissionDetails;
