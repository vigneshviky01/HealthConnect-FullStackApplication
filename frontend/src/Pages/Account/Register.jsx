import React, { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import * as registerFunc from "./RegisterFunction";
export default function Register() {
  const [formData, setFormData] = useState({
    username: "",
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    age: "",
    gender: "",
    height: "",
    weight: "",
  });
  const [loading, setLoading] = useState(false);

  const [errors, setErrors] = useState({});
  const navigate = useNavigate();
  const validateForm = () => {
    let newErrors = {};

    // Required fields
    for (const field in formData) {
      if (!formData[field]) {
        newErrors[field] = "This field is required";
      }
    }
    if (!formData.username) {
      newErrors.username = "Username is required";
    }

    // Password match check
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      newErrors.email = "Please enter a valid email address";
    }
    const age = parseInt(formData.age);
    if (age < 13 || age > 120) {
      newErrors.age = "Age must be between 13 and 120";
    }
    const height = parseInt(formData.height);
    if (height < 50 || height > 300) {
      newErrors.height = "Height must be between 50 and 300 cm";
    }
    const weight = parseInt(formData.weight);
    if (weight < 20 || weight > 500) {
      newErrors.weight = "Weight must be between 20 and 500 kg";
    }
    setErrors(newErrors);

    // Return true if no errors
    return Object.keys(newErrors).length === 0;
  };

  const submitHandler = async (e) => {
    e.preventDefault();
    if (loading) return;
    if (validateForm()) {
      setLoading(true);
      try {
        const { confirmPassword, ...cleanedFormData } = formData;

        console.log("Sending cleanedFormData:", cleanedFormData);

        const response = await registerFunc.registerUser(cleanedFormData);

        toast.success("Registered successfully!", { autoClose: 3000 });

        setTimeout(() => {
          navigate("/login");
        }, 2000);
      } catch (error) {
        if (
          error.response?.data?.message?.includes("Username is already taken")
        ) {
          setErrors((prev) => ({
            ...prev,
            username: "Username is already taken",
          }));
        } else if (
          error.response?.data?.message?.includes("Email is already in use")
        ) {
          setErrors((prev) => ({ ...prev, email: "Email is already in use" }));
        } else {
          toast.error("Registration failed", { autoClose: 2000 });
          console.error("Registration failed:", error);
        }
        setLoading(false);
      }
    } else {
      console.warn("Validation failed");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-[linear-gradient(200deg,_#0066EE_60%,_#9383FB_100%)]">
      <div className="bg-white rounded-2xl shadow-xl p-6 sm:p-8 w-full max-w-md mt-6 sm:mt-12">
        <h2 className="text-xl sm:text-2xl font-bold text-center mb-6 text-[#0066EE]">
          Create Account
        </h2>
        <form className="space-y-4" onSubmit={submitHandler}>
          <div>
            <label className="block mb-1 text-sm text-[#0066EE] font-medium">
              Username
            </label>
            <input
              type="text"
              name="username"
              placeholder="Choose a username"
              value={formData.username}
              onChange={handleChange}
              className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                errors.username ? "border-red-500" : "border-[#0066EE]"
              } text-sm sm:text-base text-[#0066EE] placeholder-[#0066EE]`}
            />
            {errors.username && (
              <p className="text-sm text-red-500">{errors.username}</p>
            )}
          </div>

          {/* Name */}
          <div>
            <label className="block mb-1 text-sm text-[#0066EE] font-medium">
              Full Name
            </label>
            <input
              type="text"
              name="name"
              placeholder="Enter your full name"
              value={formData.name}
              onChange={handleChange}
              className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                errors.name ? "border-red-500" : "border-[#0066EE]"
              } text-sm sm:text-base text-[#0066EE] placeholder-[#0066EE]  `}
            />
            {errors.name && (
              <p className="text-sm text-red-500">{errors.name}</p>
            )}
          </div>

          {/* Email */}
          <div>
            <label className="block mb-1 text-sm text-[#0066EE] font-medium">
              Email
            </label>
            <input
              type="email"
              name="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={handleChange}
              className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                errors.email ? "border-red-500" : "border-[#0066EE]"
              } text-sm sm:text-base text-[#0066EE] placeholder-[#0066EE]`}
            />
            {errors.email && (
              <p className="text-sm text-red-500">{errors.email}</p>
            )}
          </div>

          {/* Passwords */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block mb-1 text-sm text-[#0066EE] font-medium">
                Password
              </label>
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={formData.password}
                onChange={handleChange}
                className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                  errors.password ? "border-red-500" : "border-[#0066EE]"
                }text-sm sm:text-base text-[#0066EE] placeholder-[#0066EE]`}
              />
              {errors.password && (
                <p className="text-sm text-red-500">{errors.password}</p>
              )}
            </div>
            <div>
              <label className="block mb-1 text-sm text-[#0066EE] font-medium">
                Confirm Password
              </label>
              <input
                type="password"
                name="confirmPassword"
                placeholder="Confirm Password"
                value={formData.confirmPassword}
                onChange={handleChange}
                className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                  errors.confirmPassword ? "border-red-500" : "border-[#0066EE]"
                } text-sm sm:text-base text-[#0066EE] placeholder-[#0066EE]`}
              />
              {errors.confirmPassword && (
                <p className="text-sm text-red-500">{errors.confirmPassword}</p>
              )}
            </div>
          </div>

          {/* Age and Gender */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block mb-1 text-sm text-[#0066EE] font-medium">
                Age
              </label>
              <input
                type="number"
                name="age"
                placeholder="e.g. 25"
                value={formData.age}
                onChange={handleChange}
                className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                  errors.age ? "border-red-500" : "border-[#0066EE]"
                } text-sm sm:text-base text-[#0066EE] placeholder-[#0066EE]`}
              />
              {errors.age && (
                <p className="text-sm text-red-500">{errors.age}</p>
              )}
            </div>
            <div>
              <label className="block mb-1 text-sm text-[#0066EE] font-medium">
                Gender
              </label>
              <select
                name="gender"
                value={formData.gender}
                onChange={handleChange}
                className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                  errors.gender ? "border-red-500" : "border-[#0066EE]"
                } text-sm sm:text-base text-[#0066EE] bg-white`}
              >
                <option value="">Select</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
              </select>
              {errors.gender && (
                <p className="text-sm text-red-500">{errors.gender}</p>
              )}
            </div>
          </div>

          {/* Height and Weight */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block mb-1 text-sm text-[#0066EE] font-medium">
                Height (cm)
              </label>
              <input
                type="number"
                name="height"
                placeholder="e.g. 170"
                value={formData.height}
                onChange={handleChange}
                className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                  errors.height ? "border-red-500" : "border-[#0066EE]"
                } text-sm sm:text-base text-[#0066EE] placeholder-[#0066EE]`}
              />
              {errors.height && (
                <p className="text-sm text-red-500">{errors.height}</p>
              )}
            </div>
            <div>
              <label className="block mb-1 text-sm text-[#0066EE] font-medium">
                Weight (kg)
              </label>
              <input
                type="number"
                name="weight"
                placeholder="e.g. 65"
                value={formData.weight}
                onChange={handleChange}
                className={`w-full px-4 py-2 sm:px-4 sm:py-3 rounded-lg border ${
                  errors.weight ? "border-red-500" : "border-[#0066EE]"
                } text-sm sm:text-base text-[#0066EE] placeholder-[#0066EE]`}
              />
              {errors.weight && (
                <p className="text-sm text-red-500">{errors.weight}</p>
              )}
            </div>
          </div>

          {/* Submit */}
          <button
            type="submit"
            disabled={loading}
            className={`w-full py-2 sm:px-4 sm:py-3 rounded-lg font-semibold transition mt-2 ${
              loading
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-[#0066EE] hover:bg-[#0055cc] text-white"
            }`}
          >
            {loading ? "Registering..." : "Register"}
          </button>
        </form>

        <p className="text-center text-sm text-[#0066EE] mt-6">
          Already have an account?{" "}
          <NavLink
            to="/login"
            className="font-semibold underline hover:text-[#0055cc]"
          >
            Login here
          </NavLink>
        </p>
      </div>
    </div>
  );
}
