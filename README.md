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

The project includes the SQLite JDBC driver inside the `Libs` folder.

The SQLite database (`game.db`) is created automatically on first run.

---
## Project Structure

```
Chicken-invaders/
│
├── src/
├── Assets/
├── Libs/
│   └── sqlite-jdbc-3.53.2.0.jar
├── game.db
├── README.md
└── .gitignore
```

The SQLite JDBC driver is included in the `Libs` folder.

If IntelliJ does not add it automatically, add:

**File → Project Structure → Modules → Dependencies → + → JARs or Directories**

and select:

`Libs/sqlite-jdbc-3.53.2.0.jar`

---

## How to Run

1. Open the project in IntelliJ IDEA.
2. Make sure the SQLite JDBC library (`Libs/sqlite-jdbc-3.53.2.0.jar`) is added as a dependency.
3. Run `Main.java`.
4. The database (`game.db`) will be created automatically if it does not already exist.

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
- selected_plane

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