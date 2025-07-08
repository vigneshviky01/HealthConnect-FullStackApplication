import React, { useState } from "react";

const PreviousWaterTable = ({ data }) => {
  const [ascending, setAscending] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const recordsPerPage = 10;

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
    </div>
  );
};

export default PreviousWaterTable;
