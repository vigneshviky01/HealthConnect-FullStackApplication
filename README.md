# HealthConnect - Full Stack Health Tracking Application

Welcome to HealthConnect! This repository contains both the backend (Spring Boot) and frontend (React) for a modern health tracking web application.

---

##  Project Structure

```
HealthConnect-FullStackApplication/
  ├── backend/   # Spring Boot backend (Java)
  └── frontend/  # React frontend (JavaScript)
```

---

##  Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/vigneshviky01/HealthConnect-FullStackApplication.git
cd HealthConnect-FullStackApplication
```

---

### 2. Backend Setup

1. **Navigate to the backend directory:**
    ```bash
    cd backend
    ```

2. **(Optional) Configure the database:**
    - By default, the app uses H2 (in-memory).
    - To use MySQL/PostgreSQL, edit `src/main/resources/application.properties` with your DB credentials.

3. **Start the backend server:**
    ```bash
    ./mvnw spring-boot:run
    ```
    - The backend will be available at [http://localhost:8080](http://localhost:8080)

---

### 3. Frontend Setup

1. **Open a new terminal and navigate to the frontend directory:**
    ```bash
    cd frontend
    ```

2. **Install dependencies:**
    ```bash
    npm install
    # or
    yarn install
    ```

3. **Start the frontend dev server:**
    ```bash
    npm run dev
    # or
    yarn dev
    ```
    - The frontend will be available at [http://localhost:5173](http://localhost:5173)

---

### 4. Using the Application

- Open [http://localhost:5173](http://localhost:5173) in your browser.
- Register a new account or log in to start tracking your health metrics.

---

##  Configuration

- **Backend:**  
  Edit `backend/src/main/resources/application.properties` for database, JWT, and other settings.

- **Frontend:**  
  If needed, set the API base URL in `frontend/.env`:
  ```
  VITE_API_BASE_URL=http://localhost:8080
  ```

---

##  Running Tests

- **Backend tests:**
    ```bash
    cd backend
    ./mvnw test
    ```

- **Frontend tests:**
    ```bash
    cd frontend
    npm test
    # or
    yarn test
    ```

---

## Troubleshooting

- **Port conflicts:**  
  Change backend port in `application.properties` (`server.port=8080`).  
  Change frontend port in `vite.config.js` (default: 5173).

- **CORS issues:**  
  Ensure backend CORS config allows requests from the frontend origin.

- **Database errors:**  
  Check your DB credentials and ensure your DB server is running (if not using H2).

---

##  API Documentation

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (if enabled)
---
##  Contributors
- https://github.com/vigneshviky01
- https://github.com/aniketsingh6712
- https://github.com/a97u
- https://github.com/nishanth-hebbar
