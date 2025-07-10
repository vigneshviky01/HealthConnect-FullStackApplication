import React from "react";
import { Pencil, Trash2, StickyNote } from "lucide-react";

const ActivityTable = ({ data, onEdit, onDelete }) => {
  return (
    <div className="overflow-x-auto w-full mt-4">
      <table className="min-w-[600px] w-full border-collapse text-blue-600">
        <thead>
          <tr className="font-semibold text-left bg-gray-100">
            <th className="py-2 px-4">Activity</th>
            <th className="py-2 px-4">Steps</th>
            <th className="py-2 px-4">Calories</th>
            <th className="py-2 px-4">Duration(mins)</th>
            <th className="p-2">Distance(Km) </th>
            <th className="p-2">Notes</th>
            <th className="py-2 px-4">Actions</th>

          </tr>
        </thead>
        <tbody>
          <tr>
            <td colSpan="7">
              <hr className="border-blue-600" />
            </td>
          </tr>
          {data.map((activity) => (
            <tr
              key={activity.id}
              className="text-left border-b border-blue-600 hover:bg-gray-50 transition text-black"
            >
              <td className="py-2 px-4">{activity.workoutType}</td>
              <td className="py-2 px-4">{activity.stepsCount==0?"-":activity.stepsCount}</td>
              <td className="py-2 px-4">{activity.caloriesBurned}</td>
              <td className="py-2 px-4">{activity.workoutDurationMinutes}</td>
              <td className="py-2 px-4">{activity.distanceKm==0?"-":`${activity.distanceKm} Km`} </td>
              <td className="py-2 px-4">
                {activity.notes ? (
                  <span title={activity.notes} className="inline-flex justify-center items-center cursor-pointer">
                    <StickyNote size={18} className="text-blue-500" />
                  </span>
                ) : (
                  "-"
                )}
              </td>

              <td className="py-2 px-4">
                <button
                  onClick={() =>
                    onEdit({
                      ...activity,
                      type: activity.name,
                      duration: parseInt(activity.workoutDurationMinutes),
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