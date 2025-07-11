import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "../../section/Navbar";
import { useUser } from "../../context/UserContext";
import Welcome from "../../section/dashboard/Welcome";
import DashboardNav from "../../section/dashboard/DashboardNav";

import AnalysisDashboardLayout from "../../layout/AnalysisDashBoardLayout";
import WeeklySleepChart from "../../component/week chart/weeklySleepChart";
import MonthlySleepChart from "../../component/month chart/monthSleepChart";
import WeeklyActivityChart from "../../component/week chart/weeklyActivityChart";
import MonthlyActivityChart from "../../component/month chart/MonthlyActivityChart";
import WeeklyWaterChart from "../../component/week chart/WeeklyWaterChart";
import MonthlyWaterChart from "../../component/month chart/MonthlyWaterChart";
import WeeklyMoodChart from "../../component/week chart/weeklyMoodChart";
import MonthlyMoodChart from "../../component/month chart/MonthlyMoodChart";
const Dashboard = () => {
  const { userInfo, setUserInfo } = useUser();
  const [selectedAnalysis, setSelectedAnalysis] = useState("Activity");
  const token = sessionStorage.getItem("authToken");
  console.log(token);
       useEffect(() => {
      axios
        .get("http://localhost:8080/api/users/profile", {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((res) => setUserInfo(res.data))
        .catch((err) => console.error("Failed to fetch user data", err));
    }, []);
    console.log(userInfo);

    if (!userInfo) {
      return <div className="text-center mt-20">Loading user info...</div>;
    }
     const getCharts = () => {
    switch (selectedAnalysis) {
      case "Sleep":
        return {
          title: "Sleep Graph",
          weeklyChart: <WeeklySleepChart />,
          monthlyChart: <MonthlySleepChart />,
        };
      case "Activity":
        return {
          title: "Activity Graph",
          weeklyChart: <WeeklyActivityChart />,
          monthlyChart: <MonthlyActivityChart />,
        };
      case "Water":
        return {
          title: "Water Graph",
          weeklyChart: <WeeklyWaterChart />,
          monthlyChart: <MonthlyWaterChart />,
        };
      case "Mood":
        return {
          title: "Mood Graph",
          weeklyChart: <WeeklyMoodChart />,
          monthlyChart: <MonthlyMoodChart />,
        };
      default:
        return null;
    }
  };

  const { title, weeklyChart, monthlyChart } = getCharts();

  return (
    <>
      <Welcome name={userInfo.name} />
      <DashboardNav />
       <div className="flex justify-start mt-4">
        <select
          value={selectedAnalysis}
          onChange={(e) => setSelectedAnalysis(e.target.value)}
          className="border px-4 py-2 rounded-lg shadow-md text-gray-700 bg-blue-600 text-white text-bold mt-2"
        >
          <option value="Sleep">Sleep Graph</option>
          <option value="Activity">Activity Graph</option>
          <option value="Water">Water Graph</option>
          <option value="Mood">Mood Graph</option>
        </select>
      </div>

      <AnalysisDashboardLayout
        title={title}
        weeklyChart={weeklyChart}
        monthlyChart={monthlyChart}
      />
    </> 
  );
};

export default Dashboard;
