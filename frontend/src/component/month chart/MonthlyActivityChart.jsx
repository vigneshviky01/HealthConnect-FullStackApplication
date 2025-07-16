import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const MonthlyActivityChart = () => {
  const [monthlyData, setMonthlyData] = useState([]);
  const [activeChart, setActiveChart] = useState("duration"); // or "calories"

  useEffect(() => {
    const fetchMonthlyActivities = async () => {
      const token = sessionStorage.getItem("authToken");
      if (!token) return;

      try {
        const res = await axios.get("http://localhost:8080/api/metrics/monthly/graph", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const rawData = res.data;

        const formattedData = rawData.map((weekData, index) => {
          const start = new Date(weekData.startDate);
          const end = new Date(weekData.endDate);

          const weekLabel = `Week ${index + 1} (${start.getDate()}-${end.getDate()})`;

          return {
            week: weekLabel,
            duration: weekData.totalWorkoutMinutes
              ? parseFloat((weekData.totalWorkoutMinutes / 7).toFixed(2))
              : 0,
            calories: weekData.totalCalories
              ? parseFloat((weekData.totalCalories / 7).toFixed(2))
              : 0,
          };
        });

        setMonthlyData(formattedData);
      } catch (err) {
        console.error("Error fetching monthly activity data:", err);
      }
    };

    fetchMonthlyActivities();
  }, []);

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-white border border-gray-300 rounded-md p-2 text-sm shadow-md max-w-[80px] sm:max-w-[200px]">
          <p className="font-semibold text-gray-700">{label}</p>
          {activeChart === "duration" && <p className="text-green-600">{`Avg Duration: ${payload[0].value} min`}</p>}
          {activeChart === "calories" && <p className="text-indigo-500">{`Avg Calories: ${payload[0].value} `}</p>}
        </div>
      );
    }

    return null;
  };

  return (
    <div className="max-w-5xl mt-2 p-4 sm:p-6 rounded-xl space-y-6 bg-white">

      <div className="flex flex-col sm:flex-row sm:justify-start gap-4">
        <button
          onClick={() => setActiveChart("duration")}
          className={`px-4 py-2 rounded-lg font-semibold transition ${activeChart === "duration" ? "bg-green-600 text-white" : "bg-gray-200"
            }`}
        >
          Avg Duration
        </button>
        <button
          onClick={() => setActiveChart("calories")}
          className={`px-4 py-2 rounded-lg font-semibold transition ${activeChart === "calories" ? "bg-indigo-600 text-white" : "bg-gray-200"
            }`}
        >
          Avg Calories
        </button>
      </div>


      <div className="w-full overflow-x-auto">
        <div className="min-w-[500px] h-[200px] sm:h-[300px] md:h-[350px]">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={monthlyData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="week" />
              <YAxis
                label={{
                  value: activeChart === "duration" ? "Avg Minutes" : "Avg Calories",
                  angle: -90,
                  position: "insideLeft",
                }}
              />
              <Tooltip content={<CustomTooltip />} />
              <Line
                type="monotone"
                dataKey={activeChart}
                name={
                  activeChart === "duration"
                    ? "Avg Duration (min)"
                    : "Avg Calories Burned"
                }
                stroke={activeChart === "duration" ? "#10b981" : "#6366f1"}
                strokeWidth={2}
                dot={{ r: 4, strokeWidth: 1.5, fill: "#fff" }}
                activeDot={{ r: 6 }}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default MonthlyActivityChart;

