import React from "react";
import MoodForm from "../../component/dashboard/MoodForm";
import BackButton from "../../component/BackButton";
import MoodSectionTemplate from "../../layout/MoodSectionTemplate";

const MoodTracker = () => {
  return (
    <>
    <BackButton />
    <MoodSectionTemplate title="Mood Logger" formComponent={MoodForm} />
    </>
  );
};

export default MoodTracker;