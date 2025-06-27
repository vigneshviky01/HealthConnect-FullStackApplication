import { Outlet } from "react-router-dom";
import Navbar from "../section/Navbar";

const DashboardLayout = () => {
  return (
    <>
      <Navbar />
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-20">
        <Outlet /> 
      </div>
    </>
  );
};

export default DashboardLayout;
