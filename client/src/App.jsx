import React, { createContext } from "react";
import Router from "./routes/AppRouter";

export const AuthContext = createContext();

const App = () => {
  
  const getToken = () => {
    return localStorage.getItem('token999');
  }

  const setToken = (value) => {
    localStorage.setItem('token999', value);
  }

  return (
    <div>
      <AuthContext.Provider value={{getToken, setToken}}>
        <Router/>
      </AuthContext.Provider>
    </div>
  );
};

export default App;
