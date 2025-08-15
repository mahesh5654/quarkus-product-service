# Quarkus Reactive Product Management API

This is a **Quarkus Reactive** application that exposes a RESTful API for managing products.  
It supports **CRUD** operations, stock availability checks, and sorting.  
The application uses **reactive programming** for high scalability and low-latency database interactions.

---

## 🚀 Technologies Used

- **Quarkus** – Kubernetes-native Java stack for OpenJDK HotSpot & GraalVM
- **Hibernate Reactive with Panache** – Non-blocking ORM with simplified entity management
- **MySQL Reactive Client** – Reactive MySQL driver
- **Mutiny** – Reactive programming library for async event streams
- **RESTEasy Reactive** – Reactive REST API framework for Quarkus
- **Jakarta Bean Validation** – Entity data validation with annotations

---

## ⚙️ Prerequisites

Before you start, ensure you have the following installed:

- **Java 17+**
- **Maven 3.9+**
- **Docker** (if you want to run MySQL locally in a container)

---

## 🛠 Local Setup & Run

### 1️⃣ Start MySQL in Docker
Run the following to start MySQL 8 in a container:
```bash
docker run --ulimit nofile=10240:10240 --name quarkus-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=productdb \
  -p 3306:3306 \
  -d mysql:8
