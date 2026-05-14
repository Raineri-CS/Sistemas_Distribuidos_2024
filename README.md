# Sistemas Distribuídos 2024

A Java-based distributed recruitment and job board system demonstrating client-server architecture, concurrent request handling, and remote database management. This repository contains the final project for a Distributed Systems curriculum.

## Overview

The application is structured into a multi-threaded server managing multiple simultaneous client connections and a client application. It simulates a recruitment platform where candidates and companies can manage profiles, skills, and job postings via authenticated requests.

## System Architecture & Key Features

* Multi-threaded Server Execution: Utilizes Java's `ExecutorService` (cached thread pool). Each incoming connection is delegated to a dedicated `ClientHandler` thread, ensuring concurrent processing without blocking the main server loop.
* JWT Authentication: Implements JSON Web Tokens (JWT) for secure session management, state validation, and role-based claims.
* Relational Domain Model: The system utilizes a normalized MySQL schema (`schema.sql`) to manage complex relations between Candidates, Companies, Job Postings, and a mapped Skill/Experience dataset.
* Network Communication: Implemented via standard Java Sockets over TCP/IP, utilizing buffered input and output streams for continuous data exchange.
* Data Persistence: Integrates with a MySQL database via JDBC. The `DatabaseManager` class abstracts all SQL queries, ensuring a clean separation between networking logic and data access.

## Technologies Used

* Java
* Java Sockets API (TCP/IP)
* Java Concurrency API (`java.util.concurrent`)
* JWT (Auth0)
* JDBC & MySQL
* JavaFX (Server-side visualization)

## Setup and Execution

### Prerequisites
1. A local or remote MySQL server running.
2. Initialize the database using the provided `schema.sql` to generate the `service` database and its relational tables.
3. Update the `DatabaseManager.java` file with your environment's MySQL user and password credentials.

### Running the Server
Compile the server module and execute the main application to start the thread pool and listen for incoming socket connections on the configured port. 

### Running the Client
Compile the client module and execute the main client class. Use the Command Line Interface to navigate the menus, authenticate via JWT, and execute remote database operations.
