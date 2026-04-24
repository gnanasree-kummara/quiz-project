# Quiz API Integration Project

## Approach

1. Called API 10 times with poll values (0–9)
2. Added 5-second delay between calls
3. Collected all events
4. Removed duplicates using (roundId + participant)
5. Aggregated scores per participant
6. Sorted leaderboard in descending order
7. Submitted final result

## Tech Used
- Python
- Requests library

## How to Run

```bash
pip install -r requirements.txt
python main.py


---

## 🟢 STEP 6: Push to GitHub

### Commands:

```bash
git init
git add .
git commit -m "quiz solution"
git branch -M main
git remote add origin https://github.com/your-username/quiz-project.git
git push -u origin main