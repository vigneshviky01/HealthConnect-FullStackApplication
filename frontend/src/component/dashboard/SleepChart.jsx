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

// Format ISO string to "YYYY-MM-DD" (adjusted to show sleep date = createdAt - 1 day)
const formatDate = (isoString) => {
  const date = new Date(isoString);
  date.setDate(date.getDate() - 1); // subtract 1 day
  return date.toISOString().split("T")[0];
};

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
      hours: (getDurationInMinutes(entry.startTime, entry.endTime) / 60).toFixed(2),
    }));
  }, [recentData]);

  return (
    <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm overflow-x-auto">
      <div className="min-w-[400px]" style={{ height: "280px" }}>
        <ResponsiveContainer width="100%" height="100%">
          <BarChart
            data={durationData}
            margin={{ top: 20, right: 20, left: 10, bottom: 30 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis
              dataKey="date"
              angle={-45}
              textAnchor="end"
              interval={0}
              height={60}
            >
              <Label
                value="Sleep Date"
                offset={-30}
                position="insideBottom"
                style={{ fill: "#555" }}
              />
            </XAxis>
            <YAxis
              label={{
                value: "Hours Slept",
                angle: -90,
                position: "insideLeft",
                offset: 10,
                style: { fill: "#555" },
              }}
            />
            <Tooltip
              formatter={(value) => [`${value} hrs`, "Sleep Duration"]}
              labelFormatter={(label) => `Sleep Date: ${label}`}
            />
            <Bar
              dataKey="hours"
              fill="#3b82f6"
              radius={[8, 8, 0, 0]}
              stroke="none"
            />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default SleepChart;
