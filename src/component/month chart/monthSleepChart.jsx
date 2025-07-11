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

const MonthlySleepChart = () => {
  const [monthlySleepData, setMonthlySleepData] = useState([]);

  useEffect(() => {
    const fetchMonthlySleep = async () => {
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
            avgSleepHours: (weekData.totalSleepHours / 7).toFixed(2) || 0,
          };
        });

        setMonthlySleepData(formattedData);
      } catch (err) {
        console.error("Error fetching monthly sleep metrics:", err);
      }
    };

    fetchMonthlySleep();
  }, []);
  return (
    <div className="max-w-4xl  mt-4 p-6 bg-white rounded-xl space-y-6">


      <div className="w-full h-[350px]">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={monthlySleepData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="week" />
            <YAxis
              label={{
                value: "Avg Hours",
                angle: -90,
                position: "insideLeft",
              }}
            />
            <Tooltip />
            <Line
              type="monotone"
              dataKey="avgSleepHours"
              name="Avg Sleep Duration (hrs)"
              stroke="#06b6d4"
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

export default MonthlySleepChart;
