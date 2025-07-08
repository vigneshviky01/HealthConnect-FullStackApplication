import React from "react";

const MoodForm = ({ formData, onChange }) => {
  return (
    <div className="grid grid-cols-1 gap-4">
      <div>
        <label className="input-label-style">Mood Rating (1-5)</label>
        <select
          name="moodRating"
          value={formData.moodRating}
          onChange={onChange}
          className="input-field-style"
          required
        >
          <option value="">-- Select Mood Rating --</option>
          <option value="1">😞 1 - Very Bad</option>
          <option value="2">😕 2 - Bad</option>
          <option value="3">😐 3 - Neutral</option>
          <option value="4">😊 4 - Good</option>
          <option value="5">😁 5 - Excellent</option>
        </select>
      </div>

      <div>
        <label className="input-label-style">Notes (optional)</label>
        <textarea
          name="notes"
          placeholder="Write a short note..."
          value={formData.notes}
          onChange={onChange}
          className="input-field-style"
          rows={4}
        />
      </div>
    </div>
  );
};

export default MoodForm;
