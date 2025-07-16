import React, { useEffect, useState } from "react";
import axios from "axios";
import { Plus } from "lucide-react";
import WaterTable from "../component/dashboard/WaterTable";
import WaterChart from "../component/dashboard/WaterChart";
import PreviousWaterTable from "../component/dashboard/PreviousWaterTable";
import EmptyDataPrompt from "../component/EmptyDataPrompt";
import { useUser } from "../context/UserContext";
import { toast } from "react-toastify";
import ConfirmDialog from "../component/ConfirmDialog";
import { BarChart3, FileX2 } from "lucide-react";

const WaterSectionTemplate = ({ title, formComponent: FormComponent }) => {
  const token = sessionStorage.getItem("authToken");
  const today = new Date().toISOString().slice(0, 10);

  const [formData, setFormData] = useState({ amount: "" });
  const [editData, setEditData] = useState(null);
  const [waterLogs, setWaterLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [chartOrHistory, setChartOrHistory] = useState("chart");
  const [showForm, setShowForm] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [deleteId, setDeleteId] = useState(null);

  const fetchWaterLogs = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/water`, {
        headers: { Authorization: `Bearer ${token}` },
      });

     
      const logs = Array.isArray(res.data) ? res.data : [];
      setWaterLogs(logs);
    } catch (err) {
      console.error("Error fetching water logs", err);
      setWaterLogs([]); // ✅ fallback on error
    } finally {
      setLoading(false);
    }
  };

  const hasTodayWaterLogs =
    waterLogs && waterLogs.some((entry) => entry.intakeDate === today);
 const todayWaterLogs =
  waterLogs?.find((entry) => entry.intakeDate === today) || null;


  const previousLogs = waterLogs.filter((w) => w.intakeDate !== today);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const amountToValidate = editData ? editData.amount : formData.amount;

    if (!amountToValidate || parseFloat(amountToValidate) <= 0) {
      toast.error("Please enter a valid amount greater than 0");
      return;
    }

    try {
      const payload = {
        intakeDate: today,
        amountLiters: parseFloat(editData?.amount || formData.amount),
      };

      if (editData) {
        await axios.put(
          `http://localhost:8080/api/water/${editData.id}`,
          payload,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setEditData(null);
      } else {
       
        if(hasTodayWaterLogs){
            await axios.post(`http://localhost:8080/api/water/${todayWaterLogs.id}/add`, payload, {
            headers: { Authorization: `Bearer ${token}` },
          });
        }
        else{
          await axios.post(`http://localhost:8080/api/water`, payload, {
            headers: { Authorization: `Bearer ${token}` },
          });
        }
        setFormData({ amount: "" });
      }

      await fetchWaterLogs();
      setShowForm(false);
    } catch (err) {
      console.error("Error submitting water intake", err);
    }
  };

  //   const handleDelete = async (id) => {

  //     try {
  //       await axios.delete(
  //   `http://localhost:8080/api/water/${id}`,
  //   {
  //     headers: { Authorization: `Bearer ${token}` },
  //   }
  // );

  //       fetchWaterLogs();
  //     } catch (err) {
  //       console.error("Error deleting water entry", err);
  //     }
  //   };
  const confirmDelete = (id) => {
    setDeleteId(id);
    setShowConfirm(true);
  };

  const performDelete = async () => {
    if (!deleteId) return;

    try {
      await axios.delete(`http://localhost:8080/api/water/${deleteId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      toast.success("Water entry deleted successfully!");
      fetchWaterLogs();
    } catch (err) {
      console.error("Error deleting water entry", err);
    } finally {
      setShowConfirm(false);
      setDeleteId(null);
    }
  };

  useEffect(() => {
    fetchWaterLogs();
  }, []);

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
            className="cursor-pointer flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
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
                  className="cursor-pointer bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-full"
                >
                  Close
                </button>
                <button
                  type="submit"
                  className="cursor-pointer bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full"
                >
                  {editData ? "Update" : "Submit"}
                </button>
              </div>
            </form>
          </div>
        )}

        {loading ? (
          <p className="text-center text-blue-600">Loading...</p>
        ) : hasTodayWaterLogs ? (
          <WaterTable
            data={waterLogs.filter((w) => w.intakeDate === today)}
            onDelete={confirmDelete}
            onEdit={(entry) => {
              setEditData({ ...entry, amount: entry.amountLiters });
              setShowForm(true);
            }}
          />
        ) : (
          <EmptyDataPrompt message="Log your water intake for today" />
        )}
      </div>

      <div className="bg-white sm:p-4 rounded-lg shadow">
        <div className="p-4 flex gap-2">
          <button
            onClick={() => setChartOrHistory("chart")}
            className={`cursor-pointer px-4 py-2 rounded-md ${
              chartOrHistory === "chart"
                ? "bg-gray-700 text-white"
                : "bg-gray-300 text-gray-700"
            }`}
          >
            Chart
          </button>
          <button
            onClick={() => setChartOrHistory("history")}
            className={`cursor-pointer px-4 py-2 rounded-md ${
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
            {waterLogs.length > 0 ? (
              <WaterChart data={waterLogs} />
            ) : (
              <div className="w-full bg-yellow-50 text-yellow-700 p-4 rounded-md shadow-sm border border-yellow-300 flex items-start gap-3">
                <BarChart3 className="w-6 h-6 mt-1 text-yellow-700" />
                <div>
                  <p className="font-medium">
                    No Previous data available for chart
                  </p>
                </div>
              </div>
            )}
          </div>
        ) : (
          <div className="p-4">
            <h3 className="font-semibold text-lg mb-2 text-blue-600">
              Previous Entries
            </h3>
            <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm">
              {previousLogs.length > 0 ? (
                <PreviousWaterTable data={previousLogs} />
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
        )}
      </div>
      <ConfirmDialog
        isOpen={showConfirm}
        message="Are you sure you want to delete this water entry?"
        onConfirm={performDelete}
        onCancel={() => {
          setShowConfirm(false);
          setDeleteId(null);
        }}
      />
    </div>
  );
};

export default WaterSectionTemplate;
