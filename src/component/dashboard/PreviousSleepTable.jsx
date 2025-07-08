
import React, { useState } from "react";
import { StickyNote } from "lucide-react";
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
const formatDate = (isoString) => {
  return new Date(isoString).toISOString().split("T")[0];
};

const PreviousSleepTable = ({ data }) => {
  const [ascending, setAscending] = useState(true);

  const sortedRecords = [...data].sort((a, b) =>
    ascending
      ? new Date(a.createdAt) - new Date(b.createdAt)
      : new Date(b.createdAt) - new Date(a.createdAt)
  );

  return (
    <div className="relative mt-4">
      <div className="flex justify-end mb-4">
        <button
          onClick={() => setAscending(!ascending)}
          className="px-4 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700"
        >
          Sort by Date {ascending ? "▼" : "▲"}
        </button>
      </div>

      
      <div className="overflow-x-auto">
        <table className="min-w-[700px] w-full border-collapse text-purple-600">
          <thead className="bg-gray-100 text-left">
            <tr>
              <th className="px-4 py-2">Date</th>
              <th className="px-4 py-2">Duration</th>
              <th className="px-4 py-2">Sleep Quality</th>
               <th className="px-4 py-2">Notes</th>
            </tr>
          </thead>
          <tbody>
            {sortedRecords.map((record) => (
              <tr key={record.id} className="border-b border-blue-600 hover:bg-gray-50 transition text-black">
                <td className="px-4 py-2">{formatDate(record.createdAt)}</td>
                <td className="px-4 py-2">{calculateSleepDuration(record.startTime, record.endTime)}</td>
                <td className="px-4 py-2">{record.qualityRating} / 5</td>
                <td className="px-4 py-2">
                  {record.notes ? (
                    <span
                      title={record.notes}
                      className="inline-flex justify-center items-center cursor-pointer"
                    >
                      <StickyNote size={18} className="text-purple-500" />
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

export default PreviousSleepTable;