import React, { useState, useRef, useEffect } from "react";
import { Pencil, Check, X, User } from "lucide-react";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import axios from "axios";
import BackButtonCommon from "../component/BackButtonCommon";
const EditableField = ({
  label,
  keyName,
  editable = true,
  type = "text",
  userInfo,
  editingKey,
  editedValue,
  setEditedValue,
  errors,
  handleSave,
  handleCancel,
  loading,
  inputRef,
  setEditingKey,
}) => (
  <div className="mb-6">
    <label className="block mb-2 text-sm text-[#0066EE] font-medium">
      {label}
    </label>
    {editingKey === keyName ? (
      <div className="space-y-2">
        <div className="flex items-center gap-2">
          {keyName === "gender" ? (
            <select
              ref={inputRef}
              value={editedValue}
              onChange={(e) => setEditedValue(e.target.value)}
              className={`flex-1 px-4 py-3 rounded-lg border ${
                errors[keyName] ? "border-red-500" : "border-[#0066EE]"
              } text-[#0066EE] bg-white focus:outline-none focus:ring-2 focus:ring-[#0066EE]/20`}
            >
              <option value="">Select</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
          ) : (
            <input
              ref={inputRef}
              type={type}
              value={editedValue}
              onChange={(e) => setEditedValue(e.target.value)}
              className={`flex-1 px-4 py-3 rounded-lg border ${
                errors[keyName] ? "border-red-500" : "border-[#0066EE]"
              } text-[#0066EE] placeholder-[#0066EE]/70 focus:outline-none focus:ring-2 focus:ring-[#0066EE]/20`}
              placeholder={`Enter your ${label.toLowerCase()}`}
            />
          )}
          <div className="flex gap-2">
            <button
              onClick={handleSave}
              disabled={loading}
              className="p-2 bg-green-500 hover:bg-green-600 text-white rounded-lg transition-colors disabled:opacity-50"
            >
              <Check size={16} />
            </button>
            <button
              onClick={handleCancel}
              disabled={loading}
              className="p-2 bg-gray-500 hover:bg-gray-600 text-white rounded-lg transition-colors"
            >
              <X size={16} />
            </button>
          </div>
        </div>
        {errors[keyName] && (
          <p className="text-sm text-red-500">{errors[keyName]}</p>
        )}
      </div>
    ) : (
      <div className="flex items-center justify-between bg-gray-50 px-4 py-3 rounded-lg border border-gray-200 hover:border-[#0066EE]/30 transition-colors">
        <span className="text-gray-800 font-medium">
          {userInfo?.[keyName] || (
            <span className="text-gray-400 italic">Not provided</span>
          )}
          {keyName === "height" && userInfo?.[keyName] && " cm"}
          {keyName === "weight" && userInfo?.[keyName] && " kg"}
        </span>
        {editable && (
          <button
            onClick={() => {
              setEditingKey(keyName);
              setEditedValue(userInfo[keyName] || "");
            }}
            className="text-[#0066EE] hover:text-[#0055cc] p-1 rounded transition-colors"
          >
            <Pencil size={16} />
          </button>
        )}
      </div>
    )}
  </div>
);

