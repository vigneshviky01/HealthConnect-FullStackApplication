import React from "react";
import WaterForm from "../../component/dashboard/WaterForm";
import BackButton from "../../component/BackButton";
import WaterSectionTemplate from "../../layout/WaterSectionTemplate";

const WaterTracker = () => {
  return (
    <>
    <BackButton />
    <WaterSectionTemplate
      title="Daily Water Intake Tracker"
      formComponent={WaterForm}
    />
    </>
  );
};

export default WaterTracker;
