import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardSectionTemplate from "../../layout/DashboardSectionTemplate";
import BackButton from "../../component/BackButton";
import ActivityForm from "../../component/dashboard/ActivityForm";
import ActivityChart from "../../component/dashboard/ActivityChart";

const ActivityTracker = () => {
  const token = sessionStorage.getItem("authToken");

  const [formData, setFormData] = useState({
    type: "",
    steps: "",
    calories: "",
    duration: "",
  });

  const [activityLogs, setActivityLogs] = useState([]);
  const [loading, setLoading] = useState(true);

  const handleChange = (e) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const fetchActivities = async () => {
    try {
      const res = await axios.get("http://localhost:5055/activities", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setActivityLogs(res.data);
    } catch (err) {
      console.error("Error fetching activity data", err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:5055/activities", formData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setFormData({ type: "", steps: "", calories: "", duration: "" });
      fetchActivities();
    } catch (err) {
      console.error("Error logging activity", err);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  return (
    <>
      <BackButton />
      <DashboardSectionTemplate
        title="Daily Activity Tracker"
        onSubmit={handleSubmit}
        form={<ActivityForm formData={formData} onChange={handleChange} />}
        records={activityLogs.map((act, idx) => (
          <div key={idx}>
            <strong>{act.type}</strong> - {act.steps} steps, {act.calories} cal, {act.duration} mins
            <div className="text-gray-500 text-sm">
              {new Date(act.date).toLocaleDateString()}
            </div>
          </div>
        ))}
        chart={<ActivityChart data={activityLogs} />}
        loading={loading}
      />
    </>
  );
};

export default ActivityTracker;
