import React, { useEffect, useState } from "react";
import axios from "axios";
import BackButton from "../../component/BackButton";
import ActivityForm from "../../component/dashboard/ActivityForm";
import ActivityChart from "../../component/dashboard/ActivityChart";
import ActivitySectionTemplate from "../../layout/ActivitySectionTemplate";

const ActivityTracker = () => {
  const token = sessionStorage.getItem("authToken");
  const [activityLogs, setActivityLogs] = useState([]);//for previous activity
  const [loading, setLoading] = useState(true);
  const today = new Date().toISOString().slice(0, 10);

  //fetch all the previous activities
  const fetchActivities = async () => {
    try {
      const res = await axios.get(`http://localhost:5055/activities/${today}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setActivityLogs(res.data);
    } catch (err) {
      console.error("Error fetching activity data", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  //for test
    const chartData = [
  { id: 1, date: "2025-06-28", name: "Running", steps: 5000, calories: 300, duration: "30 min" },
  { id: 2, date: "2025-06-28", name: "Walking", steps: 3000, calories: 150, duration: "20 min" },
  { id: 3, date: "2025-06-27", name: "Cycling", steps: 8000, calories: 400, duration: "45 min" },
  { id: 4, date: "2025-06-26", name: "Swimming", steps: 0, calories: 350, duration: "40 min" },
  { id: 5, date: "2025-06-30", name: "Swimming", steps: 0, calories: 350, duration: "30 min" },
  { id: 6, date: "2025-06-30", name: "Swimming", steps: 0, calories: 350, duration: "30 min" },
];


  return (
    <>
      <BackButton />
      <ActivitySectionTemplate
        title="Daily Activity Tracker"
        formComponent={ActivityForm}
        chart={<ActivityChart data={chartData} />} //data={activityLogs}
        records={chartData}//activityLogs
        refreshData={fetchActivities}
        loading={loading}
      />
    </>
  );
};

export default ActivityTracker;