import React from "react";
import { Pencil, Trash2 } from "lucide-react";

const ActivityTable = ({ data, onEdit, onDelete }) => {
  return (
    <div className="overflow-x-auto w-full mt-4">
      <table className="min-w-[600px] w-full border-collapse text-blue-600">
        <thead>
          <tr className="font-semibold text-left bg-gray-100">
            <th className="py-2 px-4">Activity</th>
            <th className="py-2 px-4">Steps</th>
            <th className="py-2 px-4">Calories</th>
            <th className="py-2 px-4">Duration</th>
            <th className="py-2 px-4">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td colSpan="5">
              <hr className="border-blue-600" />
            </td>
          </tr>
          {data.map((activity) => (
            <tr
              key={activity.id}
              className="text-left border-b border-blue-600 hover:bg-gray-50 transition text-black"
            >
              <td className="py-2 px-4">{activity.name}</td>
              <td className="py-2 px-4">{activity.steps}</td>
              <td className="py-2 px-4">{activity.calories}</td>
              <td className="py-2 px-4">{activity.duration}</td>
              <td className="py-2 px-4">
                <button
                  onClick={() =>
                    onEdit({
                      ...activity,
                      type: activity.name,
                      duration: parseInt(activity.duration),
                    })
                  }
                  className="hover:text-green-600 mr-2 text-blue-600"
                >
                  <Pencil size={20} />
                </button>
                <button
                  onClick={() => onDelete(activity.id)}
                  className="text-red-600 hover:text-red-800"
                >
                  <Trash2 size={20} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ActivityTable;
