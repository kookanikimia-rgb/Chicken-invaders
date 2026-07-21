# Chicken-invaders

## Student Information

**Name:** kimia kookani

**Student ID:** 40412029

**GitHub Repository:**
https://github.com/kookanikimia-rgb/Chicken-invaders

---

## Project Description

Chicken Invaders is a 2D arcade game developed using Java Swing and SQLite.

Features:

- Login & Register
- Main Menu
- 8 Levels
- Normal, Shooter, ZigZag and Fast enemies
- Boss Battles
- Power-ups
- Sound Effects
- Background Music
- High Scores
- Game History
- Settings
- How To Play

---

## Requirements

- Java JDK 17+
- IntelliJ IDEA

SQLite database (`game.db`) is created automatically.

---

## How to Run

Run `Main.java` from IntelliJ IDEA.

Dependencies:
- SQLite JDBC Driver: Libs/sqlite-jdbc-3.53.2.0.jar

If IntelliJ does not detect the library automatically:
File → Project Structure → Modules → Dependencies → + → JARs or Directories
Select: Libs/sqlite-jdbc-3.53.2.0.jar
---

## Game Controls

| Key | Action |
|------|--------|
|← → ↑ ↓ or W A S D | Move |
| SPACE | Shoot |
| P | Pause |
| ESC | Return to Menu |

---

## Database

The project uses SQLite.

Database file:
game.db

Tables:

### users

- id
- username
- password
- high_score
- current_level
- bg_music
- shot_sound
- crash_sound
- game_over_sound

### game_history

Stores every game including:

- id
- username
- score
- level
- played_at
- sound settings

---

## GitHub Repository

https://github.com/kookanikimia-rgb/Chicken-invaders