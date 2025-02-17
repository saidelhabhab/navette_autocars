# 🚐 Navette - Transport Management Application

Navette is a transport management platform built using **Spring Boot** for the backend and **Angular** for the frontend. The application allows users to manage transportation services, view available shuttles, and make reservations.

---

## 🌟 Features

✅ User Authentication (Login & Registration)  
✅ Manage Transport Services (CRUD Operations)  
✅ Search and Filter Shuttles  
✅ Price Calculation and Subscription Plans (Monthly, Quarterly, Semi-Annual, Annual)  
✅ Responsive UI using Bootstrap & Material Design  
✅ Secure Payment Integration (if applicable)  
✅ Contact Form & Customer Support  

---

## 🛠️ Tech Stack

### Backend (Spring Boot)
- **Spring Boot 3**
- **Spring Security & JWT** (Authentication & Authorization)
- **Spring Data JPA** (Database Management)
- **Hibernate** (ORM for MySQL)
- **RESTful API** (Communication with Frontend)
- **Lombok** (Code Simplification)
- **Maven** (Dependency Management)

### Frontend (Angular)
- **Angular 16+**
- **Bootstrap 5 & Material Angular** (UI Components)
- **RxJS** (State Management)
- **ngx-toastr** (Notifications)
- **ng-bootstrap** (UI Enhancements)
- **ApexCharts** (Data Visualization)

---

## 🚀 Installation & Setup

### Prerequisites
- **Java 17+** (For Spring Boot)
- **Node.js 18+** (For Angular)
- **MySQL** (Database)
- **Maven** (Build tool)
- **Angular CLI** (For running the frontend)

### 1️⃣ Backend Setup (Spring Boot)
```sh
# Clone the repository
git clone https://github.com/saidelhabhab/navette-backend.git
cd navette-backend

# Configure database (application.properties)
# Change MySQL username & password as needed
# spring.datasource.url=jdbc:mysql://localhost:3306/navette_db
# spring.datasource.username=root
# spring.datasource.password=root

# Build & run the Spring Boot application
mvn clean install
mvn spring-boot:run

API will be available at: http://localhost:8080/api

### 2️⃣ Frontend Setup (Angular)
sh
Copy
Edit
# Clone the repository
git clone https://github.com/yourusername/navette-frontend.git
cd navette-frontend

# Install dependencies
npm install

# Run Angular application
ng serve --open
Frontend will run at: http://localhost:4200

## 🎯 API Endpoints (Spring Boot)

Method	Endpoint	Description
POST	/api/auth/signup	Register new user
POST	/api/auth/login	User login
GET	/api/navettes	Get all available shuttles
POST	/api/navettes	Add a new shuttle
PUT	/api/navettes/{id}	Update shuttle details
DELETE	/api/navettes/{id}	Delete a shuttle

## 🔒 Security & Authentication

JWT-based authentication is used for securing API endpoints.
Users must log in to access certain features (role-based access control).
Passwords are encrypted using BCrypt.

## 📸 Screenshots

soon 

## 📌 Deployment

###🏗️ Build & Deploy Backend (Spring Boot)

mvn clean package
Deploy on a server (Tomcat, AWS, etc.)

### 🌐 Deploy Angular App

Build for production:
ng build --prod
Deploy on Firebase, Netlify, or Vercel.

## 📢 Contributing

Want to improve Navette? Feel free to fork the project, make improvements, and submit a pull request! 🚀

## 📄 License

This project is MIT Licensed.

Made with ❤️ by Said Elhabhab
