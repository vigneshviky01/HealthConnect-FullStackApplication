import React, { useEffect, useState } from "react";
import axios from "axios";
import BackButton from "../../component/BackButton";
import ActivityForm from "../../component/dashboard/ActivityForm";
import ActivityChart from "../../component/dashboard/ActivityChart";
import ActivitySectionTemplate from "../../layout/ActivitySectionTemplate.jsx";

const ActivityTracker = () => {
  const token = sessionStorage.getItem("authToken");
  const [activityLogs, setActivityLogs] = useState([]);//for previous activity
  const [loading, setLoading] = useState(true);

 
  //fetch all the previous activities
  const fetchActivities = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/activities`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    const today = new Date().toISOString().slice(0, 10); // Get today's date in 'YYYY-MM-DD'
    const filteredActivities =res.data.filter(
      (activity) => activity.activityDate !== today
    );

    setActivityLogs(filteredActivities);
  } catch (err) {
    console.error("Error fetching activity data", err);
  } finally {
    setLoading(false);
  }
};
 
  useEffect(() => {
    fetchActivities();
  
  }, []);

  return (
    <>
      <BackButton />
      <ActivitySectionTemplate
        title="Daily Activity Tracker"
        formComponent={ActivityForm}
        chart={<ActivityChart data={activityLogs} />} 
        records={activityLogs}
        refreshData={fetchActivities}
        loading={loading}
      />
    </>
  );
};

export default ActivityTracker;