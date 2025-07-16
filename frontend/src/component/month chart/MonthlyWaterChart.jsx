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

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-white border border-gray-300 rounded-md p-2 text-sm shadow-md max-w-[80px] sm:max-w-[200px]">
          <p className="font-semibold ">{label}</p>
          <p className="text-sky-600">{`Avg Water Intake: ${payload[0].value} L`}</p>
        </div>
      );
    }

    return null;
  };


  return (
    <div className="w-full bg-white p-4 rounded-lg mt-4">
      <div className="overflow-x-auto">
        <div className="min-w-[500px] h-[200px] sm:h-[300px] md:h-[350px]">
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
              <Tooltip content={<CustomTooltip />} />
              <Line
                type="monotone"
                dataKey="avgLiters"
                name="Avg Water Intake (L)"
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

export default MonthlyWaterChart;
