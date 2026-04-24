package dev.qwerty.streakmaster.data;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {
    
    private final UUID uuid;
    private int currentStreak;
    private int record;
    
    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.currentStreak = 0;
        this.record = 0;
    }
}