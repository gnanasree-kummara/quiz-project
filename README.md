# Quiz API Integration Project (Java)

## 📌 Overview

This project implements a backend integration workflow where data is fetched from an external API, processed, and submitted as a final result.

The system simulates a quiz platform where participants receive scores across multiple rounds. Due to distributed system behavior, duplicate API responses may occur and must be handled correctly.

---

## 🎯 Objective

* Fetch quiz data using 10 API polls
* Handle duplicate responses
* Compute total scores per participant
* Generate a sorted leaderboard
* Submit the final result

---

## ⚙️ Approach

### 🔹 API Polling

* Called API with poll values from `0` to `9`
* Maintained a **5-second delay** between requests

### 🔹 Data Collection

* Extracted `events` from each API response
* Combined all responses into a single list

### 🔹 Deduplication

* Removed duplicates using:

  ```
  (roundId + participant)
  ```
* Ensures each participant’s score per round is counted only once

### 🔹 Score Aggregation

* Used a `HashMap` to sum scores per participant

### 🔹 Leaderboard

* Created a list of participants with total scores
* Sorted in descending order

### 🔹 Submission

* Final leaderboard is submitted using a POST request
* Submission is controlled to run only once

---

## 🛠️ Tech Stack

* Java
* HTTPURLConnection (for API calls)
* org.json library (for JSON parsing)

---

## 📂 Project Structure

```
quiz-project/
│
├── Main.java
├── Main.class
├── json-20210307.jar
└── README.md
```

---

## ▶️ How to Run

### 1. Compile

```
javac -cp ".;json-20210307.jar" Main.java
```

### 2. Run

```
java -cp ".;json-20210307.jar" Main
```

---

## 📤 Sample Output

```
Fetched poll 0
Fetched poll 1
...
Fetched poll 9

Leaderboard:
{"totalScore":295,"participant":"Bob"}
{"totalScore":280,"participant":"Alice"}
{"totalScore":260,"participant":"Charlie"}
```

---

## ⚠️ Important Notes

* Exactly **10 API calls** are made
* A strict **5-second delay** is maintained
* Duplicate API responses are handled correctly
* Submission is executed only once
* Designed considering distributed system behavior

---

## 🚀 Key Learnings

* API integration in Java
* Handling duplicate data
* Data aggregation and sorting
* Importance of idempotency in backend systems

---

## 👤 Author

* Name: KUMMARA GNANASREE
* Reg No: AP23110011650

---
