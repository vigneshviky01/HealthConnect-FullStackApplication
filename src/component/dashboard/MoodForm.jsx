import React from "react";

const MoodForm = ({ formData, onChange }) => {
  return (
    <div className="grid grid-cols-1 gap-4">
      <div>
        <label className="input-label-style">Select Mood</label>
        <select
          name="mood"
          value={formData.mood}
          onChange={onChange}
          className="input-field-style"
          required
        >
          <option value="">-- Choose your mood --</option>
          <option value="Happy">Happy</option>
          <option value="Sad">Sad</option>
          <option value="Neutral">Neutral</option>
          <option value="Excited">Excited</option>
          <option value="Tired">Tired</option>
        </select>
      </div>

      <div>
        <label className="input-label-style">Note (optional)</label>
        <textarea
          name="note"
          placeholder="Add a note about your mood..."
          value={formData.note}
          onChange={onChange}
          className="input-field-style"
          rows={4}
        />
      </div>
    </div>
  );
};

export default MoodForm;
