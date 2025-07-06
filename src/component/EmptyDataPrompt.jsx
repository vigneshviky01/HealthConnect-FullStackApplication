import React from "react";
import { Sparkles } from "lucide-react";

const EmptyDataPrompt = ({ message }) => {
  return (
    <div className="flex flex-col items-center justify-center text-blue-600 py-10">
      <Sparkles size={48} className="mb-4 animate-pulse" />
      <p className="text-lg font-semibold">{message}</p>
    </div>
  );
};

export default EmptyDataPrompt;