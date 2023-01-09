export default async function authRequestWrapper(callback) {

  const accessToken = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");

  const urlValidate = process.env.REACT_APP_AUTH_URL + "/validate-token";

  const validateOptions = {
    headers: {
      "Authorization": "Bearer " + accessToken,
    }
  }

  try {
    const response = await fetch(urlValidate, validateOptions);
    if (response.status === 401) {
      const refreshResult = await refreshTokenOperation(accessToken, refreshToken);
      if (!refreshResult) {
        return false;
      }
    }
    callback();
    return true;
  } catch (e) {
    return false;
  } 
}

const refreshTokenOperation = async (accessToken, refreshToken) => {
  const urlRefresh = process.env.REACT_APP_AUTH_URL + "/refresh-token";
  const body = {
    accessToken,
    refreshToken,
  };

  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  const requestOptions = {
    method: "POST",
    headers: myHeaders,
    body: JSON.stringify(body),
  };

  try {
    const response = await fetch(urlRefresh, requestOptions);
    if (response.ok) {
      const json = await response.json();
      localStorage.setItem("accessToken", json.accessToken);
      localStorage.setItem("refreshToken", json.refreshToken);
      return true;
    }
    return false;
  } catch (e) {
    return false;
  }
};
