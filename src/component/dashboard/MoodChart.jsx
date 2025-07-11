import React, { useMemo } from "react";
import {
  ResponsiveContainer,
  LineChart,
  Line,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Label,
} from "recharts";

// Mood level descriptions without emojis
const moodLabelMap = {
  1: "Very Bad",
  2: "Bad",
  3: "Neutral",
  4: "Good",
  5: "Excellent",
};

const MoodChart = ({ data }) => {
  const chartData = useMemo(() => {
    return data
      .filter((entry) => entry.moodRating !== undefined)
      .map(({ moodDate, moodRating }) => ({
        date: moodDate,
        moodValue: parseInt(moodRating),
      }))
      .sort((a, b) => new Date(a.date) - new Date(b.date))
      .slice(-7); // Show last 30 entries
  }, [data]);

  return (
    <div className="w-full bg-white p-4 rounded-lg shadow-sm overflow-x-auto">
      <h3 className="text-lg font-semibold mb-4 text-blue-600">
        Mood Trend (Last 7 Days)
      </h3>
      <div className="min-w-[400px]" style={{ height: "280px" }}>
        <ResponsiveContainer width="100%" height="100%">
          <LineChart
            data={chartData}
            margin={{ top: 20, right: 20, left: 10, bottom: 40 }}
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
                value="Date"
                offset={-30}
                position="insideBottom"
                style={{ fill: "#555" }}
              />
            </XAxis>
            <YAxis
              domain={[1, 5]}
              allowDecimals={false}
              tickFormatter={(value) => moodLabelMap[value] || value}
              tick={{ fontSize: 14 }}
            >
              <Label
                value="Mood"
                angle={-90}
                position="insideLeft"
                style={{ fill: "#555" }}
                 offset={-5}
              />
            </YAxis>
            <Tooltip
              formatter={(value) => [`${moodLabelMap[value]}`, "Mood"]}
              labelStyle={{ fontWeight: "bold" }}
            />
            <Line
              type="monotone"
              dataKey="moodValue"
              stroke="#3b82f6"
              strokeWidth={3}
              dot={{ r: 5 }}
              name="Mood"
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default MoodChart;
