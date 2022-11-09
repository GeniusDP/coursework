import React from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AuthProvider from "../components/AuthProvider/AuthProvider";
import Forbidden from "../components/Forbidden/Forbidden";
import LoginForm from "../components/LoginForm/LoginForm";
import RegisterForm from "../components/RegisterForm/RegisterForm";
import MainPage from "../components/MainPage/MainPage";
import AccountPage from "../components/AccountPage/AccountPage";

const Router = () => {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route index path="/" element={<MainPage />} />
          <Route path="/login" element={<LoginForm />} />
          <Route path="/register" element={<RegisterForm />} />
          <Route
            path="/account-settings"
            element={
              <AuthProvider roles={"*"}>
                <AccountPage />
              </AuthProvider>
            }
          />
          <Route
            path="/forbidden"
            element={
              <AuthProvider roles={"*"}>
                <Forbidden />
              </AuthProvider>
            }
          />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
};

export default Router;
