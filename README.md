# Task Management API

This is a simple RESTful API for managing tasks, built with Spring Boot. Including Domain-Driven Design (DDD), Test-Driven Development (TDD), and clean coding practices.

## Features

-   **CRUD Operations:** Create, Read, Update, and Delete tasks.
-   **RESTful Endpoints:** Standardized API with meaningful URIs and HTTP methods.
-   **Validation:** Ensures required fields are present and dates are valid.
-   **Filtering & Pagination:** The `GET /tasks` endpoint supports filtering by task status and paginating results.
-   **Sorting:** All tasks are returned sorted by `due_date`.
-   **DDD Architecture:** Code is separated into domain, application, and infrastructure layers.
-   **TDD:** The application is built with a focus on comprehensive unit and integration tests.
-   **In-Memory Data Store:** Uses a simple `ConcurrentHashMap` for persistence,database is not used.

## Getting Started

### Prerequisites

-   Java 17 or higher
-   Maven 3.6 or higher

### How to Run:

**1. Clone the repository to your local machine:** 

```bash
git clone [https://github.com/reubenkl/taskManagementAPI.git]
cd taskManagementAPI
```

**2. Navigate to the projects root directory**

**3. Execute the following command to boot up the application**
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`.

### How to Run Tests:
```bash
mvn test
```

### TO Test APIs using Postman
**Create Task: /api/v1/tasks**
```dtd
Request Payload:

{
"title": "Title 1",
"description": "Title 1 Description",
"due_date": "2025-08-12"
}
```