package dev.qwerty.streakmaster.hook;

import dev.qwerty.streakmaster.StreakMaster;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StreakExpansion extends PlaceholderExpansion {
    
    private final StreakMaster plugin;
    
    public StreakExpansion(StreakMaster plugin) {
        this.plugin = plugin;
    }
    
    @Override
    @NotNull
    public String getIdentifier() {
        return "streak";
    }
    
    @Override
    @NotNull
    public String getAuthor() {
        return "dev.qwerty";
    }
    
    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        
        if (params.equals("current")) {
            return String.valueOf(plugin.getStreakManager().getCurrentStreak(player.getUniqueId()));
        }
        
        if (params.equals("record")) {
            return String.valueOf(plugin.getStreakManager().getRecord(player.getUniqueId()));
        }
        
        if (params.startsWith("top_active_name_")) {
            String[] parts = params.split("_");
            int position = Integer.parseInt(parts[3]);
            
            List<Map.Entry<UUID, Integer>> topActive = plugin.getDataManager().getTopActiveStreaks(10);
            
            if (position <= topActive.size()) {
                UUID uuid = topActive.get(position - 1).getKey();
                OfflinePlayer topPlayer = Bukkit.getOfflinePlayer(uuid);
                return topPlayer.getName() != null ? topPlayer.getName() : "Unknown";
            }
            
            return "-";
        }
        
        if (params.startsWith("top_active_value_")) {
            String[] parts = params.split("_");
            int position = Integer.parseInt(parts[3]);
            
            List<Map.Entry<UUID, Integer>> topActive = plugin.getDataManager().getTopActiveStreaks(10);
            
            if (position <= topActive.size()) {
                return String.valueOf(topActive.get(position - 1).getValue());
            }
            
            return "0";
        }
        
        if (params.startsWith("top_record_name_")) {
            String[] parts = params.split("_");
            int position = Integer.parseInt(parts[3]);
            
            List<Map.Entry<UUID, Integer>> topRecords = plugin.getDataManager().getTopRecords(10);
            
            if (position <= topRecords.size()) {
                UUID uuid = topRecords.get(position - 1).getKey();
                OfflinePlayer topPlayer = Bukkit.getOfflinePlayer(uuid);
                return topPlayer.getName() != null ? topPlayer.getName() : "Unknown";
            }
            
            return "-";
        }
        
        if (params.startsWith("top_record_value_")) {
            String[] parts = params.split("_");
            int position = Integer.parseInt(parts[3]);
            
            List<Map.Entry<UUID, Integer>> topRecords = plugin.getDataManager().getTopRecords(10);
            
            if (position <= topRecords.size()) {
                return String.valueOf(topRecords.get(position - 1).getValue());
            }
            
            return "0";
        }
        
        return null;
    }
}