import React from "react";

const DashboardSectionTemplate = ({
  title,
  onSubmit,
  children,
  form,
  chart,
  records,
  loading,
}) => {
  return (
    <div className="p-6 mt-20 max-w-4xl mx-auto space-y-6">
      <h2 className="text-2xl font-bold text-blue-600">{title}</h2>
   {/* Chart */}
      {chart && (
        <div className="bg-white p-4 rounded-lg shadow">
          <h3 className="font-semibold text-lg mb-2">Overview</h3>
          {chart}
        </div>
      )}

      {/* Form */}
      {form && (
        <form
          onSubmit={onSubmit}
          className="bg-white p-4 rounded-lg shadow space-y-4"
        >
          {form}
          <button
            type="submit"
            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-full cursor-pointer"
          >
            Submit
          </button>
        </form>
      )}

   
      {/* Recent Records */}
      <div className="bg-white p-4 rounded-lg shadow">
        <h3 className="font-semibold text-lg mb-2">Recent Records</h3>
        {loading ? (
          <p>Loading...</p>
        ) : records?.length === 0 ? (
          <p className="text-gray-500">No records available.</p>
        ) : (
          <ul className="space-y-2">
            {records.map((record, index) => (
              <li key={index} className="border-b last:border-b-0 py-2">
                {record}
              </li>
            ))}
          </ul>
        )}
      </div>

      {/* Additional children (optional) */}
      {children}
    </div>
  );
};

export default DashboardSectionTemplate;
