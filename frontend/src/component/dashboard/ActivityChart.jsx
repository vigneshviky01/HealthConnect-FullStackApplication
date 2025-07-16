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

    data.forEach(({ activityDate, workoutDurationMinutes }) => {
      const durationMin = parseFloat(workoutDurationMinutes);
      if (!dateMap[activityDate]) dateMap[activityDate] = 0;
      dateMap[activityDate] += durationMin;
    });

    return Object.entries(dateMap)
      .map(([date, minutes]) => ({ date, hours: minutes / 60 }))
      .sort((a, b) => new Date(a.date) - new Date(b.date)) // oldest to latest
      .slice(-7); // last 7 entries
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
                value: "Hours",
                angle: -90,
                position: "insideLeft",
                offset: 0,
                style: { fill: "#555" },
              }}
            />
            <Tooltip
              formatter={(value) => [`${value.toFixed(2)} hrs`, "Duration"]}
              labelFormatter={(label) => `Date: ${label}`}
            />
            <Bar
              dataKey="hours"
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

export default ActivityChart;
