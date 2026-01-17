# 🅿️ ParkingSystemBackend

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)

## 📖 Overview

**ParkingSystemBackend** is a system for parking app with high-performance,
reactive backend service designed to manage modern parking systems.
Built with **Kotlin** and **Spring Boot WebFlux**, it uses a non-blocking architecture to handle high concurrency efficiently.
This system serves as the core engine for reservation management, user authentication, and real-time parking slot availability.

## ✨ Key Features

*   **🔐 Robust Authentication**: Secure access using JWT (JSON Web Token) and Refresh Tokens via Spring Security OAuth2.
*   **📅 Reservation System**: Full lifecycle management of parking reservations (Create, Update, Cancel).
*   **🚗 Slot Management**: Real-time tracking and management of parking slots.
*   **🗓️ Availability Calendar**: View and manage availability across dates.
*   **👤 User Management**: Administration of user profiles and data.

## 🛠️ Tech Stack

This project uses a cutting-edge stack to ensure performance and maintainability:

*   **Language**: [Kotlin](https://kotlinlang.org/) (JVM 21)
*   **Framework**: [Spring Boot 4](https://spring.io/projects/spring-boot) (Reactive / WebFlux)
*   **Database**: [MongoDB](https://www.mongodb.com/) (Reactive Driver)
*   **Security**: Spring Security & OAuth2 Resource Server
*   **Build Tool**: Gradle (Kotlin DSL)

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed:
*   **Java 21 SDK**
*   **MongoDB** (Running locally on default port `27017`)

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/hammafataka/parking-system-backend.git
    cd parking-system-backend
    ```

2.  **Configure Environment**
    The application defaults to `localhost` for MongoDB. If your setup differs, update `src/main/resources/application.yaml`:
    ```yaml
    spring:
      data:
        mongodb:
          uri: mongodb://user:pass@host:27017/parking
    ```

3.  **Run the Application**
    ```bash
    ./gradlew bootRun
    ```

    The server will start on `http://localhost:8080`.

## 🔌 API Endpoints

| Resource | Description |
| :--- | :--- |
| **Auth** | Login, Refresh Token, and Key management. |
| **Reservations** | Book and manage parking spots. |
| **Slots** | CRUD operations for parking slots. |
| **Calendar** | Check availability for specific dates. |
| **Users** | Manage system users. |

## 🏗️ Build & Test

To build the project without running tests:
```bash
./gradlew build -x test
```

To run the test suite:
```bash
./gradlew test
```

