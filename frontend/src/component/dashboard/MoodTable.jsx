import React from "react";
import { Pencil, StickyNote, Trash2 } from "lucide-react";

const MoodTable = ({ data, onEdit, onDelete }) => {
  return (
    <div className="overflow-x-auto">
      <table className="w-full border-t border-blue-600 mt-2">
        <thead>
          <tr className="text-blue-600 text-left">
            <th className="py-2 px-4">Date</th>
            <th className="py-2 px-4">Mood</th>
            <th className="py-2 px-4">Note</th>
            <th className="py-2 px-4">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td colSpan="7">
              <hr className="border-blue-600" />
            </td>
          </tr>
          {data.map((entry) => (
            <tr key={entry.id} className="border-b border-blue-600">
              <td className="py-2 px-4">{entry.moodDate}</td>
              <td className="py-2 px-4">{entry.moodRating}</td>
              <td className="py-2 px-4"> {entry.notes ? (
                  <span title={entry.notes} className="inline-flex justify-center items-center cursor-pointer">
                    <StickyNote size={18} className="text-blue-500" />
                  </span>
                ) : (
                  "-"
                )}</td>
              <td className="py-2 px-4">
                <div className="flex  items-center gap-3">

                <button onClick={() => onEdit(entry)} className="text-blue-600 hover:text-green-600">
                  <Pencil size={18} />
                </button>
                <button onClick={() => onDelete(entry.id)} className="text-red-600 hover:text-red-800">
                  <Trash2 size={18} />
                </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MoodTable;