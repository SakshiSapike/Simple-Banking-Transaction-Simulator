# Simple-Banking-Transaction-Simulator
The Simple Banking Transaction Simulator is a Java-based application that simulates basic banking operations such as account creation, fund transfers, and balance management. It enforces banking rules like insufficient balance checks and daily transaction limits while maintaining transaction and alert logs using file handling.

# 🏦 Banking Transaction Simulator (Java)

## 📌 Project Overview
The **Banking Transaction Simulator** is a Java-based application designed to simulate core banking operations such as account creation, money transfer, balance monitoring, and transaction logging.

This project demonstrates **real-world backend design concepts** including:
- Layered architecture
- File-based transaction logging
- Observer pattern for alerts
- Input validation
- Service-oriented design
- Optional REST API testing using Postman

The project is implemented using **Core Java** and is extendable to **Spring Boot** 

---

## 🛠️ Technologies Used
- **Java 17**
- **Maven**
- **Spring Boot (starter-web)**
- **File I/O** (`FileWriter`)
- **Collections Framework**
)
-src/
-├── methods/
=│ ├── Account.java
-│ ├── AccountType.java
-│
-├── Service/
-│ ├── BankService.java
-│ ├── BankSystem.java
-│
-├── Observers/
-│ ├── BalanceObserver.java
-│ ├── BalanceAlertTracker.java
-│
-├── data/
-│ └── transactions.log (auto-generated)
-│
-└── BankingSimulationApplication.java (Spring Boot entry point)


---

## ✨ Features Implemented

### 1️⃣ Account Management
- Create bank accounts
- Store user details (Name, Email, Mobile)
- Activate / deactivate accounts
- Account types: `SAVINGS`, `CURRENT`

### 2️⃣ Transaction Handling
- Money transfer between accounts
- Daily transaction limit enforcement
- Self-transfer prevention
- Proper exception handling

### 3️⃣ File-Based Logging (Audit Trail)
All transactions are logged to a file:

Each log entry contains:
- Timestamp
- Account ID
- Transaction type
- Amount
- Status (SUCCESS / FAILED)
- Failure reason (if any)

### 4️⃣ Balance Alert System (Observer Pattern)
- Automatic alert when balance falls below minimum threshold
- Alerts are:
  - Printed on console
  - Logged to the same log file
- Observer logic is decoupled from business logic

### 5️⃣ Console-Based User Interaction
- Menu-driven interface
- Scanner-based input
- Input validation for enums, email, mobile number

---

## 🚀 How to Run the Project (Console Mode)

### Prerequisites
- Java 17+
- Maven
- IntelliJ IDEA / Eclipse / VS Code

### Steps
1. Clone or extract the project
2. Open the project in IDE
3. Ensure Java version is set to **17**
4. Run:
   ```bash
