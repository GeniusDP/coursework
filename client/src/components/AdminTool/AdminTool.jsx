import React, { useContext, useState, useEffect, useCallback } from 'react';
import CustomAppBar from "../CustomAppBar/CustomAppBar";
import { Spinner } from "../Spinner/Spinner";
import {
  Button,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Select
} from "@mui/material";
import { AuthContext } from "../../App";
import authRequestWrapper from "../../utils/authRequestWrapper.js";

const AdminTool = () => {
  const { getAccessToken } = useContext(AuthContext);

  const [requests, setRequests] = useState(null);
  const [spinnerVisible, setSpinnerVisible] = useState(true);
  const [operationDidPerform, setOperationDidPerform] = useState(true);

  useEffect(() => {
    authRequestWrapper(async () => {
      setSpinnerVisible(true);
      const options = {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${getAccessToken()}`
        }
      };
      const url = process.env.REACT_APP_USER_MANAGEMENT_URL + `/requests-for-teacher-grant/get-all`;
      (async () => {
        try {
          const response = await fetch(url, options);
          const array = await response.json();
          console.log(array);
          setRequests(array);
        } catch (e) {
          window.alert("Some error occured.\nOperation did not finish successfully.");
        } finally {
          setSpinnerVisible(false);
        }
      })();
    });
  }, [operationDidPerform]);

  async function handleAccept(requestId) {
    authRequestWrapper(async () => {
      setSpinnerVisible(true);
      const options = { 
        method: 'POST',
        headers: {
          Authorization: `Bearer ${getAccessToken()}`
        } 
      };
      const url = process.env.REACT_APP_USER_MANAGEMENT_URL + `/requests-for-teacher-grant/accept/${requestId}`;
      try {
        const response = await fetch(url, options);
        if (response.ok) {
          window.alert("Successfully accepted request");
        }
      } catch (e) {
        window.alert("Some error occured.\nOperation did not finish successfully.");
      } finally {
        setSpinnerVisible(false);
        setOperationDidPerform((value) => !value);
      }
    });
  }

  async function handleReject(requestId) {
    authRequestWrapper(async () => {
      setSpinnerVisible(true);
      const options = { 
        method: 'POST',
        headers: {
          Authorization: `Bearer ${getAccessToken()}`
        } 
      };
      const url = process.env.REACT_APP_USER_MANAGEMENT_URL + `/requests-for-teacher-grant/reject/${requestId}`;
      try {
        const response = await fetch(url, options);
        if (response.ok) {
          window.alert("Successfully rejected request");
        }
      } catch (e) {
        window.alert("Some error occured.\nOperation did not finish successfully.");
      } finally {
        setSpinnerVisible(false);
        setOperationDidPerform((value) => !value);
      }
    });
  }

  if (spinnerVisible) {
    return <Spinner />;
  }

  return (
    <>
      <CustomAppBar />
      <Container style={{ marginTop: 20 }}>
        <table className="table table-light">
          <thead className="thead-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">username</th>
              <th scope="col">ACCCEPT</th>
              <th scope="col">REJECT</th>
            </tr>
          </thead>
          <tbody>
            {
              requests && requests.map(req => {
                return (
                  <tr key={req.id}>
                    <th scope="col">{req.id}</th>
                    <th scope="col">{req.username}</th>
                    <th scope="col">
                      <Button 
                        variant={"contained"} 
                        style={{backgroundColor: "green", color: "whitesmoke"}}
                        onClick={() => handleAccept(req.id)}
                      >
                          Accept
                      </Button>
                    </th>
                    <th scope="col">
                      <Button 
                        variant={"contained"} 
                        style={{backgroundColor: "red", color: "whitesmoke"}}
                        onClick={() => handleReject(req.id)}
                      >
                        Reject
                      </Button>
                    </th>
                  </tr>
                )
              })
            }
          </tbody>
        </table>
      </Container>
    </>
  );
};

export default AdminTool;