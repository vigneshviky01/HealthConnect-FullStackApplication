import React, { useState, useEffect } from "react";

const PreviousWaterTable = ({ data: initialData }) => {
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
      const date = new Date(entry.intakeDate);
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

  // Group by date and sum amountLiters
  const grouped = data.reduce((acc, curr) => {
    const date = curr.intakeDate;
    if (!acc[date]) {
      acc[date] = { date, amount: 0 };
    }
    acc[date].amount += parseFloat(curr.amountLiters);
    return acc;
  }, {});

  const groupedRecords = Object.values(grouped).map((record) => ({
    ...record,
    amount: record.amount.toFixed(2),
  }));

  const sortedRecords = [...groupedRecords].sort((a, b) =>
    ascending ? new Date(a.date) - new Date(b.date) : new Date(b.date) - new Date(a.date)
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

      {/* Water Intake Table */}
      {currentRecords.length === 0 ? (
        <p className="text-center text-gray-500 mt-4">No water intake records found.</p>
      ) : (
        <>
          <div className="overflow-x-auto">
            <table className="min-w-[400px] w-full border-collapse text-blue-600">
              <thead>
                <tr className="bg-gray-100 text-left">
                  <th className="py-2 px-4">Date</th>
                  <th className="py-2 px-4">Water Intake</th>
                </tr>
              </thead>
              <tbody>
                {currentRecords.map((record) => (
                  <tr
                    key={record.date}
                    className="border-b border-blue-600 text-black hover:bg-gray-50 transition"
                  >
                    <td className="py-2 px-4">{record.date}</td>
                    <td className="py-2 px-4">{record.amount} litres</td>
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

export default PreviousWaterTable;
