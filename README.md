# Wizard Tower Defence

A simple tower defense game for the INFO1113 assignment.

## Gameplay

- The board is a 20x20 grid of 32x32 pixel tiles.
- Game window size: 760x680 (640x640 board + 40 pixel top info bar + 120 pixel right sidebar).
- Waves of enemies attack along a set path. Players must defend by placing towers.
- Players can build towers, upgrade tower range, speed, and damage, and manage mana.
- Hovering over action buttons shows tooltip costs.

## Project Structure

- Main package: WizardTD
- Entry point: `WizardTD.App`
- Dependencies:
  - Processing (graphics framework)
  - Guava (utilities)
  - JUnit 5 (testing)

## Build and Run

Requires Java 8+ and Gradle.

To build and run:

```bash
./gradlew build
./gradlew run
```

The application will start the tower defense game window.