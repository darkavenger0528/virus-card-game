# 🦠 Virus! — Java Card Game

A digital adaptation of the Virus! card game, built in Java with a graphical interface (Swing). Players compete to build a complete healthy body while infecting and sabotaging their opponents.

> Universidad EAN — Object-Oriented Programming  
> Author: Justin Thomas Moreno Solano

---

## Gameplay

Each player tries to collect **4 healthy organs** (red, green, yellow, and blue) before their opponents. Use virus cards to infect enemies, medicine cards to protect your organs, and special treatment cards to turn the tide.

### Card types

| Card | Description |
|---|---|
| 🫀 **Organ** | Place it in your body. Collect 4 different colors to win. |
| 🦠 **Virus** | Infect an opponent's organ of the same color. Two viruses extirpate it. |
| 💊 **Medicine** | Vaccinate a healthy organ or cure an infected one. Two medicines = immune. |
| 🔄 **Treatment** | Special effects: Transplant, Theft, Infection, Latex Glove, Medical Error. |
| ⛔ **Block** | Skip the next player's turn. |
| 🃏 **Throw Two** | Play an extra card this turn. |

### Organ states

```
Healthy → (virus) → Infected → (second virus) → Extirpated ❌
Healthy → (medicine) → Vaccinated → (second medicine) → Immune 🛡️
Vaccinated → (virus) → Healthy (vaccination cancelled)
```

---

## Requirements

- **Java 8 or higher**

Check your version:
```bash
java -version
```

---

## How to run

### Terminal

```bash
# 1. Compile
javac -d bin *.java

# 2. Run
java -cp bin VirusGameGUI
```

### IDE (IntelliJ, Eclipse, NetBeans, VS Code)

1. Import all `.java` files into a new Java project.
2. Make sure the `package model;` declaration matches your folder structure.
3. Set `VirusGameGUI` as the main class and run.

---

## Project structure

```
.
├── Card.java            # Abstract base class for all cards
├── OrganCard.java       # Organ logic (infected, vaccinated, immune states)
├── VirusCard.java       # Infects organs
├── MedicineCard.java    # Cures or protects organs
├── TreatmentCard.java   # Special treatment effects
├── BlockCard.java       # Skips next player's turn
├── ThrowTwoCard.java    # Allows playing an extra card
├── Deck.java            # Deck initialization, drawing, discarding
├── Player.java          # Hand, body, drawing and playing logic
├── Game.java            # Game state, turn management, win condition
└── VirusGameGUI.java    # Swing graphical interface
```

---

## Troubleshooting

**`Main class VirusGameGUI not found`**  
Make sure you compiled with the correct casing:
```bash
javac -d bin *.java
java -cp bin VirusGameGUI
```

**Missing files error**  
Verify all `.java` files are present and that `package model;` matches your folder layout.

**GUI doesn't load**  
Confirm you're running Java 8+ and that your system supports Swing (works on Windows, macOS, and Linux).
