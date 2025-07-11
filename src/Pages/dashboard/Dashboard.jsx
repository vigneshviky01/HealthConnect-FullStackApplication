import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "../../section/Navbar";
import { useUser } from "../../context/UserContext";
import Welcome from "../../section/dashboard/Welcome";
import DashboardNav from "../../section/dashboard/DashboardNav";
import BMICard from "../../component/dashboard/BMICard";

const Dashboard = () => {
  const { userInfo, setUserInfo } = useUser();

  const token = sessionStorage.getItem("authToken");
  console.log(token);
       useEffect(() => {
      axios
        .get("http://localhost:8080/api/users/profile", {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((res) => setUserInfo(res.data))
        .catch((err) => console.error("Failed to fetch user data", err));
    }, []);
    console.log(userInfo);

    if (!userInfo) {
      return <div className="text-center mt-20">Loading user info...</div>;
    }

  return (
    <>
      <Welcome name={userInfo.name} />
      <DashboardNav />
      {userInfo && (
  <BMICard
    height={userInfo.height}
    weight={userInfo.weight}
  />
)}
    </>
  );
};

export default Dashboard;
