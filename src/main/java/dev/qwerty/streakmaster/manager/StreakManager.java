package dev.qwerty.streakmaster.manager;

import dev.qwerty.streakmaster.StreakMaster;
import dev.qwerty.streakmaster.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StreakManager {
    
    private final StreakMaster plugin;
    
    public StreakManager(StreakMaster plugin) {
        this.plugin = plugin;
    }
    
    public void addKill(Player player) {
        PlayerData data = plugin.getDataManager().getPlayerData(player.getUniqueId());
        data.setCurrentStreak(data.getCurrentStreak() + 1);
        
        if (data.getCurrentStreak() > data.getRecord()) {
            data.setRecord(data.getCurrentStreak());
        }
        
        plugin.getMessageManager().sendKillMessage(player, data.getCurrentStreak(), data.getRecord());
        
        plugin.getMilestoneManager().checkMilestone(player, data.getCurrentStreak());
        plugin.getRewardManager().checkIntervalReward(player, data.getCurrentStreak());
    }
    
    public void resetStreak(Player player, Player killer) {
        PlayerData data = plugin.getDataManager().getPlayerData(player.getUniqueId());
        int oldStreak = data.getCurrentStreak();
        
        if (oldStreak > 0) {
            data.setCurrentStreak(0);
            plugin.getMessageManager().sendDeathMessage(player, data.getRecord());
            
            if (oldStreak >= 5 && killer != null) {
                plugin.getMessageManager().sendStreakEndedMessage(killer, player, oldStreak);
            }
        }
    }
    
    public void addStreak(UUID uuid, int amount) {
        PlayerData data = plugin.getDataManager().getPlayerData(uuid);
        data.setCurrentStreak(data.getCurrentStreak() + amount);
        
        if (data.getCurrentStreak() > data.getRecord()) {
            data.setRecord(data.getCurrentStreak());
        }
    }
    
    public void removeStreak(UUID uuid, int amount) {
        PlayerData data = plugin.getDataManager().getPlayerData(uuid);
        data.setCurrentStreak(Math.max(0, data.getCurrentStreak() - amount));
    }
    
    public void setStreak(UUID uuid, int amount) {
        PlayerData data = plugin.getDataManager().getPlayerData(uuid);
        data.setCurrentStreak(Math.max(0, amount));
        
        if (data.getCurrentStreak() > data.getRecord()) {
            data.setRecord(data.getCurrentStreak());
        }
    }
    
    public void resetStreak(UUID uuid) {
        PlayerData data = plugin.getDataManager().getPlayerData(uuid);
        data.setCurrentStreak(0);
    }
    
    public void addRecord(UUID uuid, int amount) {
        PlayerData data = plugin.getDataManager().getPlayerData(uuid);
        data.setRecord(data.getRecord() + amount);
    }
    
    public void removeRecord(UUID uuid, int amount) {
        PlayerData data = plugin.getDataManager().getPlayerData(uuid);
        data.setRecord(Math.max(0, data.getRecord() - amount));
    }
    
    public int getCurrentStreak(UUID uuid) {
        return plugin.getDataManager().getPlayerData(uuid).getCurrentStreak();
    }
    
    public int getRecord(UUID uuid) {
        return plugin.getDataManager().getPlayerData(uuid).getRecord();
    }
}