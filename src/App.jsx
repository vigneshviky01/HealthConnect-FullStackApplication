import { Routes, Route } from "react-router-dom";
import { BrowserRouter } from "react-router-dom";
import Login from "./Pages/Account/Login";
import Register from "./Pages/Account/Register";
import Home from "./Pages/Home";
import Dashboard from "./Pages/dashboard/Dashboard";
import Profile from "./Pages/Profile";
import DashboardLayout from "./layout/DashboardLayout";
import SleepTracker from "./Pages/dashboard/SleepTracker";

import WaterTracker from "./Pages/dashboard/WaterTracker";
import ActivityTracker from "./Pages/dashboard/ActivityTracker";
import MoodTracker from "./Pages/dashboard/MoodTracker";
import { PrivateRoute } from "./component/LoginRoutes";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
function App() {
  return (
    <>
    <ToastContainer />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route  path="/profile" element={<PrivateRoute><Profile /></PrivateRoute>} />
          <Route path="/dashboard" element={<PrivateRoute><DashboardLayout /></PrivateRoute>}>
            <Route index element={<Dashboard />} /> {/* /dashboard */}
            <Route path="activity" element={<ActivityTracker />} />
            <Route path="sleep" element={<SleepTracker />} />
            <Route path="water" element={<WaterTracker />} />
            <Route path="mood" element={<MoodTracker />} />
          </Route>
          <Route exact path="/login" element={<Login />} />
          <Route exact path="/register" element={<Register />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
