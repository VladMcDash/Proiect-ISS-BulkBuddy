# BulkBuddy — Gym Tracker (Meal & Health Tracker)

A production-ready Full-Stack application built with **Java Spring Boot 3** and **React 19 + TypeScript**.

## 🚀 Quick Start

### 1. Backend (Spring Boot)
- **Requirements**: Java 17+, Maven 3.x
- **Navigate to**: `cd backend`
- **Run**: `./mvnw spring-boot:run`
- **Port**: `http://localhost:8080` (H2 Console at `/h2-console`)

### 2. Frontend (React + Vite)
- **Requirements**: Node.js 18+, npm
- **Navigate to**: `cd frontend`
- **Install**: `npm install`
- **Run**: `npm run dev`
- **Port**: `http://localhost:5173`

## 👥 Default Accounts (Seeded)
The application comes pre-populated with standard meals, workouts, and the following test users:

| Username | Role | Password |
| :--- | :--- | :--- |
| `admin` | ADMIN | `admin` |
| `john` | USER | `password` |
| `jane` | USER | `password` |

## ✨ Core Features
- **Gram-based Tracking**: Precise meal logging with gram-level accuracy (no fixed portions).
- **Macro Goals**: Set and monitor daily calorie and protein targets.
- **Smart Progress**: Visual macro bars that update in real-time as you log meals and workouts.
- **Notification System**: Instant alerts when you reach your daily nutrition goals.
- **Admin Catalog**: Centralized database of standardized foods and exercises managed by admins.

## 🛠 Tech Stack
- **Backend**: Spring Boot 3.4.5, Spring Data JPA, Spring Security (JWT), H2 Database.
- **Frontend**: React 19, TypeScript, Tailwind CSS v4, Axios, Lucide Icons.
