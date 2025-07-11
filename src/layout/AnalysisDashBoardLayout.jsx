import React, { useState } from "react";

const AnalysisDashboardLayout = ({ title, weeklyChart, monthlyChart }) => {
  const [mode, setMode] = useState("week");

  return (
    <div className="max-w-6xl mx-auto mt-10 bg-white rounded-xl shadow p-6 space-y-6">
      {/* Header with toggle & title */}
      <div className="flex flex-col md:flex-row justify-between items-center mb-4">
        {/* View Switch Dropdown */}
        <div className="mb-4 md:mb-0">
          <select
            value={mode}
            onChange={(e) => setMode(e.target.value)}
            className="px-4 py-2 rounded-lg border border-gray-300 text-gray-800 font-semibold focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="week">Week</option>
            <option value="month">Month</option>
          </select>
        </div>

        {/* Title */}
        <h2 className="text-2xl font-bold text-center text-gray-800">
          {title}
        </h2>

        {/* Right Spacer / Placeholder */}
        <div className="w-[120px]"></div>
      </div>

      {/* Chart Section */}
      {mode === "week" ? weeklyChart : monthlyChart}
    </div>
  );
};

export default AnalysisDashboardLayout;

