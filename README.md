# Full-Stack E-Commerce Application

A modern, full-stack E-Commerce admin application featuring a Java Spring Boot backend and a dynamic React frontend.

## Architecture

- **Backend**: Java 17, Spring Boot, Spring Data JPA, MySQL. The backend serves REST APIs on `http://localhost:8080/api/products`.
- **Frontend**: React, Vite, Vanilla CSS with custom theming. The frontend runs on `http://localhost:5173` and proxies `/api` requests to the backend to avoid CORS issues natively.

## Prerequisites

Before starting, ensure you have the following installed:
- [Java Development Kit (JDK) 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/) (or use the included wrapper)
- [Node.js & npm](https://nodejs.org/)
- **MySQL Database**: Running locally on port `3306`. (Ensure your credentials matching `application.properties` are configured).

---

## 🚀 Setting up the Backend (Spring Boot)

1. Open your terminal and navigate to the project root directory:
   ```bash
   cd SpringDataJPADemo
   ```

2. Make sure your MySQL database is running. The application is configured to create the database automatically if it doesn't exist via the `createDatabaseIfNotExist=true` parameter in `application.properties`.

3. Run the application using the Maven wrapper:
   - On **Windows**:
     ```bash
     mvnw.cmd spring-boot:run
     ```
   - On **macOS/Linux**:
     ```bash
     ./mvnw spring-boot:run
     ```

The backend server will start on `http://localhost:8080`.

---

## 🎨 Setting up the Frontend (React + Vite)

The frontend is housed entirely within the `frontend` directory. 

1. Open a **new terminal tab/window** and navigate to the frontend folder:
   ```bash
   cd SpringDataJPADemo/frontend
   ```

2. Install all required dependencies (this only needs to be done once):
   ```bash
   npm install
   ```

3. Start the Vite development server:
   ```bash
   npm run dev
   ```

4. You can now view the application by opening the provided local link in your browser (typically `http://localhost:5173`).

---

## 🛠️ Available Features and APIs

### Backend API Endpoints (`/api/products`)
- `GET /` - Retrieve all products
- `GET /{id}` - Retrieve single product
- `POST /` - Add a new product
- `PATCH /{id}` - Update a product
- `DELETE /{id}` - Remove a product

### Frontend Features
- A globally responsive grid layout utilizing modern "glassmorphism" aesthetic.
- Form validations with dynamic modal states for adding and updating products.
- Error handling that gracefully displays UI warnings if the server disconnects.
