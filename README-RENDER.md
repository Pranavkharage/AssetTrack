# AssetTrack — Render deployment

## Project structure

- `backend/`: Java 21, Spring Boot 3.5.7, Spring Data JPA, Spring Security, JWT and MySQL.
- `frontend/`: React + Vite + JavaScript + plain CSS.
- `database/schema.sql`: reference MySQL 8 schema.
- `Dockerfile`: builds the React bundle, copies it into Spring Boot static resources, and runs one Render web service.
- `render.yaml`: Render Blueprint with a `/health` health check.

## Included application features

### Administrator workspace
- Dashboard and asset inventory
- Employee directory and linked employee account provisioning
- Asset assignment and return history
- Warranty register and 30-day expiry view
- Maintenance records and Pending → In Progress → Completed lifecycle
- ADMIN-only access to all administrative APIs

### Employee workspace
- Separate employee login layout
- Own profile, assignment history and currently assigned assets
- Employee maintenance requests restricted to assets currently assigned to that employee
- Employee API routes are protected by the EMPLOYEE role; employee endpoints query records by the authenticated user's linked employee ID

### Business safeguards
- BCrypt password hashing
- Initial administrator only created when bootstrap environment variables are configured
- Asset row pessimistic lock during assignment and return to reduce double allocation
- Retired assets cannot be assigned or sent to maintenance
- Open maintenance prevents assignment
- Maintenance completion does not make an asset available if it is still assigned or another maintenance record remains open

## Aiven MySQL setup

Create a separate database named `assettrack` inside your existing Aiven MySQL service. Do not point this application at the Centralized E-Study database.

The database must exist before first startup. The application uses Hibernate `ddl-auto=update` to create/update its tables and seeds the ADMIN/EMPLOYEE roles and default asset categories. It does not seed demo employees or assets.

Example JDBC URL (replace host and port with Aiven's values):

```
jdbc:mysql://YOUR_AIVEN_HOST:YOUR_AIVEN_PORT/assettrack?sslMode=REQUIRED&serverTimezone=UTC
```

## Render deployment

Connect the repository to Render and deploy branch `AssetTrack-Structured` as a Docker web service using the repository root as Docker context, or create it from `render.yaml`.

Set these environment variables on the Render service:

| Variable | Value |
|---|---|
| `DB_URL` | Aiven JDBC URL ending in `/assettrack?sslMode=REQUIRED&serverTimezone=UTC` |
| `DB_USERNAME` | Aiven MySQL username |
| `DB_PASSWORD` | Aiven MySQL password |
| `JWT_SECRET` | Random secret of at least 32 bytes |
| `BOOTSTRAP_ADMIN_EMAIL` | Email for first administrator |
| `BOOTSTRAP_ADMIN_PASSWORD` | Unique password, at least 12 characters |
| `JWT_EXPIRATION_MS` | Optional; defaults to 24 hours |
| `CORS_ALLOWED_ORIGINS` | Leave blank for same-origin frontend |

The first ADMIN account is created on startup only if both bootstrap variables are supplied and that email does not already exist. The password is stored using BCrypt. Remove the bootstrap password from Render after the first successful startup if you do not need to recreate the initial account; the account remains in MySQL.

Do not commit database credentials or JWT secrets to GitHub. You can use the same Aiven MySQL service as E-Study, but keep the databases separate.

## Local development

For local development you can either set environment variables or edit the local defaults in `backend/src/main/resources/application.properties` (`spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`, `app.jwt.secret`, `app.bootstrap-admin.email`, and `app.bootstrap-admin.password`). Never commit real production credentials. For local development, configure these values first:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/assettrack"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="YOUR_LOCAL_MYSQL_PASSWORD"
$env:JWT_SECRET="REPLACE_WITH_A_RANDOM_SECRET_AT_LEAST_32_BYTES_LONG"
$env:BOOTSTRAP_ADMIN_EMAIL="admin@example.com"
$env:BOOTSTRAP_ADMIN_PASSWORD="REPLACE_WITH_A_UNIQUE_PASSWORD_12_CHARS_OR_MORE"
cd backend
mvn spring-boot:run
```

In a second terminal:

```powershell
cd frontend
npm ci
npm run dev
```

Vite proxies `/api` to Spring Boot on port 10000. In production, React calls same-origin `/api` and Spring Boot serves the built frontend.

## Build verification

GitHub Actions builds the React frontend, packages the Spring Boot backend and builds the Docker image on pushes to `AssetTrack-Structured`. The workflow also publishes a downloadable source ZIP artifact when the frontend and backend builds pass.

This workflow uses `-DskipTests`; no JUnit/Mockito test suite is included. Before treating a deployment as production-ready, perform manual end-to-end checks against your Aiven database and deployed Render service, including admin login, employee login, cross-account data access, assignment/return, maintenance status transitions and warranty expiry.
