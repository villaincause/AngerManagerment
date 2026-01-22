# Anger Management (JavaFX Game)

Anger Management is a two-player JavaFX game inspired by Rock-Paper-Scissors, enhanced with emotional states, action-based scoring, sound effects, and keyboard controls. The game emphasizes strategy and decision-making rather than pure luck.

## Gameplay
- Players choose **Rock, Paper, or Scissors**
- The round winner selects an action (**Punch, Kick, or Slap**)
- Actions affect both players’ psychological states
- Scores are calculated dynamically using a custom logic
- The first player to reach **50 points** wins

## Player States
Each player has three psychological attributes:
- **Anger**
- **Satisfaction**
- **Confidence**

These values change every round and directly influence scoring. Progress bars visually represent each state.

## Controls
**Player 1**
- Q → Rock  
- W → Paper  
- E → Scissors  

**Player 2**
- I → Rock  
- O → Paper  
- P → Scissors  

Mouse input is also supported.

## Features
- JavaFX-based UI
- Keyboard and mouse controls
- Sound effects and background music
- Animated action messages
- End-game popup with winner announcement

## Tech Stack
- Java
- JavaFX
- CSS (UI styling)

## Win Condition
The game ends when a player reaches **50 points** or when the opponent gives up.

## Notes
This project was created for academic and learning purposes.
