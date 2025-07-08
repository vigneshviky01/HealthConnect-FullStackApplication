


import React from "react";
import { FaBed } from "react-icons/fa";

// Format Date to YYYY-MM-DD
const formatDate = (date) => date.toISOString().split("T")[0];

// Get today's and yesterday's date strings
const today = formatDate(new Date());
const yesterday = formatDate(new Date(Date.now() - 86400000));

const SleepForm = ({ formData, onChange }) => {
  return (
    <div className="space-y-4">
      {/* Heading with Icon */}
      <div className="flex items-center gap-2 text-xl font-semibold mb-2">
        <FaBed className="text-blue-600" />
        <span>Sleep Tracker</span>
      </div>

      {/* Sleep From */}
      <label className="input-label-style">Sleep From</label>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        {/* Start Time */}
        <div>
          <div className="relative">
            <input
              type="time"
              name="startTime"
              value={formData.startTime}
              onChange={onChange}
              className="input-field-style pl-10"
              step="60"
              required
            />
          </div>
        </div>

        {/* Start Date */}
        <div>
          <input
            type="date"
            name="sleepStartDate"
            value={formData.sleepStartDate}
            onChange={onChange}
            className="input-field-style"
            required
            min={yesterday}
            max={today}
          />
        </div>
      </div>

      {/* Sleep To */}
      <label className="input-label-style">Sleep To</label>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        {/* End Time */}
        <div>
          <div className="relative">
            <input
              type="time"
              name="endTime"
              value={formData.endTime}
              onChange={onChange}
              className="input-field-style pl-10"
              step="60"
              required
              min={
                formData.sleepStartDate === formData.sleepEndDate
                  ? formData.startTime
                  : undefined
              }
            />
          </div>
        </div>

        {/* End Date */}
        <div>
          <input
            type="date"
            name="sleepEndDate"
            value={formData.sleepEndDate}
            onChange={onChange}
            className="input-field-style"
            required
            min={formData.sleepStartDate || yesterday}
            max={today}
          />
        </div>
      </div>

      {/* Sleep Quality */}
      <div>
        <label className="input-label-style">Sleep Quality (out of 5)</label>
        <input
          type="number"
          name="qualityRating"
          value={formData.qualityRating}
          onChange={onChange}
          placeholder="e.g. 3"
          className="input-field-style"
          min="0"
          max="5"
          required
        />

        {/* Notes */}
        <div>
          <label className="input-label-style">Notes</label>
          <textarea
            name="notes"
            value={formData.notes}
            onChange={onChange}
            placeholder="Any observations or comments"
            className="input-field-style"
            rows={3}
          />
        </div>
      </div>
    </div>
  );
};

export default SleepForm;
