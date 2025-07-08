import React from "react";
import { Pencil, Trash2 } from "lucide-react";

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
          {data.map((entry) => (
            <tr key={entry.id} className="border-t border-blue-600">
              <td className="py-2 px-4">{entry.moodDate}</td>
              <td className="py-2 px-4">{entry.moodRating}</td>
              <td className="py-2 px-4">{entry.notes}</td>
              <td className="py-2 px-4 flex gap-2">
                <button onClick={() => onEdit(entry)} className="text-blue-600 hover:text-green-600">
                  <Pencil size={18} />
                </button>
                <button onClick={() => onDelete(entry.id)} className="text-red-600 hover:text-red-800">
                  <Trash2 size={18} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MoodTable;