export default function Profile() {
  const { userInfo, setUserInfo } = useUser();
  const navigate = useNavigate();

  const [editingKey, setEditingKey] = useState(null);
  const [editedValue, setEditedValue] = useState("");
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const token = sessionStorage.getItem("authToken");

  const inputRef = useRef(null);

  useEffect(() => {
    if (!userInfo && token) {
      fetch("http://localhost:8080/api/users/profile", {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((res) => res.json())
        .then((data) => setUserInfo(data))
        .catch((err) => console.error("Profile fetch error", err));
    }
  }, [userInfo, token]);

  useEffect(() => {
    if (editingKey && inputRef.current) {
      inputRef.current.focus();
    }
  }, [editingKey]);

  const validateField = (key, value) => {
    let error = "";

    if (!value.trim()) {
      error = "This field is required";
    } else if (key === "email") {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(value)) {
        error = "Please enter a valid email address";
      }
    } else if (key === "age") {
      const age = parseInt(value);
      if (age < 13 || age > 120) {
        error = "Age must be between 13 and 120";
      }
    } else if (key === "height") {
      const height = parseInt(value);
      if (height < 50 || height > 300) {
        error = "Height must be between 50 and 300 cm";
      }
    } else if (key === "weight") {
      const weight = parseInt(value);
      if (weight < 20 || weight > 500) {
        error = "Weight must be between 20 and 500 kg";
      }
    }

    return error;
  };

  const handleSave = async () => {
    if (!editedValue || editedValue === userInfo[editingKey]) {
      setEditingKey(null);
      setErrors({});
      return;
    }

    const error = validateField(editingKey, editedValue);
    if (error) {
      setErrors({ [editingKey]: error });
      return;
    }

    setLoading(true);

    try {
      const updatedFields = {
        fullName: userInfo.name,
        gender: userInfo.gender,
        age: userInfo.age,
        weight: userInfo.weight,
        height: userInfo.height,
      };

      // Now update the right field
      if (editingKey === "name") {
        updatedFields.fullName = editedValue;
      } else {
        updatedFields[editingKey] = editedValue;
      }

      const response = await fetch(`http://localhost:8080/api/users/profile`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(updatedFields),
      });

      if (!response.ok) throw new Error("Failed to update");

      console.log("updated!!");

      // Update local UI state
      setUserInfo((prev) => ({ ...prev, [editingKey]: editedValue }));
      setEditingKey(null);
      setEditedValue("");
      setErrors({});
    } catch (error) {
      console.error("Failed to update:", error);
      setErrors({ [editingKey]: "Failed to update. Please try again." });
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    setEditingKey(null);
    setEditedValue("");
    setErrors({});
  };

  const handleLogout = async () => {
  const token = sessionStorage.getItem("authToken");

  try {
    await axios.post(
      "http://localhost:8080/api/auth/logout",
      {},
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    toast.success("Logged out successfully");
  } catch (error) {
    console.error("Logout failed:", error);
    toast.error("Logout failed. Proceeding to clear session.");
  }

  sessionStorage.removeItem("authToken"); // Remove token
  setUserInfo(null);                      // Clear context
  navigate("/login", { replace: true }); // Redirect to login page
};

  console.log(userInfo);

  if (!userInfo) {
    return (
      <div className="min-h-screen flex items-center justify-center px-4 bg-[linear-gradient(200deg,_#0066EE_60%,_#9383FB_100%)]">
        <div className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#0066EE] mx-auto mb-4"></div>
            <p className="text-gray-600">Loading your profile...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="py-10 relative min-h-screen flex items-center justify-center px-4 bg-[linear-gradient(200deg,_#0066EE_60%,_#9383FB_100%)]">
     <BackButtonCommon />
      <div className="bg-white rounded-2xl shadow-xl p-6 sm:p-8 w-full max-w-md mt-6 sm:mt-12">
        <div className="text-center mb-8">
          <div className="w-20 h-20 bg-[#0066EE]/10 rounded-full flex items-center justify-center mx-auto mb-4">
            <User size={32} className="text-[#0066EE]" />
          </div>
          <h2 className="text-xl sm:text-2xl font-bold text-[#0066EE]">
            Your Profile
          </h2>
          <p className="text-gray-600 text-sm mt-1">
            Manage your personal information
          </p>
        </div>

        <div className="space-y-4">
          <EditableField
            label="Username"
            keyName="username"
            editable={false}
            userInfo={userInfo}
            editingKey={editingKey}
            editedValue={editedValue}
            setEditedValue={setEditedValue}
            errors={errors}
            handleSave={handleSave}
            handleCancel={handleCancel}
            loading={loading}
            inputRef={inputRef}
            setEditingKey={setEditingKey}
          />
          <EditableField
            label="Full Name"
            keyName="name"
            userInfo={userInfo}
            editingKey={editingKey}
            editedValue={editedValue}
            setEditedValue={setEditedValue}
            errors={errors}
            handleSave={handleSave}
            handleCancel={handleCancel}
            loading={loading}
            inputRef={inputRef}
            setEditingKey={setEditingKey}
          />
          <EditableField
            label="Email"
            keyName="email"
            editable={false}
            userInfo={userInfo}
            editingKey={editingKey}
            editedValue={editedValue}
            setEditedValue={setEditedValue}
            errors={errors}
            handleSave={handleSave}
            handleCancel={handleCancel}
            loading={loading}
            inputRef={inputRef}
            setEditingKey={setEditingKey}
          />
          <EditableField
            label="Age"
            keyName="age"
            type="number"
            userInfo={userInfo}
            editingKey={editingKey}
            editedValue={editedValue}
            setEditedValue={setEditedValue}
            errors={errors}
            handleSave={handleSave}
            handleCancel={handleCancel}
            loading={loading}
            inputRef={inputRef}
            setEditingKey={setEditingKey}
          />
          <EditableField
            label="Gender"
            keyName="gender"
            userInfo={userInfo}
            editingKey={editingKey}
            editedValue={editedValue}
            setEditedValue={setEditedValue}
            errors={errors}
            handleSave={handleSave}
            handleCancel={handleCancel}
            loading={loading}
            inputRef={inputRef}
            setEditingKey={setEditingKey}
          />
          <EditableField
            label="Height"
            keyName="height"
            type="number"
            userInfo={userInfo}
            editingKey={editingKey}
            editedValue={editedValue}
            setEditedValue={setEditedValue}
            errors={errors}
            handleSave={handleSave}
            handleCancel={handleCancel}
            loading={loading}
            inputRef={inputRef}
            setEditingKey={setEditingKey}
          />
          <EditableField
            label="Weight"
            keyName="weight"
            type="number"
            userInfo={userInfo}
            editingKey={editingKey}
            editedValue={editedValue}
            setEditedValue={setEditedValue}
            errors={errors}
            handleSave={handleSave}
            handleCancel={handleCancel}
            loading={loading}
            inputRef={inputRef}
            setEditingKey={setEditingKey}
          />
        </div>

        <div className="mt-8 p-4 bg-blue-50 rounded-lg">
          <p className="text-xs text-gray-600 text-center">
            Click the pencil icon to edit any field. Your changes will be saved
            automatically.
          </p>
        </div>
        <div className="mt-6 text-center">
          <button
            onClick={handleLogout}
            className="px-4 py-2 cursor-pointer align-middle bg-red-500 text-white rounded-full hover:bg-red-600 transition-all duration-300"
          >
            Logout
          </button>
        </div>
      </div>
    </div>
  );
}
