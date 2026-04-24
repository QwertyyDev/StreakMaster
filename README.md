# StreakMaster

StreakMaster is a high-quality, fully customizable killstreak plugin designed for Spigot/Paper servers. It provides a powerful and intelligent killstreak system with advanced anti-abuse protection, premium messaging, and deep configuration support.

## Features

- Real-time killstreak tracking  
- Advanced anti-farm / anti-abuse system  
- Fully configurable milestone system  
- Command-based reward system (flexible)  
- Custom sounds (volume & pitch adjustable)  
- Smart broadcast system  
- Highly detailed and editable config  
- Full PlaceholderAPI support  
- Admin control commands  
- Optimized and performance-friendly  

## How It Works

- Each time a player kills another player → their streak increases  
- If a player dies → their streak resets  
- Milestones reward players with commands, messages, and sounds  
- Anti-abuse system prevents fake streak farming  

## Anti-Abuse Protection

StreakMaster includes a smart detection system to prevent farming:

- Same IP kills are ignored  
- Repeated kills on the same player don’t count  
- Detects suspicious patterns like:
  - Kill loops  
  - Alternating accounts  
  - Fast repeated kills  
- Fully configurable (enable/disable & thresholds)  

## Milestones

Default milestones:

```
3, 5, 10, 15, 20, 25, 30
```

Each milestone supports:

- Custom message  
- Custom sound  
- Volume & pitch  
- Broadcast toggle  
- Multiple reward commands  

## Rewards

Two reward systems:

### Milestone Rewards
Given when reaching specific streak values  

### Interval Rewards
Example:

- 5–50 → reward A  
- 51–100 → reward B  

## Messaging

- Premium styled messages  
- No prefix clutter  
- Fully customizable  
- Per-player and global notifications  
- Sound effects supported  

## Commands

```
/streak add <player> <amount>
/streak remove <player> <amount>
/streak set <player> <amount>
/streak reset <player>
/streak addrecord <player> <amount>
/streak removerecord <player> <amount>
/streak reload
```

## Placeholders

### Player:
- %streak_current%  
- %streak_record%  

### Top Active Players:
- %streak_top_active_name_1% → %streak_top_active_name_10%  
- %streak_top_active_value_1% → %streak_top_active_value_10%  

### Top Records:
- %streak_top_record_name_1% → %streak_top_record_name_10%  
- %streak_top_record_value_1% → %streak_top_record_value_10%  

## Configuration

Everything is configurable:

- Messages  
- Sounds  
- Rewards  
- Milestones  
- Anti-abuse system  
- Broadcast behavior  
- Minimum player requirement  

Config is:

- Clean and organized  
- Fully documented in Turkish  
- Easy to edit  

## Installation

1. Download the plugin  
2. Place it in your `/plugins` folder  
3. Start the server  
4. Edit the config if needed  
5. Use `/streak reload` after changes  

## Notes

- Designed for both small and large servers  
- Works seamlessly with PlaceholderAPI  
- Built for performance and stability  
- No unnecessary complexity in usage  

Enjoy a premium-level killstreak experience with StreakMaster.
