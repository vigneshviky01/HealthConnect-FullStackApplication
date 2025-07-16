Steps to set up the server:-

Extract the zip file into any location.
Then, cd location/HealthConnect-FullStackApplication
Frontend:-
cd frontend
npm i
npm run dev

The application will launch in browser at http://localhost:5173.

Backend:-
Make sure you have java 17
In MySQL, create a db: healthconnect by running:
create database healthconnect;

Also, replace the spring.datasource.password field with your actual MySQL DB Password file: backend\src\main\resources\application.properties

Then in terminal:
cd backend
.\mvnw.cmd spring-boot:run

The server will run in http://localhost:8080.

You can find the API Docs on:
http://localhost:8080/swagger-ui/index.html#/
