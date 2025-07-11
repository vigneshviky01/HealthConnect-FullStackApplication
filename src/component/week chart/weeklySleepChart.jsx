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

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-white border border-gray-300 rounded-md p-2 text-sm shadow-md max-w-[80px] sm:max-w-[200px]">
          <p className="font-semibold text-gray-700">{label}</p>
          <p className="text-sky-600">{`Sleep Duration(hrs): ${payload[0].value}`}</p>
        </div>
      );
    }

    return null;
  };

  return (
    <div className="max-w-4xl mt-4 p-4 rounded-xl bg-white space-y-6">
      <h2 className="text-xl font-semibold text-blue-500">
        Sleep Duration (Last 7 Days)
      </h2>

      <div className="w-full overflow-x-auto">
        <div className="min-w-[500px] h-[200px] sm:h-[250px] md:h-[300px]">
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
              <Tooltip content={<CustomTooltip />} />
              <Line
                type="monotone"
                dataKey="sleepHours"
                name="Sleep Duration (hrs)"
                stroke="#0ea5e9"
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

export default WeeklySleepChart;
