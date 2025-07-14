import React from 'react';
import { User, Activity, Moon, Droplets, BarChart3, ChevronRight } from 'lucide-react';

const HowItWorks = () => {
  const steps = [
    {
      id: 1,
      title: "Create Your Profile",
      description: "Sign up securely and set up your personal health profile with basic information like age, weight, and health goals.",
      icon: User,
      iconBg: "bg-blue-200"
    },
    {
      id: 2,
      title: "Track Daily Activities",
      description: "Log your daily activities including steps, workouts, calories burned, and exercise duration.",
      icon: Activity,
      iconBg: "bg-blue-200"
    },
    {
      id: 3,
      title: "Monitor Sleep Patterns",
      description: "Record your sleep start time, end time, quality rating, and add personal notes about your rest.",
      icon: Moon,
      iconBg: "bg-blue-200"
    },
    {
      id: 4,
      title: "Log Water & Mood",
      description: "Track your daily water intake and mood levels to understand the connection between hydration and well-being.",
      icon: Droplets,
      iconBg: "bg-blue-200"
    },
    {
      id: 5,
      title: "Visualize Your Progress",
      description: "View your health trends through interactive charts and graphs that show your progress over time.",
      icon: BarChart3,
      iconBg: "bg-blue-200"
    }
  ];

  return (
    <div className="max-w-2xl mx-auto p-6">
    <div className="text-center mb-8">
        <h2 className="text-3xl font-bold text-gray-900 mb-3">
          How It Works
        </h2>
        <p className="text-gray-600 text-lg">
          Get started with HealthConnect in just a few simple steps
        </p>
      </div>
    <div className="space-y-4">
      {steps.map((step) => {
        const Icon = step.icon;
        
        return (
          <div
            key={step.id}
            className="bg-white rounded-2xl border border-gray-200 p-6  "
          >
            <div className="flex items-center justify-between">
              <div className="flex items-start space-x-4">
                <div className={`w-14 h-14 rounded-2xl ${step.iconBg} flex items-center justify-center flex-shrink-0`}>
                  <Icon className="w-7 h-7 text-blue-600" />
                </div>
                <div className="flex-1">
                  <h3 className="text-xl font-semibold text-gray-900 mb-2">
                    {step.title}
                  </h3>
                  <p className="text-gray-600 leading-relaxed">
                    {step.description}
                  </p>
                </div>
              </div>
            </div>
          </div>
        );
      })}
    </div>
    </div>
  );
};

export default HowItWorks;