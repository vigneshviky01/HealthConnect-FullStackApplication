import React, { useState } from "react";

const PreviousActivityTable = ({ data }) => {
  const [ascending, setAscending] = useState(true);

  // Sort data by date
  const sortedRecords = [...data].sort((a, b) =>
    ascending ? new Date(a.date) - new Date(b.date) : new Date(b.date) - new Date(a.date)
  );

  return (
    <div className="relative mt-4">
      <div className="flex justify-end mb-4">
        <button
          onClick={() => setAscending(!ascending)}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
        >
          Sort by Date {ascending ? "▼" : "▲"}
        </button>
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-[700px] w-full border-collapse text-blue-600">
          <thead>
            <tr className="bg-gray-100 text-left">
              <th className="py-2 px-4">Date</th>
              <th className="py-2 px-4">Activity</th>
              <th className="py-2 px-4">Steps</th>
              <th className="py-2 px-4">Calories</th>
              <th className="py-2 px-4">Duration</th>
            </tr>
          </thead>
          <tbody>
            {sortedRecords.map((record) => (
              <tr key={record.id} className="border-b border-blue-600 hover:bg-gray-50 transition text-black">
                <td className="py-2 px-4">{record.date}</td>
                <td className="py-2 px-4">{record.name}</td>
                <td className="py-2 px-4">{record.steps} steps</td>
                <td className="py-2 px-4">{record.calories} cal</td>
                <td className="py-2 px-4">{record.duration}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default PreviousActivityTable;
