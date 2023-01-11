import React, { useContext, useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../../App';
import { Spinner } from '../Spinner/Spinner';
import { Button } from "@mui/material";
import authRequestWrapper from "../../utils/authRequestWrapper.js";

const MySubmissions = ({ taskId }) => {
  const [submissions, setSubmissions] = useState([]);
  const [spinnerVisible, setSpinnerVisible] = useState(true);
  const [unauthorized, setUnauthorized] = useState(false);
  const [goToDetails, setGoToDetails] = useState(null);

  const { getAccessToken } = useContext(AuthContext);

  useEffect(() => {
    (async() => {
      const result = await authRequestWrapper(async () => {
        const options = {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${getAccessToken()}`
          }
        };
        const url = process.env.REACT_APP_CHECK_SERVICE_URL + `/tasks/${taskId}/my-submissions`;
        setSpinnerVisible(true);
        try {
          (async () => {
            const response = await fetch(url, options);
            setSubmissions(await response.json());
          })();
        } catch (e) {
          alert("Error occured!");
        } finally {
          setSpinnerVisible(false);
        }
      });
      if (!result) {
        setUnauthorized(true);
      }
    })();
  }, []);

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

  if (unauthorized) {
    return <Navigate to="/login" />;
  }

  if (goToDetails != null) {
    return (
      <Navigate to={`/all-tasks/task/${taskId}/my-submissions/${goToDetails.id}`} />
    );
  }

  return (
    <>
      {spinnerVisible && <Spinner />}
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
            <th scope="col">Details</th>
          </tr>
        </thead>
        <tbody>
          {
            submissions.map(s => {
              const testsScore = s.testsPassed / s.testsRun * s.task.testPoints;
              const dto = {
                id: s.id,
                compilationStatus: s.compilationStatus,
                runtimeStatus: s.runtimeStatus,
                totalScore: s.totalScore == null ? "N/A" : s.totalScore,
                testsScore: s.testsRun === -1 ? "N/A" : (testsScore + "/"
                  + s.task.testPoints),
              };

              dto.pmdScore = getPmdScore(s);
              dto.checkstyleScore = getCheckstyleScore(s);
              return (
                <tr key={s.id}>
                  <th scope="col">{dto.id}</th>
                  <th scope="col">{dto.compilationStatus}</th>
                  <th scope="col">{dto.runtimeStatus}</th>
                  <th scope="col">{dto.totalScore}</th>
                  <th scope="col">{dto.testsScore}</th>
                  <th scope="col">{dto.pmdScore}</th>
                  <th scope="col">{dto.checkstyleScore}</th>
                  <th scope="col">
                    <Button onClick={() => {
                      setGoToDetails(s)
                    }}>
                      Details
                    </Button>
                  </th>
                </tr>
              )
            })
          }
        </tbody>
      </table>
    </>
  );
};

export default MySubmissions;