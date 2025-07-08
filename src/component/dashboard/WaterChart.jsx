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

    data.forEach(({ intakeDate, amountLiters }) => {
      if (!dateMap[intakeDate]) dateMap[intakeDate] = 0;
      dateMap[intakeDate] += parseFloat(amountLiters);
    });

    return Object.entries(dateMap)
      .map(([date, liters]) => ({ date, liters }))
      .sort((a, b) => new Date(a.date) - new Date(b.date))
      .slice(-7); // last 7 days
  }, [data]);

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
              height={60}
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
                style: { fill: "#555" }
              }}
            />
            <Tooltip
              formatter={(value) => [`${value} L`, "Water Intake"]}
              labelFormatter={(label) => `Date: ${label}`}
            />
            <Bar
              dataKey="liters"
              fill="#3b82f6"
              radius={[8, 8, 0, 0]}
              stroke="none"
            />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default WaterChart;
