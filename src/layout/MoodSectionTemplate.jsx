// PreviousMoodTable.jsx (Updated with Header + Responsive Table + Add Control in Template)
import React, { useEffect, useState } from "react";
import axios from "axios";
import { Plus } from "lucide-react";
import { toast } from "react-toastify";
import MoodTable from "../component/dashboard/MoodTable";
import MoodChart from "../component/dashboard/MoodChart";
import PreviousMoodTable from "../component/dashboard/PreviousMoodTable";
import EmptyDataPrompt from "../component/EmptyDataPrompt";
import ConfirmDialog from "../component/ConfirmDialog";

const MoodSectionTemplate = ({ title, formComponent: FormComponent }) => {
  const token = sessionStorage.getItem("authToken");
  const today = new Date().toISOString().slice(0, 10);

  const [formData, setFormData] = useState({ moodRating: "", notes: "" });
  const [editData, setEditData] = useState(null);
  const [moodLogs, setMoodLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [chartOrHistory, setChartOrHistory] = useState("chart");
  const [showForm, setShowForm] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [deleteId, setDeleteId] = useState(null);
  // const dummyRecords = [
  //   { id: 1, date: "2025-06-28", mood: "Happy", note: "Had a great day!" },
  //   { id: 2, date: "2025-06-29", mood: "Sad", note: "Felt down." },
  //   {
  //     id: 3,
  //     date: "2025-06-30",
  //     mood: "Excited",
  //     note: "Something good happened!",
  //   },
  //   { id: 4, date: "2025-07-01", mood: "Angry", note: "Bad service." },
  //   { id: 5, date: "2025-07-02", mood: "Happy", note: "Weekend!" },
  //   { id: 6, date: today, mood: "Calm", note: "Just chill." },
  // ];

  const fetchMoodLogs = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/mood`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setMoodLogs(res.data);
    } catch (err) {
      console.error("Error fetching mood logs", err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        moodDate: today,
        moodRating: editData?.moodRating || formData.moodRating,
        notes: editData?.notes || formData.notes,
      };
      if (editData) {
        await axios.put(
          `http://localhost:8080/api/mood/${editData.id}`,
          payload,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setEditData(null);
      } else {
        console.log(formData);
        console.log(payload);
        await axios.post(`http://localhost:8080/api/mood`, payload, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setFormData({ moodRating: "", notes: "" });
      }
      fetchMoodLogs();
      setShowForm(false);
    } catch (err) {
      console.error("Error submitting mood log", err);
    }
  };

  // const handleDelete = async (id) => {
  //   try {
  //     await axios.delete(`http://localhost:8080/api/mood/${id}`, {
  //       headers: { Authorization: `Bearer ${token}` },
  //     });
  //     fetchMoodLogs();
  //   } catch (err) {
  //     console.error("Error deleting mood entry", err);
  //   }
  // };
  const confirmDelete = (id) => {
    setDeleteId(id);
    setShowConfirm(true);
  };

  const performDelete = async () => {
    if (!deleteId) return;

    try {
      await axios.delete(`http://localhost:8080/api/mood/${deleteId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      toast.success("Mood entry deleted successfully!");
      fetchMoodLogs();
    } catch (err) {
      console.error("Error deleting Mood entry", err);
    } finally {
      setShowConfirm(false);
      setDeleteId(null);
    }
  };

  useEffect(() => {
    fetchMoodLogs();
    // setMoodLogs(dummyRecords); // Only for test/demo
  }, []);

  const hasTodayMood = moodLogs.some((entry) => entry.moodDate === today);
  const previousLogs = moodLogs.filter((w) => w.moodDate !== today);
  return (
    <div className="p-6 mt-5 max-w-4xl mx-auto space-y-6">
      <h2 className="text-2xl font-bold text-blue-600">{title}</h2>
      <div className="bg-white p-4 rounded-lg shadow">
        <div className="flex justify-between mb-2">
          <h2 className="text-xl font-bold text-blue-600">Today's Mood</h2>
          <button
            onClick={() => {
              if (hasTodayMood) {
                toast.info(
                  "You've already logged your mood today. You can edit or delete it."
                );
              } else {
                setShowForm(true);
                setEditData(null);
              }
            }}
            className={`flex items-center gap-2 px-4 py-2 rounded-md transition ${
              hasTodayMood
                ? "bg-gray-300 text-gray-600 cursor-not-allowed"
                : "bg-blue-600 text-white hover:bg-blue-700"
            }`}
          >
            <Plus size={20} /> Log Mood
          </button>
        </div>

        {showForm && (
          <div className="fixed inset-0 bg-blue-500 bg-opacity-50 flex justify-center items-center z-10">
            <form
              onSubmit={handleSubmit}
              className="bg-white p-6 rounded-lg shadow max-w-md w-full space-y-4"
            >
              <FormComponent
                formData={editData || formData}
                onChange={(e) =>
                  editData
                    ? setEditData({
                        ...editData,
                        [e.target.name]: e.target.value,
                      })
                    : setFormData({
                        ...formData,
                        [e.target.name]: e.target.value,
                      })
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
        ) : hasTodayMood ? (
          <MoodTable
            data={moodLogs.filter((w) => w.moodDate === today)}
            onEdit={(entry) => {
              setEditData(entry);
              setShowForm(true);
            }}
            onDelete={confirmDelete}
          />
        ) : (
          <EmptyDataPrompt message="Log your mood for today!" />
        )}
      </div>

      <div className="bg-white p-4 rounded-lg shadow">
        <div className="flex gap-2">
          <button
            onClick={() => setChartOrHistory("chart")}
            className={`px-4 py-2 rounded-md ${
              chartOrHistory === "chart"
                ? "bg-gray-700 text-white"
                : "bg-gray-300 text-gray-700"
            }`}
          >
            Chart
          </button>
          <button
            onClick={() => setChartOrHistory("history")}
            className={`px-4 py-2 rounded-md ${
              chartOrHistory === "history"
                ? "bg-gray-700 text-white"
                : "bg-gray-300 text-gray-700"
            }`}
          >
            Previous Logs
          </button>
        </div>

        {chartOrHistory === "chart" ? (
          <div className="p-4">
            <h3 className="font-semibold text-lg mb-2 text-blue-600">
              Overview
            </h3>
            {moodLogs.length > 0 ? (
              <MoodChart data={moodLogs} />
            ) : (
              <div className="text-center">No data</div>
            )}
          </div>
        ) : (
          <div className="p-4">
            <h3 className="font-semibold text-lg mb-2 text-blue-600">
              Previous Entries
            </h3>
            <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm">
              {previousLogs.length > 0 ? (
                <PreviousMoodTable data={previousLogs} />
              ) : (
                <div className="text-center">No data</div>
              )}
            </div>
          </div>
        )}
      </div>
      <ConfirmDialog
        isOpen={showConfirm}
        message="Are you sure you want to delete this mood entry?"
        onConfirm={performDelete}
        onCancel={() => {
          setShowConfirm(false);
          setDeleteId(null);
        }}
      />
    </div>
  );
};

export default MoodSectionTemplate;
