import React, { createContext } from "react";
import Router from "./routes/AppRouter";

export const AuthContext = createContext();

const App = () => {
  
  const getToken = () => {
    return localStorage.getItem('token');
  }

  const setToken = (value) => {
    localStorage.setItem('token', value);
  }

  const removeToken = () => {
    localStorage.removeItem('token');
  }

  return (
    <div>
      <AuthContext.Provider value={{getToken, setToken, removeToken}}>
        <Router/>
      </AuthContext.Provider>
    </div>
  );
};

export default App;
