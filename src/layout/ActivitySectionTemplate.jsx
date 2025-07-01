import React, { useEffect, useState } from "react";
import ActivityTable from "../component/dashboard/ActivityTable";
import { Plus } from "lucide-react";
import axios from "axios";
import PreviousActivityTable from "../component/dashboard/PreviousActivityTable";

const ActivitySectionTemplate = ({ title, formComponent: FormComponent, chart, records, loading }) => {
  const token = sessionStorage.getItem("authToken");
  const today = new Date().toISOString().slice(0, 10);

  const [formData, setFormData] = useState({ type: "", steps: "", calories: "", duration: "" });
  const [editData, setEditData] = useState(null);
  const [activityLogs, setActivityLogs] = useState([]);
  const [loadingToday, setLoadingToday] = useState(true);
  const [chartOrHistory, setChartOrHistory] = useState("chart");
  const [showForm, setShowForm] = useState(false);

  //for test purpose
  let dummyRecords = [
    { id: 1, name: "Running", steps: 5000, calories: 300, duration: "30 min" },
    { id: 2, name: "Walking", steps: 3000, calories: 150, duration: "20 min" },
    { id: 3, name: "Cycling", steps: 8000, calories: 400, duration: "45 min" },
    { id: 4, name: "HIIT", steps: 6000, calories: 500, duration: "45 min" },
    { id: 5, name: "Swimming", steps: 0, calories: 350, duration: "40 min" },
    { id: 6, name: "Pilates", steps: 0, calories: 250, duration: "60 min" },
    { id: 7, name: "Strength Training", steps: 2000, calories: 600, duration: "1 hr" },
  ];


  const handleChange = (e) => {
    if (editData) {
      setEditData({ ...editData, [e.target.name]: e.target.value });
    } else {
      setFormData({ ...formData, [e.target.name]: e.target.value });
    }
  };

  //today's activity submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:5055/activities", { ...formData, date: today }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setFormData({ type: "", steps: "", calories: "", duration: "" });
  
      fetchTodayActivities();
      setShowForm(false);
    } catch (err) {
      console.error("Error logging activity", err);
    }
  };

  //today's activity update
  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:5055/update-activity", { ...editData, date: today }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setEditData(null);
     
      fetchTodayActivities();
    } catch (err) {
      console.error("Error updating activity", err);
    }
  };

  //today's activity delete
  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:5055/delete-activity/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
        data: { date: today },
      });
     
      fetchTodayActivities();
    } catch (err) {
      console.error("Error deleting activity", err);
    }
  };

  //fetch today's activity
  const fetchTodayActivities = async () => {
    try {
      const res = await axios.get(`http://localhost:5055/activities?date=${today}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setActivityLogs(res.data);

    } catch (err) {
      console.error("Error fetching today's activity", err);
    } finally {
      setLoadingToday(false);
    }
  };

  useEffect(() => {
    fetchTodayActivities();
    setActivityLogs(dummyRecords);// for test purpose
  }, []);

  return (
    <div className="p-6 mt-5 max-w-4xl mx-auto space-y-6">
      <h2 className="text-2xl font-bold text-blue-600">{title}</h2>
      <div className="bg-white p-4 rounded-lg shadow">

        {/* today's activity box */}
        <div className="flex flex-row justify-between">
          <h2 className="text-xl font-bold text-blue-600">Today's activity</h2>

          {/* adding activity */}
          <button
            onClick={() => setShowForm(true)}
            className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
          >
            <Plus size={20} /> Add Activity
          </button>

        </div>
        {loadingToday ? (
          <p className="text-center text-blue-600">Loading...</p>
        ) : activityLogs.length === 0 ? (
          <p className="text-center text-blue-600">No activity added</p>
        ) : (
          <ActivityTable data={activityLogs} onEdit={setEditData} onDelete={handleDelete} />
        )}

        {/* adding actvity form */}
        {showForm && (
          <div className="fixed inset-0 bg-blue-500 bg-opacity-50 flex justify-center items-center z-2">
            <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow max-w-md w-full space-y-4">
              <FormComponent formData={formData} onChange={handleChange} />
              <div className="flex justify-end gap-2">
                <button type="button" onClick={() => setShowForm(false)} className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-full">Close</button>
                <button type="submit" className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full">Submit</button>
              </div>
            </form>
          </div>
        )}

        {/* editing activity form */}

        {editData && (
          <div className="fixed inset-0 bg-blue-500 bg-opacity-50 flex justify-center items-center z-2">
            <form onSubmit={handleUpdate} className="bg-white p-6 rounded-lg shadow max-w-md w-full space-y-4">
              <FormComponent formData={editData} onChange={handleChange} />
              <div className="flex justify-end gap-2">
                <button type="button" onClick={() => setEditData(null)} className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-full">Close</button>
                <button type="submit" className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full">Update</button>
              </div>
            </form>
          </div>
        )}
      </div>

        {/* previous activity section */}
      <div className="bg-white p-4 rounded-lg shadow">
        <div className="flex gap-2">
          <button onClick={() => setChartOrHistory("chart")} className={`px-4 py-2 rounded-md ${chartOrHistory === "chart" ? "bg-gray-700 text-white" : "bg-gray-300 text-gray-700"}`}>Chart</button>
          <button onClick={() => setChartOrHistory("history")} className={`px-4 py-2 rounded-md ${chartOrHistory === "history" ? "bg-gray-700 text-white" : "bg-gray-300 text-gray-700"}`}>Previous Activity</button>
        </div>
        {chartOrHistory === "chart" ? (
          <div className="p-4">
            <h3 className="font-semibold text-lg mb-2 text-blue-600">Overview</h3>
            {chart}
          </div>
        ) : (
          <div className="p-4">
            <h3 className="font-semibold text-lg mb-2 text-blue-600">Recent Records</h3>
            {loading ? (
              <p className="text-blue-600">Loading...</p>
            ) : (
              <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm">
                <PreviousActivityTable data={records} />
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default ActivitySectionTemplate;
