import React from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AuthProvider from "../components/AuthProvider/AuthProvider";
import LoginForm from "../components/LoginForm/LoginForm";
import RegisterForm from "../components/RegisterForm/RegisterForm";
import MainPage from "../components/MainPage/MainPage";
import AccountPage from "../components/AccountPage/AccountPage";
import LogoutConfirmation from "../components/Logout/LogoutConfirmation";
import TaskElement from "../components/TaskElement/TaskElement";
import AllTasksPage from "../components/AllTasksPage/AllTasksPage";
import CreateTaskForm from "../components/CreateTaskForm/CreateTaskForm";
import SubmissionDetails
  from "../components/SubmissionDetails/SubmissionDetails";
import AdminTool from "../components/AdminTool/AdminTool";
import TaskStatsPage from "../components/TaskStatsPage/TaskStatsPage";
import ChangePasswordDialog from "../components/ChangePasswordDialog/ChangePasswordDialog";
import ChangeTaskForm from "../components/ChangeTaskForm/ChangeTaskForm";

const AppRouter = () => {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route index path="/" element={<MainPage />} />
          <Route path="/login" element={<LoginForm />} />
          <Route path="/register" element={<RegisterForm />} />
          <Route
            path="/logout"
            element={
              <AuthProvider roles={"*"}>
                <LogoutConfirmation />
              </AuthProvider>
            }
          />
          <Route path="/all-tasks" element={<AllTasksPage />} />

          <Route path="/all-tasks/task/:id" element={
            <TaskElement />
          } />

          <Route path="/all-tasks/tasks/:taskId/stats" element={
            <TaskStatsPage />
          } />

          <Route path={"/all-tasks/task/:taskId/my-submissions/:submissionId"} element={
            <AuthProvider>
              <SubmissionDetails />
            </AuthProvider>
          } />

          <Route path="/create-task" element={
            <AuthProvider roles={["TEACHER"]}>
              <CreateTaskForm />
            </AuthProvider>
          }
          />


          <Route path="/admin-tool" element={
            <AuthProvider roles={["ADMIN"]}>
              <AdminTool />
            </AuthProvider>
          }
          />

          <Route
            path="/account"
            element={
              <AuthProvider>
                <AccountPage />
              </AuthProvider>
            }
          />

          <Route
            path="/change-password"
            element={
              <AuthProvider>
                <ChangePasswordDialog />
              </AuthProvider>
            }
          />

          <Route path="/all-tasks/tasks/:id/change-task" element={
            <AuthProvider roles={"*"}>
              <ChangeTaskForm />
            </AuthProvider>
          } />

          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
};

export default AppRouter;
