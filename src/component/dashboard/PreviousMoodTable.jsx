import React, { useState } from "react";

const PreviousMoodTable = ({ data }) => {
  const [ascending, setAscending] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const recordsPerPage = 10;

  // Group moods by date
  const grouped = data.reduce((acc, item) => {
    if (!acc[item.date]) acc[item.date] = [];
    acc[item.date].push(item);
    return acc;
  }, {});

  const groupedRecords = Object.entries(grouped)
    .map(([date, moods]) => ({ date, moods }))
    .sort((a, b) =>
      ascending ? new Date(a.date) - new Date(b.date) : new Date(b.date) - new Date(a.date)
    );

  const totalPages = Math.ceil(groupedRecords.length / recordsPerPage);
  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = groupedRecords.slice(indexOfFirstRecord, indexOfLastRecord);

  const handleNextPage = () => {
    if (currentPage < totalPages) setCurrentPage((prev) => prev + 1);
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) setCurrentPage((prev) => prev - 1);
  };

  return (
    <div className="relative mt-4">
      <div className="flex flex-col sm:flex-row justify-between items-center mb-4 gap-2">
        <button
          onClick={() => setAscending(!ascending)}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
        >
          Sort by Date {ascending ? "▼" : "▲"}
        </button>

        <div className="flex items-center gap-2">
          <button
            onClick={handlePreviousPage}
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
            onClick={handleNextPage}
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
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-[400px] w-full border-collapse text-blue-600">
          <thead>
            <tr className="bg-gray-100 text-left">
              <th className="py-2 px-4">Date</th>
              <th className="py-2 px-4">Mood</th>
              <th className="py-2 px-4">Note</th>
            </tr>
          </thead>
          <tbody>
            {currentRecords.map(({ date, moods }) =>
              moods.map((mood, idx) => (
                <tr
                  key={mood.id + "-" + idx}
                  className="border-b border-blue-600 text-black hover:bg-gray-50 transition"
                >
                  <td className="py-2 px-4">{idx === 0 ? date : ""}</td>
                  <td className="py-2 px-4">{mood.mood}</td>
                  <td className="py-2 px-4">{mood.note}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default PreviousMoodTable;
