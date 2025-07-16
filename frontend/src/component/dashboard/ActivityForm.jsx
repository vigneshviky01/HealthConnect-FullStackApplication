import React from "react";
import { FaRunning } from "react-icons/fa";

const ActivityForm = ({ formData, onChange }) => {
  return (
    <div className="space-y-4">
      {/* Heading with Icon */}
      <div className="flex items-center gap-2 text-xl font-semibold mb-2">
        <FaRunning className="text-blue-600" />
        <span>Activity Tracker</span>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">

        <div>
          <label className="input-label-style">Workout Type</label>
          <input
            type="text"
            name="workoutType"
            value={formData.workoutType}
            onChange={onChange}
            placeholder="e.g. Walking, Running"
            required
            className="input-field-style"
          />
        </div>

        <div>
          <label className="input-label-style">Steps Taken</label>
          <input
            type="number"
            name="stepsCount"
            value={formData.stepsCount}
            onChange={onChange}
            placeholder="e.g. 5000"
            className="input-field-style"
          />
        </div>

        <div>
          <label className="input-label-style">Calories Burned</label>
          <input
            type="number"
            name="caloriesBurned"
            value={formData.caloriesBurned}
            onChange={onChange}
            placeholder="e.g. 250"
            className="input-field-style"
          />
        </div>

        <div>
          <label className="input-label-style">Duration (minutes)</label>
          <input
            type="number"
            name="workoutDurationMinutes"
            value={formData.workoutDurationMinutes}
            onChange={onChange}
            placeholder="e.g. 45"
            className="input-field-style"
          />
        </div>

        <div>
          <label className="input-label-style">Distance (km)</label>
          <input
            type="number"
            name="distanceKm"
            value={formData.distanceKm}
            onChange={onChange}
            placeholder="e.g. 5.5"
            step="0.1"
            className="input-field-style"
          />
        </div>

        <div className="sm:col-span-2">
          <label className="input-label-style">Notes</label>
          <textarea
            name="notes"
            value={formData.notes}
            onChange={onChange}
            placeholder="e.g. Morning run with interval training"
            className="input-field-style"
            rows={3}
          />
        </div>
      </div>
    </div>
  );
};

export default ActivityForm;
