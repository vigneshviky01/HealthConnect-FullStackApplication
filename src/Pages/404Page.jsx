import React from "react";
import { useNavigate } from "react-router-dom";
import BackButtonCommon from "../component/BackButtonCommon";

const NotFoundPage = () => {
  const navigate = useNavigate();

  return (
    <div className="relative flex flex-col items-center justify-center h-screen bg-blue-100 px-4 text-center">
      <BackButtonCommon  />

      <h1 className="text-9xl font-bold text-blue-600">404</h1>
      <p className="text-2xl mt-4 text-gray-800 font-semibold">Page Not Found</p>
      <p className="text-gray-600 mt-2 max-w-md">
        Sorry, the page you’re looking for doesn’t exist.
      </p>

      <button
        onClick={() => navigate("/dashboard")}
        className="mt-6 px-6 py-2 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition duration-300"
      >
        Go to Dashboard
      </button>
    </div>
  );
};

export default NotFoundPage;
