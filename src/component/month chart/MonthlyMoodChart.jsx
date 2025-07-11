import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Label,
  ResponsiveContainer,
} from "recharts";

const moodLabelMap = {
  0: "0",
  1: "Very Bad",
  2: "Bad",
  3: "Neutral",
  4: "Good",
  5: "Very Good",
};

const MonthlyMoodChart = () => {
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    const fetchMonthlyMood = async () => {
      const token = sessionStorage.getItem("authToken");
      if (!token) return;

      try {
        const res = await axios.get("http://localhost:8080/api/metrics/monthly/graph", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const rawData = res.data;

        const formatted = rawData.map((weekData, index) => {
          const start = new Date(weekData.startDate);
          const end = new Date(weekData.endDate);
          const weekLabel = `Week ${index + 1} (${start.getDate()}-${end.getDate()})`;

          return {
            week: weekLabel,
            moodValue: weekData.avgMoodRating
              ? parseFloat(weekData.avgMoodRating.toFixed(2))
              : 0,
          };
        });

        setChartData(formatted);
      } catch (err) {
        console.error("Error fetching monthly mood data", err);
      }
    };

    fetchMonthlyMood();
  }, []);

  const CustomTooltip = ({ active, payload, label }) => {
    if (!active || !payload || payload.length === 0) return null;

    const moodValue = payload[0].value;
    const moodText = moodLabelMap[moodValue] || moodValue;

    return (
      <div className="bg-white p-2 border border-gray-200 rounded shadow text-sm max-w-[80px] sm:max-w-[200px]" >
        <p className="font-semibold ">{label}</p>
        <p className="text-indigo-600">Avg Mood: {moodText}</p>
      </div>
    );
  };
  return (
    <div className="w-full bg-white p-4 rounded-lg mt-4">
      <div className="overflow-x-auto">
        <div className="min-w-[500px] h-[200px] sm:h-[300px] md:h-[350px]">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart
              data={chartData}
              margin={{ top: 20, right: 20, left: 10, bottom: 40 }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="week" />
              <YAxis
                domain={[1, 5]}
                allowDecimals={false}
                tickFormatter={(v) => moodLabelMap[Math.round(v)] || ""}
                tick={{ fontSize: 14 }}
              >
                <Label
                  value="Avg Mood"
                  angle={-90}
                  position="insideLeft"
                  offset={-5}
                  style={{ fill: "#555" }}
                />
              </YAxis>
              <Tooltip content={<CustomTooltip />} />
              <Line
                type="monotone"
                dataKey="moodValue"
                stroke="#6366f1"
                strokeWidth={2}
                dot={{ r: 4, strokeWidth: 1.5, fill: "#fff" }}
                activeDot={{ r: 6 }}
                name="Avg Mood"
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default MonthlyMoodChart;

