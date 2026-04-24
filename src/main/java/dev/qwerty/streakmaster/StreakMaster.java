package dev.qwerty.streakmaster;

import dev.qwerty.streakmaster.command.StreakCommand;
import dev.qwerty.streakmaster.hook.StreakExpansion;
import dev.qwerty.streakmaster.listener.PlayerDeathListener;
import dev.qwerty.streakmaster.manager.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Getter
public class StreakMaster extends JavaPlugin {
    
    private ConfigManager configManager;
    private MessageManager messageManager;
    private DataManager dataManager;
    private StreakManager streakManager;
    private AntiFarmManager antiFarmManager;
    private MilestoneManager milestoneManager;
    private RewardManager rewardManager;
    
    @Override
    public void onEnable() {
        loadConfigWithDefaults();
        
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.dataManager = new DataManager(this);
        this.streakManager = new StreakManager(this);
        this.antiFarmManager = new AntiFarmManager(this);
        this.milestoneManager = new MilestoneManager(this);
        this.rewardManager = new RewardManager(this);
        
        dataManager.loadAllData();
        
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        
        getCommand("streak").setExecutor(new StreakCommand(this));
        
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new StreakExpansion(this).register();
        }
    }
    
    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveAllData();
        }
    }
    
    private void loadConfigWithDefaults() {
        File configFile = new File(getDataFolder(), "config.yml");
        
        if (!configFile.exists()) {
            saveDefaultConfig();
            return;
        }
        
        FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(configFile);
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
            new InputStreamReader(getResource("config.yml"), StandardCharsets.UTF_8)
        );
        
        boolean updated = false;
        
        for (String key : defaultConfig.getKeys(true)) {
            if (!existingConfig.contains(key)) {
                existingConfig.set(key, defaultConfig.get(key));
                updated = true;
            }
        }
        
        if (updated) {
            try {
                existingConfig.save(configFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        reloadConfig();
    }
    
    public void reload() {
        saveConfig();
        
        loadConfigWithDefaults();
        
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.milestoneManager = new MilestoneManager(this);
        this.rewardManager = new RewardManager(this);
        this.antiFarmManager = new AntiFarmManager(this);
    }
}