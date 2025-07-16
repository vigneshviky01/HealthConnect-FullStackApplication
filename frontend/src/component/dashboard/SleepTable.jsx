
import React from "react";
import { Pencil, Trash2, Bed,StickyNote } from "lucide-react";

// Format ISO string to HH:mm
const formatTime = (iso) => {
  const date = new Date(iso);
  return date.toTimeString().slice(0, 5); // "HH:mm"
};
const formatDate = (iso) => {
  return new Date(iso).toISOString().split("T")[0];
};

//sleep table
const SleepTable = ({ data, onEdit, onDelete }) => {
  if (!data || data.length === 0) {
    return (
      <p className="text-center text-blue-600 font-medium">
        No sleep record for today.
      </p>
    );
  }

  // Assuming only one entry for the current day
  const entry = data[0];

  return (
   
      <div className="overflow-x-auto w-full mt-4">
        <table className="min-w-[600px] w-full border-collapse text-blue-600">
          <thead>
            <tr className="font-semibold text-left bg-gray-100">
              <th className="p-2">Sleep</th>
              <th className="p-2">From</th>
              <th className="p-2">To</th>
              <th className="p-2">Quality</th>
              <th className="p-2">Notes</th>
              <th className="p-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr>
            <td colSpan="6">
              <hr className="border-blue-600" />
            </td>
          </tr>
            <tr
              key={entry.id}
              className="text-left border-b border-blue-600 hover:bg-gray-50 transition text-black"
              style={{ borderBottom: "8px solid white" }}
            >
              <td className="p-2 flex items-center gap-2">
                <Bed size={18} />
                Sleep
              </td>
              <td className="p-2">{formatTime(entry.startTime)}</td>
              <td className="p-2">{formatTime(entry.endTime)}</td>
              <td className="p-2">{entry.qualityRating} / 5</td>
              <td className="p-2">
                {entry.notes ? (
                  <span title={entry.notes} className="inline-flex justify-center items-center cursor-pointer">
                    <StickyNote size={18} className="text-blue-300" />
                  </span>
                ) : (
                  "-"
                )}
              </td>
              <td className="p-2">
                <button
                  onClick={() => onEdit({
              ...entry,
              startTime: formatTime(entry.startTime),
              endTime: formatTime(entry.endTime),
              sleepStartDate:formatDate(entry.startTime),
              sleepEndDate:formatDate(entry.endTime)


            })}
                  className="text-blue-500 hover:text-green-200 mr-2"
                >
                  <Pencil size={20} />
                </button>
                <button
                  onClick={() => onDelete(entry.id)}
                  className="text-red-600 hover:text-red-800"
                >
                  <Trash2 size={20} />
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      

     
    </div>
  );
};

export default SleepTable;