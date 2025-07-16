import React, { useEffect, useState } from "react";

const PreviousActivityTable = ({ data: initialData }) => {
  const [data, setData] = useState([]);
  const [ascending, setAscending] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  const recordsPerPage = 10;

  useEffect(() => {
    setData(Array.isArray(initialData) ? initialData : []);
  }, [initialData]);

  const applyFilter = () => {
    if (!startDate && !endDate) {
      setData(initialData);
      return;
    }

    const filtered = initialData.filter((entry) => {
      const date = new Date(entry.activityDate);
      const start = startDate ? new Date(startDate) : null;
      const end = endDate ? new Date(endDate) : null;
      return (!start || date >= start) && (!end || date <= end);
    });

    setData(filtered);
    setCurrentPage(1);
  };

  const resetFilter = () => {
    setStartDate("");
    setEndDate("");
    setData(initialData);
    setCurrentPage(1);
    setAscending(true);
  };

  const sortedRecords = [...data].sort((a, b) =>
    ascending
      ? new Date(a.activityDate) - new Date(b.activityDate)
      : new Date(b.activityDate) - new Date(a.activityDate)
  );

  const totalPages = Math.ceil(sortedRecords.length / recordsPerPage);
  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = sortedRecords.slice(indexOfFirstRecord, indexOfLastRecord);

  return (
    <div className="relative mt-6 p-4 border rounded-md shadow-sm bg-white">
      {/* Filter Controls */}
      <div className="flex flex-col md:flex-row justify-between items-center mb-6 gap-4">
        <div className="flex flex-wrap items-center justify-start gap-4">
          <label className="text-sm font-semibold text-gray-700">
            Start Date:
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              className="ml-2 p-2 border rounded-md text-sm"
            />
          </label>
          <label className="text-sm font-semibold text-gray-700">
            End Date:
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
              className="ml-2 p-2 border rounded-md text-sm"
            />
          </label>
          <button
            onClick={applyFilter}
            className="cursor-pointer bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md"
          >
            Filter
          </button>
          <button
            onClick={resetFilter}
            className="cursor-pointer bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md"
          >
            Reset
          </button>
        </div>

        <button
          onClick={() => setAscending(!ascending)}
          className="cursor-pointer bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md"
        >
          Sort by Date {ascending ? "▼" : "▲"}
        </button>
      </div>

      {/* Table */}
      {currentRecords.length === 0 ? (
        <p className="text-center text-gray-500 mt-4">No activity records found.</p>
      ) : (
        <>
          <div className="overflow-x-auto">
            <table className="min-w-[700px] w-full border-collapse text-blue-600">
              <thead>
                <tr className="bg-gray-100 text-left">
                  <th className="py-2 px-4">Date</th>
                  <th className="py-2 px-4">Activity</th>
                  <th className="py-2 px-4">Steps</th>
                  <th className="py-2 px-4">Calories</th>
                  <th className="py-2 px-4">Duration(mins)</th>
                  <th className="py-2 px-4">Distance(Km)</th>
                  <th className="py-2 px-4">Notes</th>
                </tr>
              </thead>
              <tbody>
                {currentRecords.map((record) => (
                  <tr
                    key={record.id}
                    className="border-b border-blue-600 hover:bg-gray-50 transition text-black"
                  >
                    <td className="py-2 px-4">{record.activityDate}</td>
                    <td className="py-2 px-4">{record.workoutType}</td>
                    <td className="py-2 px-4">
                      {record.stepsCount === 0 ? "-" : `${record.stepsCount} Steps`}
                    </td>
                    <td className="py-2 px-4">{record.caloriesBurned} cal</td>
                    <td className="py-2 px-4">{record.workoutDurationMinutes}</td>
                    <td className="py-2 px-4">
                      {record.distanceKm === 0 ? "-" : `${record.distanceKm} Km`}
                    </td>
                    <td className="py-2 px-4">{record.notes || "-"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Pagination */}
          <div className="mt-4 flex justify-center items-center gap-4">
            <button
              onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
              disabled={currentPage === 1}
              className={`px-4 py-2 rounded-md ${
                currentPage === 1
                  ? "bg-gray-300 text-gray-600 cursor-not-allowed"
                  : "bg-blue-600 text-white hover:bg-blue-700 cursor-pointer"
              }`}
            >
              Prev
            </button>
            <span className="text-sm text-blue-700">
              Page {currentPage} of {totalPages}
            </span>
            <button
              onClick={() => setCurrentPage((p) => Math.min(totalPages, p + 1))}
              disabled={currentPage === totalPages}
              className={`px-4 py-2 rounded-md ${
                currentPage === totalPages
                  ? "bg-gray-300 text-gray-600 cursor-not-allowed"
                  : "bg-blue-600 text-white hover:bg-blue-700 cursor-pointer "
              }`}
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default PreviousActivityTable;
