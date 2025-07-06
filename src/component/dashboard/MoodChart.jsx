// MoodChart.jsx (Responsive and Insightful for 100+ entries)
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



const emojiMap = {
  Happy: "😊",
  Sad: "😢",
  Stressed: "😫",
  Relaxed: "😌",
  Energetic: "💪",
  Tired: "😴",
  Angry: "😠",
};

const moodToNumber = {
  Angry: 0,
  Sad: 1,
  Stressed: 2,
  Tired: 3,
  Relaxed: 4,
  Energetic: 5,
  Happy: 6,
};

const MoodChart = ({ data }) => {
  const chartData = useMemo(() => {
    return data
      .filter((entry) => moodToNumber[entry.mood] !== undefined)
      .map(({ date, mood }) => ({
        date,
        moodValue: moodToNumber[mood],
      }))
      .sort((a, b) => new Date(a.date) - new Date(b.date))
      .slice(-30); // Show last 30 days
  }, [data]);

  return (
    <div className="w-full bg-white p-4 rounded-lg shadow-sm overflow-x-auto">
      <h3 className="text-lg font-semibold mb-4 text-blue-600">Mood Trend (Last 30 Days)</h3>
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
              domain={[0, 6]}
              tickFormatter={(value) => {
                const mood = Object.keys(moodToNumber).find(
                  (key) => moodToNumber[key] === value
                );
                return emojiMap[mood] || value;
              }}
              tick={{ fontSize: 16 }}
            >
              <Label value="Mood" angle={-90} position="insideLeft" />
            </YAxis>
            <Tooltip
              formatter={(value) => {
                const mood = Object.keys(moodToNumber).find(
                  (key) => moodToNumber[key] === value
                );
                return [`${emojiMap[mood]} ${mood}`, "Mood"];
              }}
              labelStyle={{ fontWeight: "bold" }}
            />
            <Line
              type="monotone"
              dataKey="moodValue"
              stroke="#3b82f6"
              strokeWidth={3}
              dot={{ r: 6 }}
              name="Mood"
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default MoodChart;
