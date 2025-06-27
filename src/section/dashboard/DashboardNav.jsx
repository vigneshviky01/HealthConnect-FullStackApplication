import React from "react";
import { useNavigate } from "react-router-dom";
import { Dumbbell, BedDouble, Droplet, Smile } from "lucide-react";

const DashboardNav = () => {
  const navigate = useNavigate();

  const modules = [
    { label: "Activity Tracker", path: "activity", icon: <Dumbbell /> },
    { label: "Sleep Tracker", path: "sleep", icon: <BedDouble /> },
    { label: "Water Intake", path: "water", icon: <Droplet /> },
    { label: "Mood Log", path: "mood", icon: <Smile /> },
  ];

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mt-10">
      {modules.map((mod) => (
        <button
          key={mod.path}
          onClick={() => navigate(`/dashboard/${mod.path}`)}
          className="flex cursor-pointer flex-col items-center justify-center p-6 bg-blue-100 hover:bg-blue-200 rounded-xl shadow-md transition-transform hover:scale-105"
        >
          <div className="text-blue-600 mb-2">{mod.icon}</div>
          <span className="text-blue-900 font-semibold">{mod.label}</span>
        </button>
      ))}
    </div>
  );
};

export default DashboardNav;
