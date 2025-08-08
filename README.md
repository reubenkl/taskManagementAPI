# Task Management API

This is a simple RESTful API for managing tasks, built with Spring Boot. The project demonstrates key software engineering principles, including Domain-Driven Design (DDD), Test-Driven Development (TDD), and clean coding practices.

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

### Installation

Clone the repository to your local machine:

```bash
git clone [https://github.com/your-username/task-manager.git](https://github.com/your-username/task-manager.git)
cd task-manager