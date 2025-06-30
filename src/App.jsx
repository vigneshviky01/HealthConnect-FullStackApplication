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

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route exact path="/" element={<Home />} />
          <Route exact path="/profile" element={<Profile />} />
          <Route path="/dashboard" element={<DashboardLayout />}>
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
