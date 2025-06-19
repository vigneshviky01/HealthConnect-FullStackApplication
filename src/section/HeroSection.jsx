// src/components/HeroSection.jsx
import React from 'react';

const HeroSection = () => {
  return (
    <section className="bg-gradient-to-b from-blue-600 to-blue-500 text-white py-16 px-20">
      <div className="max-w-6xl mx-auto flex flex-col lg:flex-row items-center justify-between">
        
        <div className="text-center lg:text-left lg:max-w-xl">
          <h1 className="text-5xl md:text-6xl lg:text-7xl font-extrabold mb-4 leading-tight">
            Wellness in One Place
          </h1>
          <p className="text-lg md:text-xl mb-6 text-blue-100">
            Track your daily activity, sleep, water intake, and mood —
            all in one dashboard to help you thrive.
          </p>
          <a
            href="/register"
            className="inline-block bg-white text-blue-700 font-semibold px-6 py-3 rounded-full shadow hover:bg-blue-100 transition"
          >
            Start Tracking Now →
          </a>
        </div>

        <div className="mt-10 lg:mt-0">
          <img
            src="/images/heroImg.png"
            alt="Health Dashboard"
            className="w-80 md:w-96 "
          />
        </div>
      </div>
    </section>
  );
};

export default HeroSection;
