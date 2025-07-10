import React from "react";
import { Pencil, Trash2 } from "lucide-react";

const WaterTable = ({ data, onEdit, onDelete }) => {
  console.log(data)
  return (
    <div className="overflow-x-auto mt-2">
      <table className="min-w-[500px] w-full text-center border-t border-blue-600">
        <thead>
          <tr className="text-blue-600 text-left bg-gray-100">
            <th className="py-2 px-4">Date</th>
            <th className="py-2 px-4">Amount (Liters)</th>
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
            <tr
              key={entry.id}
              className="border-b border-blue-600 text-left hover:bg-gray-50 transition"
            >
              <td className="py-2 px-4">{entry.intakeDate}</td>
              <td className="py-2 px-4">{entry.amountLiters}</td>
              <td className="py-2 px-4">
                <div className="flex items-center gap-3">
                  <button
                    onClick={() => onEdit(entry)}
                    className="text-blue-600 hover:text-green-600"
                    title="Edit"
                  >
                    <Pencil size={18} />
                  </button>
                  <button
                    onClick={() => onDelete(entry.id)}
                    className="text-red-600 hover:text-red-800"
                    title="Delete"
                  >
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

export default WaterTable;
