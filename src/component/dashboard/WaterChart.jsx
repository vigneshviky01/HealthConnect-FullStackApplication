import React, { useMemo } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Label
} from "recharts";

const WaterChart = ({ data }) => {
  const chartData = useMemo(() => {
    const dateMap = {};
    data.forEach(({ date, amount }) => {
      if (!dateMap[date]) dateMap[date] = 0;
      dateMap[date] += parseFloat(amount);
    });

    return Object.entries(dateMap)
      .map(([date, amount]) => ({ date, liters: amount }))
      .sort((a, b) => new Date(a.date) - new Date(b.date))
      .slice(-7);
  }, [data]);

  // Optional: short date formatter (e.g., Jul 2)
  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return `${date.toLocaleString("default", { month: "short" })} ${date.getDate()}`;
  };

  return (
    <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm overflow-x-auto">
      <div className="min-w-[400px]" style={{ height: "280px" }}>
        <ResponsiveContainer width="100%" height="100%">
          <BarChart
            data={chartData}
            margin={{ top: 20, right: 20, left: 10, bottom: 30 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
           <XAxis
              dataKey="date"
              angle={-45}
              textAnchor="end"
              interval={0}
              height={60} // makes space for long labels
            >
              <Label
                value="Date"
                offset={-30}
                position="insideBottom"
                style={{ fill: "#555" }}
              />
            </XAxis>
            <YAxis
              label={{
                value: "Liters",
                angle: -90,
                position: "insideLeft",
                offset: 10,
                style: { fill: "#555" },
              }}
            />
            <Tooltip
              formatter={(value, name) => [`${value} L`, "Water Intake"]}
              labelFormatter={(label) => `Date: ${label}`}
            />
            <Bar
              dataKey="liters"
              fill="#3b82f6"
              radius={[8, 8, 0, 0]}
              stroke="none" // removes border on hover
            />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default WaterChart;
