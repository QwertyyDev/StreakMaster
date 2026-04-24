package dev.qwerty.streakmaster.manager;

import dev.qwerty.streakmaster.StreakMaster;
import dev.qwerty.streakmaster.data.PlayerData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class DataManager {
    
    private final StreakMaster plugin;
    private final Map<UUID, PlayerData> playerDataMap;
    private final File dataFile;
    private FileConfiguration dataConfig;
    
    public DataManager(StreakMaster plugin) {
        this.plugin = plugin;
        this.playerDataMap = new ConcurrentHashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }
    
    public void loadAllData() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            dataConfig = YamlConfiguration.loadConfiguration(dataFile);
            
            if (dataConfig.contains("players")) {
                for (String uuidString : dataConfig.getConfigurationSection("players").getKeys(false)) {
                    UUID uuid = UUID.fromString(uuidString);
                    int currentStreak = dataConfig.getInt("players." + uuidString + ".current", 0);
                    int record = dataConfig.getInt("players." + uuidString + ".record", 0);
                    
                    PlayerData data = new PlayerData(uuid);
                    data.setCurrentStreak(currentStreak);
                    data.setRecord(record);
                    
                    playerDataMap.put(uuid, data);
                }
            }
        });
    }
    
    public void saveAllData() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
                UUID uuid = entry.getKey();
                PlayerData data = entry.getValue();
                
                dataConfig.set("players." + uuid.toString() + ".current", data.getCurrentStreak());
                dataConfig.set("players." + uuid.toString() + ".record", data.getRecord());
            }
            
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, PlayerData::new);
    }
    
    public List<Map.Entry<UUID, Integer>> getTopActiveStreaks(int limit) {
        List<Map.Entry<UUID, Integer>> list = new ArrayList<>();
        
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            list.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().getCurrentStreak()));
        }
        
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        return list.subList(0, Math.min(limit, list.size()));
    }
    
    public List<Map.Entry<UUID, Integer>> getTopRecords(int limit) {
        List<Map.Entry<UUID, Integer>> list = new ArrayList<>();
        
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            list.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().getRecord()));
        }
        
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        return list.subList(0, Math.min(limit, list.size()));
    }
}