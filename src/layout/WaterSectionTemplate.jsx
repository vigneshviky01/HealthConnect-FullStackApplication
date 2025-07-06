// WaterSectionTemplate.jsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import { Plus } from "lucide-react";
import WaterTable from "../component/dashboard/WaterTable";
import WaterChart from "../component/dashboard/WaterChart";
import PreviousWaterTable from "../component/dashboard/PreviousWaterTable";
import EmptyDataPrompt from "../component/EmptyDataPrompt";

const WaterSectionTemplate = ({ title, formComponent: FormComponent }) => {
  const token = sessionStorage.getItem("authToken");
  const today = new Date().toISOString().slice(0, 10);

  const [formData, setFormData] = useState({ amount: "" });
  const [editData, setEditData] = useState(null);
  const [waterLogs, setWaterLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [chartOrHistory, setChartOrHistory] = useState("chart");
  const [showForm, setShowForm] = useState(false);

  const dummyRecords = [
    { id: 1, date: "2025-06-28", amount: 3 },
    { id: 2, date: "2025-06-29", amount: 4 },
    { id: 3, date: "2025-06-30", amount: 3.5 },
    { id: 4, date: "2025-07-01", amount: 2.5 },
    { id: 5, date: "2025-07-02", amount: 3.2 },
    { id: 6, date: today, amount: 1.2 },
    { id: 7, date: today, amount: 2 },
    { id: 8, date: "2025-06-27", amount: 3 },
    { id: 9, date: "2025-06-26", amount: 4 },
    { id: 10, date: "2025-06-25", amount: 3.5 },
    { id: 11, date: "2025-06-24", amount: 2.5 },
    { id: 12, date: "2025-06-23", amount: 3.2 },
    { id: 8, date: "2025-06-22", amount: 3 },
    { id: 9, date: "2025-06-21", amount: 4 },
    { id: 10, date: "2025-06-20", amount: 3.5 },
    { id: 11, date: "2025-06-19", amount: 2.5 },
    { id: 12, date: "2025-06-18", amount: 3.2 },
    
  ];

  const fetchWaterLogs = async () => {
    try {
      const res = await axios.get(`http://localhost:5055/water-intake`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setWaterLogs(res.data);
    } catch (err) {
      console.error("Error fetching water logs", err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editData) {
        await axios.post(
          "http://localhost:5055/update-water-intake",
          editData,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setEditData(null);
      } else {
        await axios.post(
          "http://localhost:5055/water-intake",
          { ...formData, date: today },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setFormData({ amount: "" });
      }
      fetchWaterLogs();
      setShowForm(false);
    } catch (err) {
      console.error("Error submitting water intake", err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:5055/delete-water-intake/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchWaterLogs();
    } catch (err) {
      console.error("Error deleting water entry", err);
    }
  };

  useEffect(() => {
    fetchWaterLogs();
    setWaterLogs(dummyRecords); // for test only
  }, []);

  const hasTodayWaterLogs = waterLogs &&  waterLogs.some((entry) => entry.date === today);
  return (
    <div className="p-6 mt-5 max-w-4xl mx-auto space-y-6">
      <h2 className="text-2xl font-bold text-blue-600">{title}</h2>
      <div className="bg-white p-4 rounded-lg shadow">
        <div className="flex justify-between mb-2">
          <h2 className="text-xl font-bold text-blue-600">Today's Intake</h2>
          <button
            onClick={() => {
              setShowForm(true);
              setEditData(null);
            }}
            className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
          >
            <Plus size={20} /> Log Water Intake
          </button>
        </div>

        {showForm && (
          <div className="fixed inset-0 bg-blue-500 bg-opacity-50 flex justify-center items-center z-2">
            <form
              onSubmit={handleSubmit}
              className="bg-white p-6 rounded-lg shadow max-w-md w-full space-y-4"
            >
              <FormComponent
                formData={editData || formData}
                onChange={(e) =>
                  editData
                    ? setEditData({ ...editData, [e.target.name]: e.target.value })
                    : setFormData({ ...formData, [e.target.name]: e.target.value })
                }
              />
              <div className="flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => {
                    setShowForm(false);
                    setEditData(null);
                  }}
                  className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-full"
                >
                  Close
                </button>
                <button
                  type="submit"
                  className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full"
                >
                  {editData ? "Update" : "Submit"}
                </button>
              </div>
            </form>
          </div>
        )}

        {loading ? (
          <p className="text-center text-blue-600">Loading...</p>
        ) : (
          hasTodayWaterLogs?(

            <WaterTable
              data={waterLogs.filter((w) => w.date === today)}
              onDelete={handleDelete}
              onEdit={(entry) => {
                setEditData(entry);
                setShowForm(true);
              }}
            />
          ): <EmptyDataPrompt message="Log your water intake for today"/>
        )}
      </div>

      <div className="bg-white p-4 rounded-lg shadow">
        <div className="flex gap-2">
          <button
            onClick={() => setChartOrHistory("chart")}
            className={`px-4 py-2 rounded-md ${chartOrHistory === "chart" ? "bg-gray-700 text-white" : "bg-gray-300 text-gray-700"}`}
          >
            Chart
          </button>
          <button
            onClick={() => setChartOrHistory("history")}
            className={`px-4 py-2 rounded-md ${chartOrHistory === "history" ? "bg-gray-700 text-white" : "bg-gray-300 text-gray-700"}`}
          >
            Previous Logs
          </button>
        </div>

        {chartOrHistory === "chart" ? (
          <div className="p-4">
            <h3 className="font-semibold text-lg mb-2 text-blue-600">Overview</h3>
            <WaterChart data={waterLogs} />
          </div>
        ) : (
          <div className="p-4">
            <h3 className="font-semibold text-lg mb-2 text-blue-600">Previous Entries</h3>
            <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm">
              <PreviousWaterTable data={waterLogs.filter(w => w.date !== today)} />
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default WaterSectionTemplate;
