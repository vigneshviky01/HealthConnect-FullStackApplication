
import React, { useMemo } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Label,
  ResponsiveContainer,
} from "recharts";

// Calculate duration between startTime and endTime in minutes
const getDurationInMinutes = (start, end) => {
  const startDate = new Date(start);
  const endDate = new Date(end);
  const diffMs = endDate - startDate;
  return Math.floor(diffMs / 60000); // in minutes
};

// Format ISO string to "YYYY-MM-DD"
const formatDate = (isoString) => isoString.split("T")[0];

const SleepChart = ({ data }) => {
  // Prepare data using `createdAt` as the reference date
  const recentData = useMemo(() => {
    return [...data]
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      .slice(0, 7)
      .reverse(); 
  }, [data]);

  const durationData = useMemo(() => {
    return recentData.map((entry) => ({
      date: formatDate(entry.createdAt),
      hours: getDurationInMinutes(entry.startTime, entry.endTime) / 60,
    }));
  }, [recentData]);

  return (
    <div className="w-full bg-gray-100 p-3 sm:p-4 rounded-md shadow-sm">

      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={durationData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date" fontSize={12}>
            <Label value="Date" offset={-5} position="insideBottom" />
          </XAxis>
          <YAxis fontSize={12}>
            <Label value="Hours Slept" angle={-90} position="insideLeft" />
          </YAxis>
          <Tooltip />
          <Bar dataKey="hours" fill="#3b82f6" radius={[8, 8, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default SleepChart;
