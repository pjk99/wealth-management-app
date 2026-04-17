```
# Wealth Manager App

## Folder Structure (Monorepo Style) & Tech Stack

wealth-manager-app/
│
├── frontend/                 # React, Vite, AG-Grid
├── backend/                  # Java Spring Boot, PostgreSQL
├── ai-service/               # Python FastAPI
├── infrastructure/           # Docker, infra configs
├── shared/                   # Shared contracts
└── README.md

A full-stack financial application that turns messy Excel/CSV client data into a clean, structured, and normalized data.

---

## What it does

- Parses financial data from uploaded Excel/CSV
- Uses an LLM to standardize inconsistent fields across different files
- Example: (col headers) `acc #`, `acc no.`, `account number` → `account_number`
- Builds a consistent financial data model automatically

## Flow Summary
- 1. File uploaded from UI  
- 2. Backend parses raw Excel/CSV  
- 3. AI service normalizes column headers using LLM  
- 4. Backend maps raw data → clean financial schema  
- 5. Data stored in PostgreSQL and rendered in UI

## Local Setup

Follow these steps to run the project locally.

### 1. Clone the Repository
```bash
git clone https://github.com/pjk99/wealth-management-app.git
cd wealth-managemet-app

### 2. Setup Backend
Configure your database in application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_user
spring.datasource.password=your_password

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run

Ensure Backend runs on http://localhost:8080

### 3. Setup Frontend
```bash
cd frontend
npm run dev

Ensure Frontend runs on http://localhost:5173

### 4. AI Service setup
add .env file at ai-service/
add this line GEMINI_API_KEY=<your-gemini-api-key>

```bash
cd ai-service
python3 -m venv venv
source venv/bin/activate   # Mac/Linux
venv\Scripts\activate      # Windows
pip install -r requirements.txt
uvicorn app.main:app --reload

Ensure AI Service runs on http://localhost:8000
```

