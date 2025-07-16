import React, { useState, useEffect } from "react";
import { User } from "lucide-react";
import { Link, useLocation } from "react-router-dom";
import { useUser } from "../context/UserContext";

const Navbar = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const location = useLocation();
  const { userInfo } = useUser();

  const isDashboard = location.pathname.startsWith("/dashboard");
  // const isLoggedIn = !!userInfo;;
const isLoggedIn = true; //change after


  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 10);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  // Determine profile link destination
  const profileLink = isLoggedIn && isDashboard ? "/profile" : "/login";

  // Profile button component (reused)
  const ProfileButton = () => (
    <Link to={profileLink}>
      <button
        className={`cursor-pointer p-2 rounded-full transition-colors duration-200 ${
          isDashboard
            ? isScrolled
              ? "text-blue-600 hover:text-white hover:bg-blue-600"
              : "text-white hover:text-blue-600 hover:bg-white"
            : isScrolled
            ? "text-white hover:text-blue-600 hover:bg-white"
            : "text-white hover:text-blue-600 hover:bg-white"
        }`}
        title={isLoggedIn ? "Profile" : "Login"}
      >
        <User size={24} className="transition-colors duration-200" />
      </button>
    </Link>
  );

  // CTA button component
  const CTAButton = () => (
    <button className="px-6 py-2 rounded-full font-medium transition-all duration-300 hover:scale-105 bg-blue-600 text-white hover:bg-blue-700">
      Start Tracking Now
    </button>
  );

  // Determine which button to show
  const renderRightButton = () => {
    if (!isScrolled || isDashboard) {
      return <ProfileButton />;
    }
    return <CTAButton />;
  };

  return (
    <nav
      className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 ease-in-out ${
        isScrolled ? "bg-white shadow-md" : "bg-blue-600"
      }`}
    >
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div className="flex-shrink-0">
            <h1
              className={`text-xl font-bold transition-colors duration-300 ${
                isScrolled ? "text-blue-600" : "text-white"
              }`}
            >
              healthconnect
            </h1>
          </div>

          {/* Right Button */}
          <div className="flex items-center">{renderRightButton()}</div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
