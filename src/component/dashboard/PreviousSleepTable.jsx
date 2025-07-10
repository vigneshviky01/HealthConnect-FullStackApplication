import React, { useEffect, useState } from "react";

// Helper to calculate duration between two ISO timestamps
const calculateSleepDuration = (startTime, endTime) => {
  const start = new Date(startTime);
  const end = new Date(endTime);

  let diffMinutes = Math.floor((end - start) / (1000 * 60));
  if (diffMinutes < 0) diffMinutes += 24 * 60; // cross-midnight fix

  const hours = Math.floor(diffMinutes / 60);
  const minutes = diffMinutes % 60;

  return `${hours}h ${minutes}m`;
};

// Format ISO string to YYYY-MM-DD
// const formatDate = (isoString) => new Date(isoString).toISOString().split("T")[0];
const formatDate = (isoString) => {
  const date = new Date(isoString);
  date.setDate(date.getDate() - 1); // subtract one day
  return date.toISOString().split("T")[0];
};


const PreviousSleepTable = ({ data: initialData }) => {
  const [data, setData] = useState([]);
  const [ascending, setAscending] = useState(true);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [currentPage, setCurrentPage] = useState(1);

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
    const date = new Date(entry.createdAt);
    date.setDate(date.getDate() - 1); // Shift to match display logic

    const start = startDate ? new Date(startDate) : null;
    const end = endDate
      ? new Date(new Date(endDate).setHours(23, 59, 59, 999))
      : null;

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
      ? new Date(a.createdAt) - new Date(b.createdAt)
      : new Date(b.createdAt) - new Date(a.createdAt)
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
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md"
          >
            Filter
          </button>
          <button
            onClick={resetFilter}
            className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md"
          >
            Reset
          </button>
        </div>

        <button
          onClick={() => setAscending(!ascending)}
          className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md"
        >
          Sort by Date {ascending ? "▼" : "▲"}
        </button>
      </div>

      {/* Table */}
      {currentRecords.length === 0 ? (
        <p className="text-center text-gray-500 mt-4">No sleep records found.</p>
      ) : (
        <>
          <div className="overflow-x-auto">
            <table className="min-w-[700px] w-full border-collapse text-blue-600">
              <thead className="bg-gray-100 text-left">
                <tr>
                  <th className="px-4 py-2">Date</th>
                  <th className="px-4 py-2">Duration</th>
                  <th className="px-4 py-2">Sleep Quality</th>
                  <th className="px-4 py-2">Notes</th>
                </tr>
              </thead>
              <tbody>
                {currentRecords.map((record) => (
                  <tr
                    key={record.id}
                    className="border-b border-blue-600 hover:bg-gray-50 transition text-black"
                  >
                    <td className="px-4 py-2">{formatDate(record.createdAt)}</td>
                    <td className="px-4 py-2">
                      {calculateSleepDuration(record.startTime, record.endTime)}
                    </td>
                    <td className="px-4 py-2">{record.qualityRating} / 5</td>
                    <td className="px-4 py-2">{record.notes || "-"}</td>
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
                  : "bg-blue-600 text-white hover:bg-blue-700"
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
                  : "bg-blue-600 text-white hover:bg-blue-700"
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

export default PreviousSleepTable;
