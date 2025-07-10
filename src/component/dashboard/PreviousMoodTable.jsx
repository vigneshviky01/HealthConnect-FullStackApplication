import React, { useState, useEffect } from "react";
import axios from "axios";
import { toast } from "react-toastify";

const ratingMap = {
  1: "Very Bad",
  2: "Bad",
  3: "Neutral",
  4: "Good",
  5: "Excellent",
};

const PreviousMoodTable = ({ data: initialData }) => {
  const [data, setData] = useState([]);
  const [ascending, setAscending] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [loading, setLoading] = useState(false);

  const recordsPerPage = 10;
  const token = sessionStorage.getItem("authToken");

  // ✅ use props data on first load
  useEffect(() => {
    setData(Array.isArray(initialData) ? initialData : []);
  }, [initialData]);

  // ✅ fetch filtered data only on button click
  const fetchMoodData = async () => {
    try {
      if (!startDate && !endDate) return;

      setLoading(true);
      const params = {};
      if (startDate) params.startDate = startDate;
      if (endDate) params.endDate = endDate;


    const res = await axios.get("http://localhost:8080/api/mood", {
      params,
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log(res)
      setData(Array.isArray(res.data) ? res.data : []);
      setCurrentPage(1);
    } catch (err) {
      console.error("Error fetching moods:", err);
      toast.error("Failed to load filtered mood data.");
    } finally {
      setLoading(false);
    }
  };
const resetFilter = () => {
  setStartDate("");
  setEndDate("");
  setData(Array.isArray(initialData) ? initialData : []);
  setCurrentPage(1);
  setAscending(true);
};

  const grouped = data.reduce((acc, item) => {
    if (!item.moodDate) return acc;
    if (!acc[item.moodDate]) acc[item.moodDate] = [];
    acc[item.moodDate].push(item);
    return acc;
  }, {});

  const groupedRecords = Object.entries(grouped)
    .map(([moodDate, moods]) => ({ moodDate, moods }))
    .sort((a, b) =>
      ascending
        ? new Date(a.moodDate) - new Date(b.moodDate)
        : new Date(b.moodDate) - new Date(a.moodDate)
    );

  const totalPages = Math.ceil(groupedRecords.length / recordsPerPage);
  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = groupedRecords.slice(indexOfFirstRecord, indexOfLastRecord);

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
    onClick={fetchMoodData}
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

      {/* Mood Table */}
      {loading ? (
        <p className="text-center text-blue-600">Loading...</p>
      ) : currentRecords.length === 0 ? (
        <p className="text-center text-gray-500 mt-4">No mood records found.</p>
      ) : (
        <>
          <div className="overflow-x-auto">
            <table className="min-w-[400px] w-full border-collapse text-blue-600">
              <thead>
                <tr className="bg-gray-100 text-left">
                  <th className="py-2 px-4">Date</th>
                  <th className="py-2 px-4">Mood Rating</th>
                  <th className="py-2 px-4">Notes</th>
                </tr>
              </thead>
              <tbody>
                {currentRecords.map(({ moodDate, moods }) =>
                  moods.map((mood, idx) => (
                    <tr
                      key={`${mood.id}-${idx}`}
                      className="border-b border-blue-600 text-black hover:bg-gray-50 transition"
                    >
                      <td className="py-2 px-4">{idx === 0 ? moodDate : ""}</td>
                      <td className="py-2 px-4">
                        {ratingMap[mood.moodRating] || "N/A"}
                      </td>
                      <td className="py-2 px-4">{mood.notes}</td>
                    </tr>
                  ))
                )}
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

export default PreviousMoodTable;
