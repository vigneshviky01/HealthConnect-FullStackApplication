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

const MonthlyWaterChart = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchMonthlyWater = async () => {
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

        

          const avgLiters =
            weekData.totalWaterIntake 
              ? parseFloat((weekData.totalWaterIntake / 7).toFixed(2))
              : 0;

          return {
            week: weekLabel,
            avgLiters,
          };
        });

        setData(formatted);
      } catch (err) {
        console.error("Error fetching water data:", err);
      }
    };

    fetchMonthlyWater();
  }, []);

  return (
    <div className="w-full h-[350px]">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="week" />
          <YAxis
            label={{
              value: "Avg Liters",
              angle: -90,
              position: "insideLeft",
            }}
          />
          <Tooltip />
          <Line
            type="monotone"
            dataKey="avgLiters"
            name="Avg Water Intake (L)"
            stroke="#0ea5e9"
            strokeWidth={2}
            dot={{ r: 5, strokeWidth: 2, fill: "#fff" }}
            activeDot={{ r: 7 }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default MonthlyWaterChart;
