import React from "react";
import { useNavigate } from "react-router-dom";
import { ArrowLeft } from "lucide-react";

const BackButton = () => {
  const navigate = useNavigate();

  return (
    <button
      onClick={() => navigate("/dashboard")}
      className="cursor-pointer flex items-center text-blue-600 hover:text-blue-800 font-medium mb-4"
    >
      <ArrowLeft className="mr-2" size={18} />
      Back to Dashboard
    </button>
  );
};

export default BackButton;
