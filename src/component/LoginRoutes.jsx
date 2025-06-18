import { useEffect, useState } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import axios from 'axios';
export const PrivateRoute = ({ children }) => {
  const token = localStorage.getItem('authToken');
  const [isValid, setIsValid] = useState(null); // null = loading
  const location = useLocation();

  useEffect(() => {
      const verifyToken = async () => {
      try {
        const res = await axios.get(`http://localhost:5000/user`, {
          params: { secret_token: token }
        });

        if (res.data === true) {
          setIsValid(true);
        } else {
          setIsValid(false);
        }
      } catch (error) {
        console.error('Token validation error:', error);
        setIsValid(false);
      }
    };

    if (token) {
      // verifyToken();
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  }, [token]);

  if (isValid === null) return <p>Loading...</p>;
  if (!isValid) return <Navigate to="/" state={{ from: location }} replace />;

  return children;
};
