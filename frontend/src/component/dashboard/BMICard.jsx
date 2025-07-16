import React from "react";
import { FaHeartbeat } from "react-icons/fa";

const BMICard = ({ height, weight}) => {
  const heightInMeters = height / 100;
  const bmi = weight / (heightInMeters * heightInMeters);
  const bmiRounded = bmi.toFixed(1);

  let message = "";
  let badgeColor = "";

  if (bmi < 18.5) {
    message = "You need to gain some weight.";
    badgeColor = "bg-yellow-100 text-yellow-700";
  } else if (bmi >= 18.5 && bmi <= 24.9) {
    message = "You're looking great!";
    badgeColor = "bg-green-100 text-green-700";
  } else if (bmi >= 25 && bmi <= 29.9) {
    message = "You need to lose some weight.";
    badgeColor = "bg-orange-100 text-orange-700";
  } else {
    message = "Consider consulting a doctor.";
    badgeColor = "bg-red-100 text-red-700";
  }

  return (
    <div className="flex justify-center items-center w-full py-10 mt-5">
      <div className="w-full max-w-xl bg-blue-50 p-4 rounded-xl shadow-md hover:shadow-lg transition duration-200">
        <div className="flex items-center gap-3 mb-2 text-blue-700">
          <FaHeartbeat size={24} />
          <h3 className="font-semibold text-lg">BMI Summary</h3>
        </div>
        <div className="text-sm text-gray-700 mb-2">
          Height: <strong>{height} cm</strong>, Weight: <strong>{weight} kg</strong>
        </div>
        <div className="text-lg font-bold text-blue-700 mb-1">
          BMI: {bmiRounded}
        </div>
        <div className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${badgeColor}`}>
          {message}
        </div>
      </div>
    </div>
  );
};

export default BMICard;
