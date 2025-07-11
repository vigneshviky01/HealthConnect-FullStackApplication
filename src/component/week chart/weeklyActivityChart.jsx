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

const WeeklyActivityChart = () => {
  const [activityData, setActivityData] = useState([]);
  const [activeChart, setActiveChart] = useState("duration"); // "duration" or "calories"

  useEffect(() => {
    const fetchActivities = async () => {
      const token = sessionStorage.getItem("authToken");
      if (!token) return;

      try {
        const res = await axios.get("http://localhost:8080/api/activities", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const activities = res.data;

        const today = new Date();
        const last7Days = [...Array(7)].map((_, i) => {
          const d = new Date(today);
          d.setDate(today.getDate() - i);
          return d.toISOString().split("T")[0]; // yyyy-mm-dd
        }).reverse();

        const initialData = last7Days.map(date => ({
          date,
          duration: 0,
          calories: 0,
        }));

        activities.forEach((item) => {
          const date = item.activityDate.split("T")[0];
          const match = initialData.find((entry) => entry.date === date);
          if (match) {
            match.duration += item.workoutDurationMinutes;
            match.calories += item.caloriesBurned;
          }
        });

        setActivityData(initialData);
      } catch (err) {
        console.error("Error fetching activities:", err);
      }
    };

    fetchActivities();
  }, []);

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-white border border-gray-300 rounded-md p-2 text-sm shadow-md max-w-[80px] sm:max-w-[200px]">
          <p className="font-semibold text-gray-700">{label}</p>
          {activeChart === "duration" && <p className="text-green-600">{`Workout Duration: ${payload[0].value} min`}</p>}
          {activeChart === "calories" && <p className="text-indigo-500">{`Calories Burned: ${payload[0].value} `}</p>}
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
          Workout Duration
        </button>
        <button
          onClick={() => setActiveChart("calories")}
          className={`px-4 py-2 rounded-lg font-semibold transition ${activeChart === "calories" ? "bg-indigo-600 text-white" : "bg-gray-200"
            }`}
        >
          Calories Burned
        </button>
      </div>


      <div className="w-full overflow-x-auto">
        <div className="min-w-[500px] h-[200px] sm:h-[300px] md:h-[350px]">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={activityData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis
                label={{
                  value: activeChart === "duration" ? "Minutes" : "Calories",
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
                    ? "Workout Duration (min)"
                    : "Calories Burned"
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

export default WeeklyActivityChart;


