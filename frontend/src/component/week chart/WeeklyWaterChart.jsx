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

const WeeklyWaterChart = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchWaterData = async () => {
      const token = sessionStorage.getItem("authToken");
      if (!token) return;

      try {
        const res = await axios.get("http://localhost:8080/api/water", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const raw = res.data;

        const today = new Date();
        const last7Days = [...Array(7)].map((_, i) => {
          const d = new Date(today);
          d.setDate(today.getDate() - i);
          return d.toISOString().split("T")[0];
        }).reverse();

        const initialData = last7Days.map((date) => ({
          date,
          liters: 0,
        }));

        raw.forEach((entry) => {
          const date = entry.intakeDate;
          const match = initialData.find((d) => d.date === date);
          if (match) {
            match.liters += entry.amountLiters;
          }
        });

        setData(initialData);
      } catch (err) {
        console.error("Error fetching water data:", err);
      }
    };

    fetchWaterData();
  }, []);
  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-white border border-gray-300 rounded-md p-2 text-sm shadow-md max-w-[80px] sm:max-w-[200px]">
          <p className="font-semibold text-gray-700">{label}</p>
          <p className="text-blue-600">{`Water Intake: ${payload[0].value} L`}</p>
        </div>
      );
    }

    return null;
  };


  return (
    <div className="w-full overflow-x-auto">
      <div className="min-w-[500px] h-[200px] sm:h-[250px] md:h-[300px]">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis
              label={{
                value: "Liters",
                angle: -90,
                position: "insideLeft",
              }}
            />
            <Tooltip content={<CustomTooltip />} />
            <Line
              type="monotone"
              dataKey="liters"
              name="Water Intake (L)"
              stroke="#3b82f6"
              strokeWidth={2}
              dot={{ r: 4, strokeWidth: 1.5, fill: "#fff" }}
              activeDot={{ r: 6 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default WeeklyWaterChart;
