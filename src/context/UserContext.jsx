// src/context/UserContext.js
import { createContext, useContext, useState } from "react";

// 1. Create the context
const UserContext = createContext();

// 2. Create a provider component
export const UserProvider = ({ children }) => {
  const [userInfo, setUserInfo] = useState(null);

  return (
    <UserContext.Provider value={{ userInfo, setUserInfo }}>
      {children}
    </UserContext.Provider>
  );
};

// 3. Create a custom hook for easy access
export const useUser = () => useContext(UserContext);
