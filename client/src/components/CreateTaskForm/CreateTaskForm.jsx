import React, { useContext, useEffect, useState } from 'react';
import { Link, Navigate } from 'react-router-dom';
import { AuthContext } from '../../App';
import useInput from '../../hooks/useInput';
import CustomFormInput from "../CustomFormInput.jsx/CustomFormInput";
import { Spinner } from '../Spinner/Spinner';
import CustomAppBar from "../CustomAppBar/CustomAppBar";
import authRequestWrapper from "../../utils/authRequestWrapper.js";

const CreateTaskForm = () => {
  const { getAccessToken } = useContext(AuthContext);
  const name = useInput("", { isEmpty: true, minLength: 3, maxLength: 50 });
  const description = useInput("", { isEmpty: true, maxLength: 4000 });

  const [testsPoints, setTestsPoints] = useState(100);

  const [pmdNeeded, setPmdNeeded] = useState(false);
  const [submissionsNumberLimitEnabled, setSubmissionsNumberLimitEnabled] = useState(false);
  const [checkstyleNeeded, setCheckstyleNeeded] = useState(false);

  const [pmdPoints, setPmdPoints] = useState(0);
  const [checkstylePoints, setCheckstylePoints] = useState(0);
  const [submissionsNumberLimit, setSubmissionsNumberLimit] = useState(-1);

  const [taskZipFile, setTaskZipFile] = useState(null);
  const [testZipFile, setTeskZipFile] = useState(null);

  const [formValid, setFormValid] = useState(false);
  const [createdSuccessfully, setCreatedSuccessfully] = useState(false);
  const [requestError, setRequestError] = useState(false);

  const [spinnerVisible, setSpinnerVisible] = useState(false);

  const handleTaskZipFileChange = (e) => {
    if (e.target.files) {
      setTaskZipFile(e.target.files[0]);
    }
  };

  const handleTestZipFileChange = (e) => {
    if (e.target.files) {
      setTeskZipFile(e.target.files[0]);
    }
  };


  useEffect(() => {

    let sumScore = Number.parseInt(testsPoints);
    if (pmdNeeded) {
      sumScore += Number.parseInt(pmdPoints);
    }
    if (checkstyleNeeded) {
      sumScore += Number.parseInt(checkstylePoints);
    }
    setFormValid(
      name.isFullValid() &&
      description.isFullValid() &&
      sumScore === 100 &&
      taskZipFile && testZipFile
    );
  }, [name, description, pmdNeeded, pmdPoints, checkstyleNeeded, checkstylePoints, testsPoints, taskZipFile, testZipFile])

  const handleCreate = async () => {
    authRequestWrapper(async () => {
      const form = new FormData();
      form.append("name", name.value);
      form.append("description", description.value);
      form.append("sourceInZip", taskZipFile);
      form.append("testSourceInZip", testZipFile);
      form.append("checkstyleNeeded", checkstyleNeeded);
      form.append("pmdNeeded", pmdNeeded);
      form.append("pmdPoints", pmdPoints);
      form.append("checkstylePoints", checkstylePoints);
      form.append("testPoints", testsPoints);
      form.append("submissionsNumberLimit", submissionsNumberLimit);

      const options = {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${getAccessToken()}`
        }
      };

      options.body = form;
      const url = process.env.REACT_APP_TASK_MANAGEMENT_URL + '/tasks';
      setSpinnerVisible(true);
      try {
        const response = await fetch(url, options);
        if (response.ok) {
          setCreatedSuccessfully(true);
        } else {
          console.log("Status was: " + response.status);
          throw new Error("Status was: " + response.status);
        }
      } catch (e) {
        alert("Problem occured");
        setRequestError(true);
      } finally {
        setSpinnerVisible(false);
      }
    });

  }

  if (createdSuccessfully || requestError) {
    return <Navigate to="/all-tasks" />
  }

  if (spinnerVisible) {
    return <Spinner />;
  }

  return (
    <div>
      <CustomAppBar />
      <h3>Create task </h3>
      <CustomFormInput
        id={"input_name"}
        label={"Name"}
        onChange={name.onChange}
        onBlur={name.onBlur}
        placeholder={"name"}
        value={name.value}
      />
      {name.isDirty && name.isEmpty && (
        <div style={{ color: "red" }}>Name must not be empty</div>
      )}
      {name.isDirty &&
        (name.minLengthError || name.maxLengthError) && (
          <div style={{ color: "red" }}>
            Name size should be from 3 to 50
          </div>
        )}
      <div>
        <div><label htmlFor="input_description">Description</label></div>
        <textarea
          id="input_description"
          maxLength="4000"
          placeholder="description of task: maximum 4000 symbols"
          style={{ resize: "none", width: "700px", height: "300px" }}
          value={description.value}
          onChange={description.onChange}
          onBlur={description.onBlur}
        />
      </div>
      {description.isDirty && description.isEmpty && (
        <div style={{ color: "red" }}>Description must not be empty</div>
      )}
      {description.isDirty &&
        (description.minLengthError || description.maxLengthError) && (
          <div style={{ color: "red" }}>
            Description size should not be bigger than 4000
          </div>
        )}

      <div>
        <div><label htmlFor="task_source_zip_input">Task sources (zip archive)</label></div>
        <input
          id="task_source_zip_input"
          type="file"
          onChange={handleTaskZipFileChange}
        />
      </div>
      <div>
        <div><label htmlFor="test_source_zip_input">Test sources (zip archive)</label></div>
        <input
          id="test_source_zip_input"
          type="file"
          onChange={handleTestZipFileChange}
        />
      </div>

      <div>
        <div><label htmlFor="test_points_input">Tests points</label></div>
        <input
          id="tests_points_input"
          type="number"
          max={100}
          min={1}
          value={testsPoints}
          onChange={(e) => { setTestsPoints(e.target.value) }}
        />
      </div>

      <div>
        <div><label htmlFor="pmd_needed_input">PMD needed</label></div>
        <input
          id="pmd_needed_input"
          type="checkbox"
          value={pmdNeeded}
          onClick={(e) => { setPmdNeeded(e.target.checked) }}
        />
      </div>

      {pmdNeeded && <div>
        <div><label htmlFor="pmd_points_input">PMD points</label></div>
        <input
          id="pmd_points_input"
          type="number"
          max={100}
          min={1}
          value={pmdPoints}
          onChange={(e) => { setPmdPoints(e.target.value) }}
        />
      </div>}

      <div>
        <div><label htmlFor="checkstyle_needed_input">Checkstyle needed</label></div>
        <input
          id="checkstyle_needed_input"
          type="checkbox"
          value={checkstyleNeeded}
          onClick={(e) => { setCheckstyleNeeded(e.target.checked) }}
        />
      </div>

      {checkstyleNeeded && <div>
        <div><label htmlFor="checkstyle_points_input">Checkstyle points</label></div>
        <input
          id="checkstyle_points_input"
          type="number"
          max={100}
          min={1}
          value={checkstylePoints}
          onChange={(e) => { setCheckstylePoints(e.target.value) }}
        />
      </div>}

      <div>
        <div><label htmlFor="submissions_limit_enabled_input">Set submissions limit</label></div>
        <input
          id="submissions_limit_enabled_input"
          type="checkbox"
          value={submissionsNumberLimitEnabled}
          onClick={(e) => { setSubmissionsNumberLimitEnabled(e.target.checked) }}
        />
      </div>

      {submissionsNumberLimitEnabled && <div>
        <div><label htmlFor="submissions_limit_input">Submissions limit</label></div>
        <input
          id="submissions_limit_input"
          type="number"
          max={100}
          min={1}
          value={submissionsNumberLimit}
          onChange={(e) => { setSubmissionsNumberLimit(e.target.value) }}
        />
      </div>}

      <div>
        <button onClick={handleCreate} disabled={!formValid}>Create task</button>
      </div>
    </div>
  );
};

export default CreateTaskForm;