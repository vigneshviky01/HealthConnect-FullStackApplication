import React, { useMemo } from "react";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Label, ResponsiveContainer } from "recharts";

const ActivityChart = ({ data }) => {

  //add all the duration for a date and do the mapping then covert minutes into hours and take atmost recent 7 days data
  const chartData = useMemo(() => {
    const dateMap = {};
    data.forEach((record) => {
      const date = record.date;
      const durationMin = parseInt(record.duration);
      if (!dateMap[date]) dateMap[date] = 0;
      dateMap[date] += durationMin;
    });

    return Object.entries(dateMap)
      .map(([date, minutes]) => ({ date, hours: minutes / 60 }))
      .sort((a, b) => new Date(b.date) - new Date(a.date))
      .slice(0, 7)
      .reverse();
  }, [data]);

  return (
    <div className="w-full bg-gray-100 p-4 rounded-md shadow-sm">
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date">
            <Label value="Date" offset={-5} position="insideBottom" />
          </XAxis>
          <YAxis>
            <Label value="Hours" angle={-90} position="insideLeft" />
          </YAxis>
          <Tooltip />
          <Bar dataKey="hours" fill="#3b82f6" radius={[8, 8, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default ActivityChart;