import { useEffect, useState } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import axios from 'axios';

export const PrivateRoute = ({ children }) => {
  const token = sessionStorage.getItem('authToken'); 
  const [isValid, setIsValid] = useState(null); 
  const location = useLocation();

  useEffect(() => {
    const verifyToken = async () => {
      if (!token) {
        setIsValid(false);
        return;
      }

      try {
        await axios.get("http://localhost:8080/api/users/profile", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setIsValid(true); 
      } catch (error) {
        console.error("Token validation error:", error);
        setIsValid(false); 
      }
    };

    verifyToken();
  }, [token]);

  if (isValid === null) return <p>Loading...</p>;
  if (!isValid) return <Navigate to="/login" state={{ from: location }} replace />;

  return children;
};
