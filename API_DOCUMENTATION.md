# Health Connect API Documentation

## Data Visualization & Trends API Endpoints

The following API endpoints are available for retrieving aggregated data for visualization and trends analysis.

### Dashboard Statistics

```
GET /api/stats/dashboard
```

Returns a summary of key health metrics for the dashboard, including average steps per day, average sleep duration, average sleep quality, and average calories burned over the last 30 days.

**Response Example:**

```json
{
  "period": "last30Days",
  "startDate": "2023-05-01",
  "endDate": "2023-05-31",
  "stats": {
    "averageStepsPerDay": 8500,
    "averageSleepHours": 7.5,
    "averageSleepQuality": 4.2,
    "averageCaloriesBurned": 350
  }
}
```

### Steps Data

```
GET /api/stats/steps?aggregation={aggregation}&startDate={startDate}&endDate={endDate}
```

Returns steps data aggregated by day, week, or month for the specified date range.

**Parameters:**

- `aggregation` (optional): The aggregation level. Valid values are `daily` (default), `weekly`, or `monthly`.
- `startDate` (optional): The start date in ISO format (YYYY-MM-DD). Defaults to 30 days ago.
- `endDate` (optional): The end date in ISO format (YYYY-MM-DD). Defaults to today.

**Response Example (Daily):**

```json
{
  "type": "daily",
  "unit": "steps",
  "startDate": "2023-05-01",
  "endDate": "2023-05-31",
  "data": [
    {
      "date": "2023-05-01",
      "value": 8000
    },
    {
      "date": "2023-05-02",
      "value": 10000
    }
  ]
}
```

**Response Example (Weekly):**

```json
{
  "type": "weekly",
  "unit": "steps",
  "startDate": "2023-05-01",
  "endDate": "2023-05-31",
  "data": [
    {
      "yearWeek": "202318",
      "value": 56000
    },
    {
      "yearWeek": "202319",
      "value": 63000
    }
  ]
}
```

**Response Example (Monthly):**

```json
{
  "type": "monthly",
  "unit": "steps",
  "startDate": "2023-01-01",
  "endDate": "2023-05-31",
  "data": [
    {
      "year": "2023",
      "month": "1",
      "value": 250000
    },
    {
      "year": "2023",
      "month": "2",
      "value": 280000
    }
  ]
}
```

### Workout Data

```
GET /api/stats/workouts?startDate={startDate}&endDate={endDate}
```

Returns workout duration data aggregated by type for the specified date range.

**Parameters:**

- `startDate` (optional): The start date in ISO format (YYYY-MM-DD). Defaults to 30 days ago.
- `endDate` (optional): The end date in ISO format (YYYY-MM-DD). Defaults to today.

**Response Example:**

```json
{
  "type": "category",
  "unit": "minutes",
  "startDate": "2023-05-01",
  "endDate": "2023-05-31",
  "data": [
    {
      "category": "Running",
      "value": 180
    },
    {
      "category": "Cycling",
      "value": 120
    },
    {
      "category": "Swimming",
      "value": 60
    }
  ]
}
```

### Calories Data

```
GET /api/stats/calories?startDate={startDate}&endDate={endDate}
```

Returns average calories burned for the specified date range.

**Parameters:**

- `startDate` (optional): The start date in ISO format (YYYY-MM-DD). Defaults to 30 days ago.
- `endDate` (optional): The end date in ISO format (YYYY-MM-DD). Defaults to today.

**Response Example:**

```json
{
  "type": "average",
  "unit": "calories",
  "startDate": "2023-05-01",
  "endDate": "2023-05-31",
  "value": 350
}
```

### Sleep Duration Data

```
GET /api/stats/sleep/duration?aggregation={aggregation}&startDate={startDate}&endDate={endDate}
```

Returns sleep duration data aggregated by day or month for the specified date range.

**Parameters:**

- `aggregation` (optional): The aggregation level. Valid values are `daily` (default) or `monthly`.
- `startDate` (optional): The start date in ISO format (YYYY-MM-DD). Defaults to 30 days ago.
- `endDate` (optional): The end date in ISO format (YYYY-MM-DD). Defaults to today.

**Response Example (Daily):**

```json
{
  "type": "daily",
  "unit": "hours",
  "startDate": "2023-05-01",
  "endDate": "2023-05-31",
  "data": [
    {
      "date": "2023-05-01",
      "value": 7.5
    },
    {
      "date": "2023-05-02",
      "value": 8.0
    }
  ]
}
```

**Response Example (Monthly):**

```json
{
  "type": "monthly",
  "unit": "hours",
  "startDate": "2023-01-01",
  "endDate": "2023-05-31",
  "data": [
    {
      "year": "2023",
      "month": "1",
      "value": 7.2
    },
    {
      "year": "2023",
      "month": "2",
      "value": 7.5
    }
  ]
}
```

### Sleep Quality Data

```
GET /api/stats/sleep/quality?startDate={startDate}&endDate={endDate}
```

Returns average sleep quality for the specified date range.

**Parameters:**

- `startDate` (optional): The start date in ISO format (YYYY-MM-DD). Defaults to 30 days ago.
- `endDate` (optional): The end date in ISO format (YYYY-MM-DD). Defaults to today.

**Response Example:**

```json
{
  "type": "average",
  "unit": "rating",
  "startDate": "2023-05-01",
  "endDate": "2023-05-31",
  "value": 4.2
}
```