
import React, { useState } from "react";
import { StickyNote } from "lucide-react";
const PreviousActivityTable = ({ data }) => {
  const [ascending, setAscending] = useState(true);

  // Sort data by date
  const sortedRecords = [...data].sort((a, b) =>
    ascending ? new Date(a.activityDate) - new Date(b.activityDate) : new Date(b.activityDate) - new Date(a.activityDate)
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
              <th className="px-4 py-2">Distance</th>
              <th className="px-4 py-2">Notes</th>
            </tr>
          </thead>
          <tbody>
            {sortedRecords.map((record) => (
              <tr key={record.id} className="border-b border-blue-600 hover:bg-gray-50 transition text-black">
                <td className="py-2 px-4">{record.activityDate}</td>
                <td className="py-2 px-4">{record.workoutType}</td>
                <td className="py-2 px-4">{record.stepsCount==0?"-":`${record.stepsCount}Steps`} </td>
                <td className="py-2 px-4">{record.caloriesBurned} cal</td>
                <td className="py-2 px-4">{record.workoutDurationMinutes}</td>
                <td className="px-4 py-2">{record.distanceKm==0?"-":`${record.distanceKm} Km`}</td>
                <td className="px-4 py-2 text-center">
                  {record.notes ? (
                    <span title={record.notes} className="inline-flex justify-center items-center cursor-pointer">
                      <StickyNote size={18} className="text-blue-500" />
                    </span>
                  ) : (
                    "-"
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default PreviousActivityTable;