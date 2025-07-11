import React, { useState } from "react";
import { Plus } from "lucide-react";
import { BarChart3, FileX2 } from "lucide-react";
import EmptyDataPrompt from "../component/EmptyDataPrompt";
import { toast } from "react-toastify";

const SleepSectionTemplate = ({
  title,
  formComponent: FormComponent,
  chartComponent,
  tableComponent,
  records,
  loading,
  onSubmit,
  onUpdate,
  formData,
  setFormData,
  editData,
  setEditData,
  previousTable,
  todaySleepData,
}) => {
  const [showForm, setShowForm] = useState(false);
  const [view, setView] = useState("chart");

  const handleChange = (e) => {
    const updater = editData ? setEditData : setFormData;
    updater((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  return (
    <div className="p-4 sm:p-6 mt-5 max-w-4xl mx-auto space-y-6">
      <h2 className="text-xl sm:text-2xl font-bold text-blue-600">{title}</h2>

      <div className="bg-white p-4 rounded-lg shadow space-y-4">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2">
          <h3 className="text-lg sm:text-xl font-semibold text-blue-600">
            Yesterday's Sleep
          </h3>
            <button
              onClick={() => {
                if (todaySleepData.length !== 0) {
                  toast.info(
                    "You've already logged your sleep info today. You can edit or delete it."
                  );
                } else {
                  setShowForm(true);
                }
              }}
               className={`flex items-center gap-2 px-4 py-2 rounded-md transition ${
              todaySleepData.length !== 0
                ? "bg-gray-300 text-gray-600 cursor-not-allowed"
                : "bg-blue-600 text-white hover:bg-blue-700 cursor-pointer "
            }`}
            >
              <Plus size={20} /> Add Sleep
            </button>
          
        </div>

        {loading ? (
          <p className="text-center text-blue-600">Loading...</p>
        ) : todaySleepData.length === 0 ? (
          <EmptyDataPrompt message="Add your sleep Record for yesterday" />
        ) : (
          tableComponent
        )}
      </div>

      {/* Add Form Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-blue-500 bg-opacity-50 flex justify-center items-center z-50 h-screen">
          <form
            onSubmit={(e) => {
              e.preventDefault();
              onSubmit();
              setShowForm(false);
            }}
            className="bg-white p-6 rounded-lg shadow max-w-md w-full max-h-screen overflow-auto space-y-4"
          >
            <FormComponent formData={formData} onChange={handleChange} />
            <div className="flex flex-col sm:flex-row justify-end gap-2">
              <button
                type="button"
                onClick={() => setShowForm(false)}
                className="cursor-pointer bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-full"
              >
                Close
              </button>
              <button
                type="submit"
                className="cursor-pointer bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full"
              >
                Submit
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Edit Form Modal */}
      {editData && (
        <div className="fixed inset-0 bg-blue-500 bg-opacity-50 flex justify-center items-center z-50 p-4 h-screen">
          <form
            onSubmit={(e) => {
              e.preventDefault();
              onUpdate();
            }}
            className="bg-white p-6 rounded-lg shadow max-w-md w-full max-h-screen overflow-auto space-y-4"
          >
            <FormComponent formData={editData} onChange={handleChange} />
            <div className="flex flex-col sm:flex-row justify-end gap-2">
              <button
                type="button"
                onClick={() => setEditData(null)}
                className="cursor-pointer bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-full"
              >
                Close
              </button>
              <button
                type="submit"
                className="cursor-pointer bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full"
              >
                Update
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Chart / History Section */}
      <div className="bg-white sm:p-4 rounded-lg shadow">
        <div className="p-4 flex flex-wrap gap-2">
          <button
            onClick={() => setView("chart")}
            className={`cursor-pointer px-4 py-2 rounded-md ${
              view === "chart"
                ? "bg-gray-700 text-white"
                : "bg-gray-300 text-gray-700"
            }`}
          >
            Chart
          </button>
          <button
            onClick={() => setView("history")}
            className={`cursor-pointer px-4 py-2 rounded-md ${
              view === "history"
                ? "bg-gray-700 text-white"
                : "bg-gray-300 text-gray-700"
            }`}
          >
            Previous Logs
          </button>
        </div>

        <div className="p-2 sm:p-4">
          {view === "chart" ? (
            records.length > 0 ? (
              chartComponent
            ) : (
              <div className="w-full bg-yellow-50 text-yellow-700 p-4 rounded-md shadow-sm border border-yellow-300 flex items-start gap-3">
                <BarChart3 className="w-6 h-6 mt-1 text-yellow-700" />
                <div>
                  <p className="font-medium">
                    No Previous data available for chart
                  </p>
                </div>
              </div>
            )
          ) : loading ? (
            <p className="text-blue-600">Loading...</p>
          ) : records.length > 0 ? (
            <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm">
              {previousTable}
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

export default SleepSectionTemplate;
