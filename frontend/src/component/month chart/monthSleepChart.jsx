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
  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-white border border-gray-300 rounded-md p-2 text-sm shadow-md max-w-[80px] sm:max-w-[200px]">
          <p className="font-semibold ">{label}</p>
          <p className="text-sky-600">{`Avg Sleep Duration: ${payload[0].value} hrs`}</p>
        </div>
      );
    }

    return null;
  };

  return (
    <div className="max-w-4xl mt-4 p-6 bg-white rounded-xl space-y-6">
      <div className="w-full overflow-x-auto">
        <div className="min-w-[500px] h-[200px] sm:h-[300px] md:h-[350px]">
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
              <Tooltip content={<CustomTooltip />} />
              <Line
                type="monotone"
                dataKey="avgSleepHours"
                name="Avg Sleep Duration (hrs)"
                stroke="#06b6d4"
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

export default MonthlySleepChart;

