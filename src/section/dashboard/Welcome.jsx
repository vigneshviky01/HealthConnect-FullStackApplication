import React from 'react'
import { Calendar, TrendingUp } from "lucide-react";

const Welcome = ({name}) => {
  return (
     <div className="mt-24 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Welcome back, {name}!</h1>
          <p className="text-gray-600 mt-1">Here's your health summary for today</p>
        </div>
        <div className="flex items-center gap-4">
          <div className="bg-green-50 text-green-700 border-green-200 rounded-4xl px-3 flex items-center gap-1">
            <TrendingUp className="w-4 h-4 mr-1" />
            On Track
          </div>
        <div className="flex items-center px-2 py-1 shadow-md rounded bg-white">
  <Calendar className="w-4 h-4 mr-2 text-blue-600" />
  {new Date().toLocaleDateString()}
</div>

        </div>
      </div>
  )
}

export default Welcome