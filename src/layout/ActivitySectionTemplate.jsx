
import React, { useEffect, useState } from "react";
import ActivityTable from "../component/dashboard/ActivityTable";
import { Plus } from "lucide-react";
import axios from "axios";
import PreviousActivityTable from "../component/dashboard/PreviousActivityTable";
import { BarChart3, FileX2 } from "lucide-react";
import EmptyDataPrompt from "../component/EmptyDataPrompt";
import ConfirmDialog from "../component/ConfirmDialog";
const ActivitySectionTemplate = ({
  title,
  formComponent: FormComponent,
  chart,
  records,
  loading,
}) => {
  const token = sessionStorage.getItem("authToken");
  const today = new Date().toISOString().slice(0, 10);

  const [formData, setFormData] = useState({
    stepsCount: 0,
    caloriesBurned: 0,
    workoutType: "",
    workoutDurationMinutes: 0,
    distanceKm: 0,
    notes: ""
  });
  const [editData, setEditData] = useState(null);
  const [activityLogs, setActivityLogs] = useState([]);
  const [loadingToday, setLoadingToday] = useState(true);
  const [chartOrHistory, setChartOrHistory] = useState("chart");
  const [showForm, setShowForm] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [activityToDelete, setActivityToDelete] = useState(null);



  const handleChange = (e) => {
    const updater = editData ? setEditData : setFormData;
    updater((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await axios.post(
        "http://localhost:8080/api/activities",
        { ...formData, activityDate: today },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setFormData({ stepsCount: 0, caloriesBurned: 0, workoutType: "", workoutDurationMinutes: 0, distanceKm: 0, notes: "" });
      fetchTodayActivities();
      setShowForm(false);
    } catch (err) {
      console.error("Error logging activity", err);
    }
  };

  const handleUpdate = async (e) => {

    e.preventDefault();
    try {
      const payload = {
        stepsCount: editData.stepsCount,
        caloriesBurned: editData.caloriesBurned,
        workoutType: editData.workoutType,
        workoutDurationMinutes: editData.workoutDurationMinutes,
        distanceKm: editData.distanceKm,
        notes: editData.notes,
        activityDate: today
      };
      console.log(editData);
      await axios.put(
        `http://localhost:8080/api/activities/${editData.id}`,
        payload,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setEditData(null);
      fetchTodayActivities();
    } catch (err) {
      console.error("Error updating activity", err);
    }
  };

  const confirmDelete = async () => {
    if (!activityToDelete) return;

    try {
      await axios.delete(`http://localhost:8080/api/activities/${activityToDelete}`, {
        headers: { Authorization: `Bearer ${token}` },
        data: { date: today },
      });
      fetchTodayActivities();
    } catch (err) {
      console.error("Error deleting activity", err);
    } finally {
      setShowConfirm(false);
      setActivityToDelete(null);
    }
  };

  const fetchTodayActivities = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/activities/by-date?${today}`, {
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


  }, []);

  return (
    <div className="p-4 sm:p-6 mt-5 max-w-4xl mx-auto space-y-6">
      <h2 className="text-xl sm:text-2xl font-bold text-blue-600">{title}</h2>

      <div className="bg-white p-4 sm:p-6 rounded-lg shadow space-y-4">
        <div className="flex flex-wrap justify-between items-center gap-2">
          <h2 className="text-lg font-semibold text-blue-600">Today's Activity</h2>
          <button
            onClick={() => setShowForm(true)}
            className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 text-sm"
          >
            <Plus size={20} /> Add Activity
          </button>
        </div>

        {loadingToday ? (
          <p className="text-center text-blue-600">Loading...</p>
        ) : activityLogs.length === 0 ? (
          <EmptyDataPrompt message="Add your Activity record for today" />

        ) : (
          <ActivityTable data={activityLogs} onEdit={setEditData} onDelete={(id) => {
            setActivityToDelete(id);
            setShowConfirm(true);
          }} />
        )}

        {/* Add Form Modal */}
        {(showForm || editData) && (
          <div className="fixed inset-0 bg-blue-500 bg-opacity-50 flex justify-center items-center z-50 p-2">
            <form
              onSubmit={showForm ? handleSubmit : handleUpdate}
              className="bg-white p-4 sm:p-6 rounded-lg shadow max-w-md w-full space-y-4 overflow-y-auto max-h-[90vh]"
            >
              <FormComponent
                formData={showForm ? formData : editData}
                onChange={handleChange}
              />
              <div className="flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => {
                    showForm ? setShowForm(false) : setEditData(null);
                  }}
                  className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-full"
                >
                  Close
                </button>
                <button
                  type="submit"
                  className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full"
                >
                  {showForm ? "Submit" : "Update"}
                </button>
              </div>
            </form>
          </div>
        )}

        <ConfirmDialog
          isOpen={showConfirm}
          message="Are you sure you want to delete this activity?"
          onConfirm={confirmDelete}
          onCancel={() => {
            setShowConfirm(false);
            setActivityToDelete(null);
          }}
        />
      </div>

      <div className="bg-white p-4 sm:p-6 rounded-lg shadow">
        <div className="flex flex-wrap gap-2 mb-4">
          <button
            onClick={() => setChartOrHistory("chart")}
            className={`px-4 py-2 rounded-md text-sm ${chartOrHistory === "chart"
              ? "bg-gray-700 text-white"
              : "bg-gray-300 text-gray-700"
              }`}
          >
            Chart
          </button>
          <button
            onClick={() => setChartOrHistory("history")}
            className={`px-4 py-2 rounded-md text-sm ${chartOrHistory === "history"
              ? "bg-gray-700 text-white"
              : "bg-gray-300 text-gray-700"
              }`}
          >
            Previous Activity
          </button>
        </div>

        <div className="p-2 sm:p-4">
          <h3 className="font-semibold text-base sm:text-lg mb-2 text-blue-600">
            {chartOrHistory === "chart" ? "Overview" : "Recent Records"}
          </h3>

          {chartOrHistory === "chart" ? (
            records.length > 0 ? (
              chart
            ) : (
              <div className="w-full bg-yellow-50 text-yellow-700 p-4 rounded-md shadow-sm border border-yellow-300 flex items-start gap-3">
                <BarChart3 className="w-6 h-6 mt-1 text-yellow-700" />
                <div>
                  <p className="font-medium">No Previous data available for chart</p>

                </div>
              </div>
            )
          ) : loading ? (
            <p className="text-blue-600">Loading...</p>
          ) : records.length > 0 ? (
            <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm">
              <PreviousActivityTable data={records} />
            </div>
          ) : (
            <div className="w-full bg-yellow-50 text-yellow-700 p-4 rounded-md shadow-sm border border-yellow-300 flex items-start gap-3">
              <FileX2 className="w-6 h-6 mt-1 text-yellow-700" />
              <div>
                <p className="font-medium">No Previous records found</p>

              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ActivitySectionTemplate;
