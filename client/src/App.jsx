import React, { createContext } from "react";
import AppRouter from "./routes/AppRouter";
export const AuthContext = createContext();

const App = () => {
  const getAccessToken = () => {
    return localStorage.getItem("accessToken");
  };

  const getRefreshToken = () => {
    return localStorage.getItem("refreshToken");
  };

  const setAccessToken = (accessToken) => {
    localStorage.setItem("accessToken", accessToken);
  };

  const setRefreshToken = (refreshToken) => {
    localStorage.setItem("refreshToken", refreshToken);
  };

  const removeTokens = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  };

  const tokenFunctions = {
    getAccessToken,
    getRefreshToken,
    setAccessToken,
    setRefreshToken,
    removeTokens,
  };

  return (
    <div>
      <AuthContext.Provider value={tokenFunctions}>
        <AppRouter />
      </AuthContext.Provider>
    </div>
  );
};

export default App;
