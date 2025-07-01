
import React from "react";
import { Pencil, Trash2 } from "lucide-react";

const ActivityTable = ({ data, onEdit, onDelete }) => {
  return (
    <table className="w-full text-blue-600 mt-4 border-collapse">
      <thead>
        <tr className="font-semibold">
          <th>Activity</th>
          <th>Steps</th>
          <th>Calories</th>
          <th>Duration</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr><td colSpan="5"><hr className="border-blue-600" /></td></tr>
        {data.map((activity, index) => (
          <tr key={activity.id} className="text-center">
            <td className="py-2">{activity.name}</td>
            <td className="py-2">{activity.steps}</td>
            <td className="py-2">{activity.calories}</td>
            <td className="py-2">{activity.duration}</td>
            <td className="py-2">
              <button onClick={() => onEdit({ ...activity, type: activity.name, duration: parseInt(activity.duration) })} className="hover:text-green-600 mr-2">
                <Pencil size={20} />
              </button>
              <button onClick={() => onDelete(activity.id)} className="hover:text-red-600">
                <Trash2 size={20} />
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default ActivityTable;