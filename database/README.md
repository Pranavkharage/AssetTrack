# AssetTrack database

Database engine: MySQL 8.0+

## Create schema

Run `schema.sql` in MySQL Workbench or with:

```bash
mysql -u YOUR_USER -p < database/schema.sql
```

The script creates `assettrack_db`, eight tables, foreign keys, unique constraints, indexes, role seed rows, and starter asset categories.

## Tables and relationships

| Table | Purpose | Important columns |
|---|---|---|
| roles | Authorization roles | id, name (ADMIN / EMPLOYEE) |
| employees | Employee directory | employee_code, full_name, email, department, designation, phone, joining_date, active |
| users | Login accounts | full_name, email, BCrypt password hash, role_id, employee_id, active |
| categories | Asset categories | name, description |
| assets | Inventory | asset_code, name, brand, model, serial_number, category_id, purchase_date, purchase_price, vendor, status, created_at |
| assignments | Assignment and return history | asset_id, employee_id, assigned_date, return_date, return_condition, remarks |
| warranties | Warranty details | asset_id, warranty_start, warranty_end, provider |
| maintenance_records | Repair workflow | asset_id, reported_by, issue_description, reported_date, status, repair_cost, resolution, completed_date |

Relationships: roles→users (one-to-many); employees→users (optional one-to-one); categories→assets (one-to-many); assets→assignments (one-to-many); employees→assignments (one-to-many); assets→warranties (optional one-to-one); assets→maintenance_records (one-to-many); employees→maintenance_records (one-to-many).

## Connect Spring Boot

Set these environment variables in Render (or your local shell):

- DB_URL=jdbc:mysql://HOST:3306/assettrack_db?useSSL=true&serverTimezone=UTC
- DB_USERNAME=database username
- DB_PASSWORD=database password
- JWT_SECRET=a randomly generated secret of at least 32 bytes

Spring Boot uses the JPA entity mappings and `spring.jpa.hibernate.ddl-auto=update` to validate/update entity tables at startup. The SQL script is provided for explicit, reproducible database creation. Do not use `create` or `create-drop` against a database with real data.

## Important limitation

Schema creation does not itself provision an ADMIN login, create employee login accounts, or prove that role-based row-level access is enforced. Those are application workflows and must be completed and tested separately. Back up the database before schema changes.
