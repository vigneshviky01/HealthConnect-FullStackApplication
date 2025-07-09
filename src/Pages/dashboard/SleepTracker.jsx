import React, { useEffect, useState } from "react";
import axios from "axios";
import BackButton from "../../component/BackButton";
import SleepSectionTemplate from "../../layout/SleepSectionTemplate";
import SleepForm from "../../component/dashboard/SleepForm";
import SleepChart from "../../component/dashboard/SleepChart";
import SleepTable from "../../component/dashboard/SleepTable";
import PreviousSleepTable from "../../component/dashboard/PreviousSleepTable";
import ConfirmDialog from "../../component/ConfirmDialog";


const SleepTracker = () => {
  const token = sessionStorage.getItem("authToken");
  const [formData, setFormData] = useState({ from: "", to: "", quality: 0, notes: "" });
  const [editData, setEditData] = useState(null);
  const [TodaySleepLogs, setTodaySleepLogs] = useState([]);
  const [PreviousSleepLogs, setPreviousSleepLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showConfirm, setShowConfirm] = useState(false);
const [sleepIdToDelete, setSleepIdToDelete] = useState(null);


  const fetchSleepData = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/sleep", {
        headers: { Authorization: `Bearer ${token}` },
      });

      const allData = res.data;
      const todayStr = new Date().toISOString().split("T")[0];
    
      const formatDate = (isoString) => isoString.split("T")[0];

      const todayLogs = allData.filter(
        (item) => formatDate(item.createdAt) === todayStr
      );

      const previousLogs = allData.filter(
        (item) => formatDate(item.createdAt) !== todayStr
      );
      setTodaySleepLogs(todayLogs);       //1 data in array
      setPreviousSleepLogs(previousLogs);
      

    } catch (err) {
      console.error("Error fetching activity data", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSleepData();

  }, []);

  const handleSubmit = async () => {
    try {

      const { startTime, endTime,sleepStartDate,sleepEndDate,qualityRating, notes } = formData;
     

      const sleepStartTime = `${sleepStartDate}T${startTime}`;
      const sleepEndTime = `${sleepEndDate}T${endTime}`;


      const payload = {
        sleepStartTime:sleepStartTime,
        sleepEndTime:sleepEndTime,
        qualityRating: parseInt(qualityRating),
        notes: formData.notes || "No notes"
      };

    

      await axios.post("http://localhost:8080/api/sleep", payload, {
        headers: { Authorization: `Bearer ${token}` }
      });


      setFormData({ from: "", to: "", quality: 0, notes: "" });
      fetchSleepData();
    } catch (err) {
      console.error("Error submitting sleep activity", err);
    }
  };



  const handleUpdate = async () => {
    try {
      const token = sessionStorage.getItem("authToken");
      const { startTime, endTime,sleepStartDate,sleepEndDate,qualityRating, notes } = editData;
      const sleepStartTime = `${sleepStartDate}T${startTime}`;
      const sleepEndTime = `${sleepEndDate}T${endTime}`;
      const payload = {
        id: editData.id,
        sleepStartTime: sleepStartTime,
        sleepEndTime: sleepEndTime,
        qualityRating: parseInt(editData.qualityRating),
        notes: editData.notes,
        createdAt: editData.createdAt,
        updatedAt: editData.updatedAt 
      };

      console.log(payload);
      await axios.put(`http://localhost:8080/api/sleep/${editData.id}`, payload, {
        headers: { Authorization: `Bearer ${token}` }
      });



      setEditData(null);
      fetchSleepData();
    } catch (err) {
      console.error("Error updating activity", err);
    }
  };


  const confirmDelete = async () => {
  if (!sleepIdToDelete) return;
  try {
    await axios.delete(`http://localhost:8080/api/sleep/${sleepIdToDelete}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    fetchSleepData();
  } catch (err) {
    console.error("Error deleting activity", err);
  } finally {
    setShowConfirm(false);
    setSleepIdToDelete(null);
  }
};

  return (
    <>
      <BackButton />
      <SleepSectionTemplate
        title="Sleep Tracker"
        formComponent={SleepForm}

        chartComponent={<SleepChart data={PreviousSleepLogs} />}
        tableComponent={
          <SleepTable
            data={TodaySleepLogs}
            onEdit={(data) => setEditData({ ...data })}
            onDelete={(id) => {
    setSleepIdToDelete(id);
    setShowConfirm(true);
  }}
          />
        }
        previousTable={
          <PreviousSleepTable data={PreviousSleepLogs} />
        }
        records={PreviousSleepLogs}
        loading={loading}
        onSubmit={handleSubmit}
        onUpdate={handleUpdate}
        onDelete={confirmDelete}
        formData={formData}
        setFormData={setFormData}
        editData={editData}
        setEditData={setEditData}
        todaySleepData={TodaySleepLogs}
      />
      <ConfirmDialog
  isOpen={showConfirm}
  message="Are you sure you want to delete this sleep record?"
  onConfirm={confirmDelete}
  onCancel={() => {
    setShowConfirm(false);
    setSleepIdToDelete(null);
  }}
/>

    </>
  );
};

export default SleepTracker;