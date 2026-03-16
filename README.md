# Task Management API ⚙️

A robust RESTful API built with Java and Spring Boot to manage users and tasks. It features secure JWT-based authentication and Role-Based Access Control (RBAC) to differentiate between standard users and administrators.

## 🚀 Tech Stack
* **Java 17+**
* **Spring Boot 3.x+** (Web, Data JPA, Security)
* **Spring Security & JWT** (Authentication & Authorization)
* **Database:** Microsoft SQL Server
* **Hibernate** (ORM)
* **Build Tool:** Maven
* **API Documentation:** SpringDoc OpenAPI (Swagger UI)

## ✨ Key Features
* **Authentication:** Secure user registration and login using JWT tokens.
* **Role-Based Access Control (RBAC):** * `USER`: Can create, read, and update their own tasks.
  * `ADMIN`: Has full system oversight, can manage all tasks, and delete users (with cascading task deletion).
* **Task Management:** Full CRUD functionality for tasks with dynamic ownership validation.
* **Admin Dashboard Support:** Specialized endpoints for user management and system-wide task retrieval.
* **Interactive API Docs:** Built-in Swagger UI for testing endpoints directly from the browser.

## 🛠️ Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/task-management-api.git](https://github.com/yourusername/task-management-api.git)
   cd task-management-api
   
2. **Configure the Database (Microsoft SQL Server):**
  Ensure your SQL Server instance is running. The application uses the following src/main/resources/application.yaml configuration. Update the username and password if your local setup differs:
 ```bash
   spring:
   application:
     name: task-management-api
   datasource:
     url: jdbc:sqlserver://localhost:1433;databaseName=TaskManagementDB;encrypt=false
     username: sa
     password: 123
     driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
   jpa:
     hibernate:
       ddl-auto: update
     show-sql: true
     properties:
       hibernate:
         dialect: org.hibernate.dialect.SQLServerDialect
   springdoc:
     api-docs:
       path: /api-docs
     swagger-ui:
       path: /swagger-ui.html
       operations-sorter: alpha
       tags-sorter: alpha
     show-actuator: true

3. **📚 API Documentation (Swagger)**
   Once the application is running, you can interact with the API endpoints and view the full documentation via Swagger UI.
   Swagger UI: http://localhost:8080/swagger-ui.html
   
