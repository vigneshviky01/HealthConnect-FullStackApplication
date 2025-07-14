import React from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft } from "lucide-react"; // Optional: for icon

const BackButtonCommon = ({ label = "Go Back" ,profilePage}) => {
  const navigate = useNavigate();

  const handleBack = () => {
    navigate(-1); // Go to previous history entry
  };

  return (
    <button
      onClick={handleBack}
      className={`flex absolute top-5 sm:top-10 left-10 items-center ${profilePage?"text-white":"text-black"} hover:text-shadow-gray-600 cursor-pointer hover:text-shadow-xs font-medium text-xl mb-4`}
    >
      <ArrowLeft className="mr-2" size={18} />
      {label}
    </button>
  );
};

export default BackButtonCommon;
