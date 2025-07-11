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

const WeeklySleepChart = () => {
  const [sleepData, setSleepData] = useState([]);

  useEffect(() => {
    const fetchSleepData = async () => {
      const token = sessionStorage.getItem("authToken");
      if (!token) return;

      try {
        const res = await axios.get("http://localhost:8080/api/sleep", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const sleeps = res.data;

        const today = new Date();
        const last7Days = [...Array(7)].map((_, i) => {
          const d = new Date(today);
          d.setDate(today.getDate() - i);
          return d.toISOString().split("T")[0];
        }).reverse();

        const initialData = last7Days.map(date => ({
          date,
          sleepHours: 0,
        }));

        sleeps.forEach((item) => {
          const date = item.createdAt.split("T")[0];
          const match = initialData.find((entry) => entry.date === date);

          if (match) {
            const start = new Date(item.startTime);
            const end = new Date(item.endTime);
            const durationHours = (end - start) / (1000 * 60 * 60); // ms to hours
            match.sleepHours += parseFloat(durationHours.toFixed(2));
          }
        });

        setSleepData(initialData);
      } catch (err) {
        console.error("Error fetching sleep data:", err);
      }
    };

    fetchSleepData();
  }, []);

  return (
    <div className="max-w-4xl  mt-4 p-4 rounded-xl bg-white space-y-6">
      <h2 className="text-xl font-semibold text-blue-500">Sleep Duration (Last 7 Days)</h2>
      <div className="w-full h-[300px]">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={sleepData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis
              label={{
                value: "Hours",
                angle: -90,
                position: "insideLeft",
              }}
            />
            <Tooltip />
            <Line
              type="monotone"
              dataKey="sleepHours"
              name="Sleep Duration (hrs)"
              stroke="#0ea5e9"
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

export default WeeklySleepChart;
