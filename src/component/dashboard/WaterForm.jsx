import React from "react";

const WaterForm = ({ formData, onChange }) => {
  return (
    <div className="grid grid-cols-1 gap-4">
      <div>
        <label className="input-label-style">Amount (in Liters)</label>
        <input
          type="number"
          name="amount"
          value={formData.amount}
          onChange={onChange}
          placeholder="e.g. 2.5"
          className="input-field-style"
          required
          min="0"
          step="0.1"
        />
      </div>
    </div>
  );
};

export default WaterForm;