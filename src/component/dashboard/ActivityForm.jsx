import React from "react";

const ActivityForm = ({ formData, onChange }) => {


  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
      <div>
        <label className="input-label-style">Workout Type</label>
        <input
          type="text"
          name="type"
          value={formData.type}
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
          name="steps"
          value={formData.steps}
          onChange={onChange}
          placeholder="e.g. 5000"
          className="input-field-style"
        />
      </div>

      <div>
        <label className="input-label-style">Calories Burned</label>
        <input
          type="number"
          name="calories"
          value={formData.calories}
          onChange={onChange}
          placeholder="e.g. 250"
          className="input-field-style"
        />
      </div>

      <div>
        <label className="input-label-style">Duration (minutes)</label>
        <input
          type="number"
          name="duration"
          value={formData.duration}
          onChange={onChange}
          placeholder="e.g. 45"
          className="input-field-style"
        />
      </div>
    </div>
  );
};

export default ActivityForm;
