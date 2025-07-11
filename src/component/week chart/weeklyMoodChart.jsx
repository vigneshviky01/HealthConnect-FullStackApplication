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

const moodLabelMap = {
  1: "Very Bad",
  2: "Bad",
  3: "Neutral",
  4: "Good",
  5: "Very Good",
};

const WeeklyMoodChart = () => {
  const [moodData, setMoodData] = useState([]);

  useEffect(() => {
    const fetchMoodData = async () => {
      const token = sessionStorage.getItem("authToken");
      if (!token) return;

      try {
        const res = await axios.get("http://localhost:8080/api/mood", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const moods = res.data;

        const today = new Date();
        const last7Days = [...Array(7)].map((_, i) => {
          const d = new Date(today);
          d.setDate(today.getDate() - i);
          return d.toISOString().split("T")[0];
        }).reverse();

        const initialData = last7Days.map(date => ({
          date,
          moodRating: 0,
        }));

        moods.forEach((item) => {
          const date = item.moodDate;
          const match = initialData.find((entry) => entry.date === date);

          if (match) {
            match.moodRating = item.moodRating;
          }
        });

        setMoodData(initialData);
      } catch (err) {
        console.error("Error fetching mood data:", err);
      }
    };

    fetchMoodData();
  }, []);

  return (
    <div className="max-w-4xl  mt-4 p-4 rounded-xl bg-white space-y-6">
      <h2 className="text-xl font-semibold text-blue-500">Mood (Last 7 Days)</h2>
      <div className="w-full h-[300px]">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={moodData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis
              domain={[1, 5]}
              tickFormatter={(value) => moodLabelMap[value] || ""}
              ticks={[1, 2, 3, 4, 5]}
              label={{
               
                angle: -90,
                position: "insideLeft",
              }}
            />
            <Tooltip
              formatter={(value) => moodLabelMap[value] || value}
              labelFormatter={(label) => `Date: ${label}`}
            />
            <Line
              type="monotone"
              dataKey="moodRating"
              name="Mood Rating"
              stroke="#0ea5e9"
              strokeWidth={2}
              dot={{ r: 5, strokeWidth: 2, fill: "#fff" }}
              activeDot={{ r: 7 }}
              isAnimationActive={true}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default WeeklyMoodChart;

