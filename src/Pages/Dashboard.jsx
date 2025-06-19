import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "../section/Navbar";
import { useUser } from "../context/UserContext";
import Welcome from "../section/dashboard/Welcome";

const Dashboard = () => {
  const { userInfo, setUserInfo } = useUser();

  const token = sessionStorage.getItem("authToken");
  //      useEffect(() => {
  //     axios
  //       .get("http://localhost:5055/user/profile", {
  //         headers: { Authorization: `Bearer ${token}` },
  //       })
  //       .then((res) => setUserInfo(res.data))
  //       .catch((err) => console.error("Failed to fetch user data", err));
  //   }, []);

  //   if (!userInfo) {
  //     return <div className="text-center mt-20">Loading user info...</div>;
  //   }

  return (
    <>
      <Navbar />
      <div className="container mx-auto  space-y-8">

     <Welcome />
      </div>
    </>
  );
};

export default Dashboard;
