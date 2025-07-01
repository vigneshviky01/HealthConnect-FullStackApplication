

import React, { useState } from "react";



const PreviousActivityTable = ({data}) => {
  const [ascending, setAscending] = useState(true);

  //for sorting based on dates
  const sortedRecords = [...data].sort((a, b) => ascending ? new Date(a.date) - new Date(b.date) : new Date(b.date) - new Date(a.date));

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
      <table className="min-w-full text-blue-600">
        
        <tbody>
          {sortedRecords.map((record) => (
            <tr key={record.id} className="border-b">
              <td className="py-2">{record.date}</td>
              <td className="py-2">{record.name}</td>
              <td className="py-2">{record.steps} steps</td>
              <td className="py-2">{record.calories} cal</td>
              <td className="py-2">{record.duration}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PreviousActivityTable;