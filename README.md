# Quarkus Reactive Product Management API

This is a small application built with **Quarkus Reactive** to create a RESTful API for a product management system. It supports standard CRUD operations and includes additional features like stock availability checks and sorting.

### 🚀 Technologies Used

* **Quarkus**: A Kubernetes-native Java stack tailored for OpenJDK HotSpot and GraalVM.
* **Hibernate Reactive with Panache**: For reactive, non-blocking data persistence and simplified entity management.
* **MySQL Reactive Client**: The reactive driver for connecting to a MySQL database.
* **Mutiny**: A reactive programming library used for handling asynchronous data streams.
* **RESTEasy Reactive**: For building the RESTful API endpoints.
* **Jakarta Bean Validation**: For validating data on the `Product` entity.

***

### ⚙️ How to Run the Application

#### Prerequisites
* Java 17+
* Maven
* Docker (for running MySQL)

#### 1. Start the MySQL Database
Use Docker to quickly set up a local MySQL instance:

```bash
docker run --ulimit nofile=10240:10240 --name quarkus-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=productdb -p 3306:3306 -d mysql:8

#### 2. Access swagger api documentation 
http://localhost:8080/q/swagger-ui/