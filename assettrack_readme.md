# AssetTrack – Full-Stack Asset Management System

A full-stack asset management application built to help organizations manage company assets, employees, asset assignments, returns, warranties, and maintenance activities from one centralized platform.

AssetTrack provides separate Admin and Employee portals with role-based access control, secure authentication, and a MySQL database.

---

## 📑 Table of Contents
1. [Project Overview](#-project-overview)
2. [Key Features](#-key-features)
3. [User Roles](#-user-roles)
4. [Technology Stack](#-technology-stack)
5. [System Architecture](#-system-architecture)
6. [Project Structure](#-project-structure)
7. [Database Design](#-database-design)
8. [Application Workflow](#-application-workflow)
9. [REST API Overview](#-rest-api-overview)
10. [Run Locally](#-run-locally)
11. [Environment Configuration](#-environment-configuration)
12. [Deploy on Render](#-deploy-on-render)
13. [Security and Business Rules](#-security-and-business-rules)
14. [Future Enhancements](#-future-enhancements)
15. [Author](#-author)
16. [License](#-license)

---

## 📌 Project Overview
Organizations manage multiple assets such as laptops, desktops, monitors, keyboards, and other equipment. Managing asset allocation, employee assignments, warranty periods, and maintenance manually can lead to inaccurate records and poor tracking.

AssetTrack addresses these challenges through a centralized web application:
* **Administrators** can manage employees and assets, assign equipment, track returns, monitor warranty expiry, and manage maintenance requests.
* **Employees** can log in to view their assigned assets, check assignment history, and report maintenance issues.

---

## 🚀 Key Features

### 1. Dashboard
* Overview of total assets.
* Available and assigned asset counts.
* Asset maintenance status.
* Employee and inventory summaries.
* Centralized asset management dashboard.

### 2. Asset Management
* Add, update, view, and manage assets.
* Organize assets using categories.
* Track asset codes, names, serial numbers, and status.
* Monitor asset availability.
* Maintain asset information in MySQL.

### 3. Employee Management
* Add and manage employees.
* Maintain employee details and contact information.
* Create employee login accounts.
* Activate or deactivate employee records.
* Link employee accounts with their assigned assets.

### 4. Asset Assignment and Return
* Assign available assets to employees.
* Record assignment dates and return dates.
* Maintain assignment history.
* Track currently assigned assets.
* Prevent assigning an already assigned asset.

### 5. Warranty Tracking
* Maintain warranty information for assets.
* Track warranty start and expiry dates.
* Identify warranties approaching expiry.
* Display upcoming warranty expiry alerts.

### 6. Maintenance Management
* Create and manage maintenance records.
* Track maintenance status:
  * `Pending`
  * `In Progress`
  * `Completed`
* Monitor assets undergoing maintenance.
* Allow employees to report maintenance issues for their assigned assets.

### 7. Employee Portal
* Employee-specific login.
* View personal profile.
* View currently assigned assets.
* Check previous assignment history.
* Submit maintenance requests.
* Access only their own employee-related information.

### 8. Authentication and Authorization
* JWT-based authentication.
* Spring Security integration.
* BCrypt password hashing.
* Role-based access control.
* Separate `ADMIN` and `EMPLOYEE` permissions.

---

## 👥 User Roles

| Feature | Admin | Employee |
| :--- | :---: | :---: |
| Login | ✅ | ✅ |
| Dashboard | ✅ | — |
| Manage assets | ✅ | — |
| Manage employees | ✅ | — |
| Assign assets | ✅ | — |
| Return assets | ✅ | — |
| View warranty alerts | ✅ | — |
| Manage maintenance | ✅ | — |
| View own profile | — | ✅ |
| View assigned assets | — | ✅ |
| View assignment history | — | ✅ |
| Report maintenance issues | — | ✅ |

---

## 🛠️ Technology Stack

### Backend
* Java 21
* Spring Boot 3.5.x
* Spring MVC
* Spring Data JPA
* Hibernate ORM
* Spring Security
* JWT Authentication
* Maven

### Frontend
* React
* JavaScript
* Vite
* HTML5
* CSS3

### Database
* MySQL 8
* Aiven MySQL (cloud database)
* Database design and relational mapping

### Development and Deployment
* Git and GitHub
* Postman
* IntelliJ IDEA / VS Code
* Docker
* Render
* GitHub Actions

---

## 🏗️ System Architecture
AssetTrack follows a layered backend architecture with a React frontend.

```text
              USER
               |
               v
       REACT FRONTEND
               |
               v
        REST API REQUEST
               |
               v
       SPRING BOOT BACKEND
               |
       +-------+--------+
       |       |        |
       v       v        v
  Controller Service Repository
                       |
                       v
                  Hibernate
                       |
                       v
                   MYSQL DB
```

### Backend Layers
| Layer | Responsibility |
| :--- | :--- |
| **Controller** | Handles HTTP requests and responses |
| **Service** | Implements business logic |
| **Repository** | Performs database operations |
| **Entity** | Represents database tables |
| **DTO** | Transfers application data |
| **Security** | Handles authentication and authorization |
| **Exception** | Handles application errors |
| **Config** | Contains application configuration |

---

## 📂 Project Structure

```text
AssetTrack/
│
├── backend/
│   ├── src/main/java/com/assettrack/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── AssetTrackApplication.java
│   │
│   ├── src/main/resources/
│   │   └── application.properties
│   │
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── App.jsx
│   │   ├── api.js
│   │   ├── main.jsx
│   │   └── styles.css
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── database/
│   ├── schema.sql
│   └── README.md
│
├── .github/
│   └── workflows/
│       └── assettrack-build.yml
│
├── Dockerfile
├── render.yaml
├── .gitignore
└── README.md
```

---

## 🗄️ Database Design
AssetTrack uses a relational MySQL database. The application contains the following core data entities:

| Table | Purpose |
| :--- | :--- |
| `roles` | Stores application roles |
| `users` | Stores authentication details |
| `employees` | Stores employee information |
| `categories` | Stores asset categories |
| `assets` | Stores asset inventory |
| `assignments` | Stores asset assignment and return history |
| `warranties` | Stores warranty information |
| `maintenance_records` | Stores maintenance activities |

### Database Relationships
* An employee can have multiple asset assignments over time.
* An asset can have multiple historical assignment records.
* Each assignment links an employee with an asset.
* An asset can have warranty information.
* An asset can have multiple maintenance records.
* Users are associated with roles and employee accounts.

> **Database Initialization:** Hibernate manages the application schema through the configured JPA `ddl-auto` setting. The application must be able to connect to MySQL before its tables can be initialized.

---

## 🔄 Application Workflow

### Admin Workflow
1. Admin logs in using credentials.
2. Admin accesses the dashboard.
3. Admin adds employees and assets.
4. Admin assigns available assets to employees.
5. The system records assignment information.
6. Admin tracks warranty expiry and maintenance activities.
7. Admin processes asset returns and reviews assignment history.

### Employee Workflow
1. Employee logs in using their account.
2. Employee accesses the employee portal.
3. Employee views their profile and assigned assets.
4. Employee checks assignment history.
5. Employee reports maintenance issues when required.

---

## 🔌 REST API Overview
The backend exposes REST endpoints for frontend communication.

| Module | Endpoint | Purpose |
| :--- | :--- | :--- |
| **Authentication** | `/api/auth/login` | User login |
| **Authentication** | `/api/auth/me` | Get authenticated user |
| **Employee Portal** | `/api/employee/profile` | Get employee profile |
| **Employee Portal** | `/api/employee/assignments` | Get own assignments |
| **Employee Portal** | `/api/employee/maintenance` | Manage own maintenance requests |
| **Health** | `/health` | Deployment health check |

*Other administrative endpoints are organized under the `/api/` route.*

---

## 💻 Run Locally

### Prerequisites
Install the following:
* Java 21
* Maven
* Node.js and npm
* MySQL 8
* Git

### 1. Clone the Repository
```bash
git clone https://github.com/Pranavkharage/AssetTrack.git
cd AssetTrack
```

### 2. Create the MySQL Database
Open MySQL Workbench or MySQL Command Line and execute:
```sql
CREATE DATABASE assettrack;
```

### 3. Configure Backend
Open `backend/src/main/resources/application.properties` and configure the local database connection or provide the environment variables described below.

### 4. Start Backend
```bash
cd backend
mvn spring-boot:run
```
*The backend uses the configured server port.*

### 5. Start Frontend
Open another terminal:
```bash
cd frontend
npm install
npm run dev
```
Open the local Vite URL displayed in your terminal, usually:
```text
http://localhost:5173
```

---

## ⚙️ Environment Configuration
For deployment, configure the following environment variables in Render:

| Variable | Description |
| :--- | :--- |
| `DB_URL` | MySQL JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key used for JWT signing |
| `JWT_EXPIRATION_MS` | JWT expiration duration in milliseconds |
| `CORS_ALLOWED_ORIGINS` | Allowed frontend origins |
| `BOOTSTRAP_ADMIN_EMAIL` | Initial admin email, if bootstrap is configured |
| `BOOTSTRAP_ADMIN_PASSWORD` | Initial admin password, if bootstrap is configured |

**Example JDBC URL:**
```text
jdbc:mysql://YOUR_HOST:YOUR_PORT/assettrack?sslMode=REQUIRED&serverTimezone=UTC
```

> **Security Note:** Never commit actual database passwords, JWT secrets, or production credentials to GitHub.

---

## ☁️ Deploy on Render
AssetTrack is configured for Docker-based deployment on Render.

### Deployment Steps
1. Push the project to GitHub.
2. Create a new **Web Service** on Render.
3. Connect the `AssetTrack` GitHub repository.
4. Select the deployment branch.
5. Choose **Docker** as the runtime.
6. Configure the required environment variables.
7. Deploy the application.
8. Verify the health endpoint:
   ```text
   https://YOUR-RENDER-URL/health
   ```

The Docker configuration builds the React frontend and Spring Boot backend and packages them into a single deployable service. The production frontend is served by the Spring Boot application.

---

## 🔐 Security and Business Rules
AssetTrack implements important application-level controls:
* Passwords are hashed using **BCrypt**.
* **JWT authentication** protects secured endpoints.
* **Role-based access** separates administrator and employee permissions.
* Employees can access only their own portal data.
* Only `available` assets can be assigned.
* Already assigned assets cannot be assigned again.
* Retired assets cannot be assigned.
* Assignment and return operations use **transactional processing**.
* Asset assignment uses **database locking** to reduce conflicting assignments.
* Assignment history is retained for tracking.
* Maintenance status follows a defined workflow.

---

## 🔮 Future Enhancements
Potential improvements include:
* Email notifications for warranty expiry.
* Asset depreciation and financial tracking.
* Exportable inventory reports in Excel and PDF.
* QR code-based asset identification.
* Advanced analytics and reporting.
* Audit logs for administrative activities.
* Automated backup and recovery.

---

## 👨‍💻 Author
**Pranav Sanjay Kharage**  
B.E. Computer Engineering | NMIET Pune  
* **GitHub:** [Pranavkharage](https://github.com/Pranavkharage)
* **LinkedIn:** Pranav Kharage

---

## 📄 License
This project is developed for learning, demonstration, and portfolio purposes. Add a formal open-source license (such as MIT or Apache 2.0) if you intend to permit reuse or redistribution.