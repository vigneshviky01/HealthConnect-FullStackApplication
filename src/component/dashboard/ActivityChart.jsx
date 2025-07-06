import React, { useMemo } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Label,
  ResponsiveContainer,
} from "recharts";

const ActivityChart = ({ data }) => {
  const chartData = useMemo(() => {
    const dateMap = {};

    data.forEach((record) => {
      const date = record.date;
      const durationMin = parseInt(record.duration, 10);
      if (!dateMap[date]) dateMap[date] = 0;
      dateMap[date] += durationMin;
    });

    return Object.entries(dateMap)
      .map(([date, minutes]) => ({
        date,
        hours: +(minutes / 60).toFixed(2), // round to 2 decimal places
      }))
      .sort((a, b) => new Date(b.date) - new Date(a.date))
      .slice(0, 7)
      .reverse();
  }, [data]);

  return (
    <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm overflow-x-auto">
      <ResponsiveContainer width="100%" minWidth={350} height={300}>
        <BarChart
          data={chartData}
          margin={{ top: 10, right: 20, left: 10, bottom: 40 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis
            dataKey="date"
            angle={-30}
            textAnchor="end"
            interval={0}
            height={60}
          >
            <Label value="Date" position="insideBottom" offset={-20} style={{ fill: "#555" }} />
          </XAxis>
          <YAxis tickFormatter={(v) => `${v}h`}>
            <Label
              value="Hours"
              angle={-90}
              position="insideLeft"
              offset={0}
              style={{ fill: "#555" }}
            />
          </YAxis>
          <Tooltip formatter={(value) => `${value} hrs`} />
          <Bar
            dataKey="hours"
            fill="#3b82f6"
            radius={[8, 8, 0, 0]}
            barSize={35}
          />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default ActivityChart;
