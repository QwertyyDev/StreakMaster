package dev.qwerty.streakmaster.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class KillRecord {
    
    private final UUID victimUUID;
    private final long timestamp;
}