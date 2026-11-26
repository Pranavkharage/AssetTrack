CREATE DATABASE IF NOT EXISTS assettrack_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE assettrack_db;

CREATE TABLE IF NOT EXISTS roles (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_roles_name UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS employees (
  id BIGINT NOT NULL AUTO_INCREMENT,
  employee_code VARCHAR(30) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  department VARCHAR(255),
  designation VARCHAR(255),
  phone VARCHAR(255),
  joining_date DATE,
  active BIT(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (id),
  CONSTRAINT uk_employees_employee_code UNIQUE (employee_code),
  CONSTRAINT uk_employees_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role_id BIGINT NOT NULL,
  employee_id BIGINT,
  active BIT(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (id),
  CONSTRAINT uk_users_email UNIQUE (email),
  CONSTRAINT uk_users_employee UNIQUE (employee_id),
  CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id),
  CONSTRAINT fk_users_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  description VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT uk_categories_name UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS assets (
  id BIGINT NOT NULL AUTO_INCREMENT,
  asset_code VARCHAR(30) NOT NULL,
  name VARCHAR(120) NOT NULL,
  brand VARCHAR(255),
  model VARCHAR(255),
  serial_number VARCHAR(100),
  category_id BIGINT NOT NULL,
  purchase_date DATE,
  purchase_price DECIMAL(19,2),
  vendor VARCHAR(255),
  status VARCHAR(30) NOT NULL,
  created_at DATETIME(6),
  PRIMARY KEY (id),
  CONSTRAINT uk_assets_asset_code UNIQUE (asset_code),
  CONSTRAINT uk_assets_serial_number UNIQUE (serial_number),
  CONSTRAINT fk_assets_category FOREIGN KEY (category_id) REFERENCES categories(id),
  INDEX idx_assets_status (status),
  INDEX idx_assets_category (category_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS assignments (
  id BIGINT NOT NULL AUTO_INCREMENT,
  asset_id BIGINT NOT NULL,
  employee_id BIGINT NOT NULL,
  assigned_date DATE,
  return_date DATE,
  return_condition VARCHAR(255),
  remarks TEXT,
  PRIMARY KEY (id),
  CONSTRAINT fk_assignments_asset FOREIGN KEY (asset_id) REFERENCES assets(id),
  CONSTRAINT fk_assignments_employee FOREIGN KEY (employee_id) REFERENCES employees(id),
  INDEX idx_assignments_asset_return (asset_id, return_date),
  INDEX idx_assignments_employee (employee_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS warranties (
  id BIGINT NOT NULL AUTO_INCREMENT,
  asset_id BIGINT NOT NULL,
  warranty_start DATE,
  warranty_end DATE,
  provider VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT uk_warranties_asset UNIQUE (asset_id),
  CONSTRAINT fk_warranties_asset FOREIGN KEY (asset_id) REFERENCES assets(id),
  INDEX idx_warranties_end (warranty_end)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS maintenance_records (
  id BIGINT NOT NULL AUTO_INCREMENT,
  asset_id BIGINT NOT NULL,
  reported_by BIGINT NOT NULL,
  issue_description TEXT NOT NULL,
  reported_date DATETIME(6),
  status VARCHAR(255),
  repair_cost DECIMAL(19,2),
  resolution TEXT,
  completed_date DATETIME(6),
  PRIMARY KEY (id),
  CONSTRAINT fk_maintenance_asset FOREIGN KEY (asset_id) REFERENCES assets(id),
  CONSTRAINT fk_maintenance_reported_by FOREIGN KEY (reported_by) REFERENCES employees(id),
  INDEX idx_maintenance_status (status),
  INDEX idx_maintenance_asset (asset_id)
) ENGINE=InnoDB;

-- Seed reference roles only. Create the first ADMIN account securely through
-- the application's provisioning flow; never insert a plain-text password.
INSERT IGNORE INTO roles (name) VALUES ('ADMIN'), ('EMPLOYEE');

INSERT IGNORE INTO categories (name, description) VALUES
 ('Laptop', 'Portable computers'),
 ('Desktop', 'Desktop computers'),
 ('Monitor', 'Display devices'),
 ('Mobile', 'Mobile phones and tablets'),
 ('Peripheral', 'Keyboards, mice and accessories'),
 ('Networking', 'Routers, switches and network equipment'),
 ('Other', 'Other company equipment');
