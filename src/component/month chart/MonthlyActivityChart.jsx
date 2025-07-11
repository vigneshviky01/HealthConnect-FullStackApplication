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
            ? parseFloat((weekData.totalWorkoutMinutes/7).toFixed(2))
            : 0,
          calories: weekData.totalCalories
            ? parseFloat((weekData.totalCalories/7).toFixed(2))
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

  return (
    <div className="max-w-5xl  mt-2 p-4 sm:p-6 rounded-xl space-y-6 bg-white ">
      {/* Toggle Buttons */}
      <div className="flex flex-col sm:flex-row sm:justify-start gap-4">
        <button
          onClick={() => setActiveChart("duration")}
          className={`px-4 py-2 rounded-lg font-semibold transition ${
            activeChart === "duration" ? "bg-green-600 text-white" : "bg-gray-200"
          }`}
        >
          Avg Duration
        </button>
        <button
          onClick={() => setActiveChart("calories")}
          className={`px-4 py-2 rounded-lg font-semibold transition ${
            activeChart === "calories" ? "bg-indigo-600 text-white" : "bg-gray-200"
          }`}
        >
          Avg Calories 
        </button>
      </div>

      {/* Chart */}
      <div className="w-full h-[350px]">
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
            <Tooltip />
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
              dot={{ r: 5, strokeWidth: 2, fill: "#fff" }}
              activeDot={{ r: 7 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default MonthlyActivityChart;
