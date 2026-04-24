package dev.qwerty.streakmaster.manager;

import dev.qwerty.streakmaster.StreakMaster;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class ConfigManager {
    
    private final StreakMaster plugin;
    private final FileConfiguration config;
    
    private final int minimumPlayers;
    private final boolean resetOnQuit;
    
    private final boolean sameIpEnabled;
    private final boolean sameIpLog;
    
    private final boolean samePlayerEnabled;
    private final int requiredDifferentKills;
    private final boolean samePlayerLog;
    
    private final boolean patternDetectionEnabled;
    private final boolean alternatingKillsEnabled;
    private final int alternatingThreshold;
    private final int alternatingTimeWindow;
    
    private final boolean smallPoolEnabled;
    private final int checkLastKills;
    private final int minimumUniquePlayersInPool;
    
    private final boolean rapidRepeatedEnabled;
    private final int rapidTimeThreshold;
    private final int rapidRepeatCount;
    
    private final boolean blockStreakGain;
    private final boolean patternLog;
    private final boolean notifyAdmins;
    
    private final boolean globalMilestoneSounds;
    
    public ConfigManager(StreakMaster plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        
        this.minimumPlayers = config.getInt("minimum-players", 3);
        this.resetOnQuit = config.getBoolean("reset-on-quit", false);
        
        this.sameIpEnabled = config.getBoolean("anti-farm.same-ip.enabled", true);
        this.sameIpLog = config.getBoolean("anti-farm.same-ip.log", true);
        
        this.samePlayerEnabled = config.getBoolean("anti-farm.same-player.enabled", true);
        this.requiredDifferentKills = config.getInt("anti-farm.same-player.required-different-kills", 1);
        this.samePlayerLog = config.getBoolean("anti-farm.same-player.log", true);
        
        this.patternDetectionEnabled = config.getBoolean("anti-farm.pattern-detection.enabled", true);
        this.alternatingKillsEnabled = config.getBoolean("anti-farm.pattern-detection.alternating-kills.enabled", true);
        this.alternatingThreshold = config.getInt("anti-farm.pattern-detection.alternating-kills.threshold", 3);
        this.alternatingTimeWindow = config.getInt("anti-farm.pattern-detection.alternating-kills.time-window", 300);
        
        this.smallPoolEnabled = config.getBoolean("anti-farm.pattern-detection.small-pool.enabled", true);
        this.checkLastKills = config.getInt("anti-farm.pattern-detection.small-pool.check-last-kills", 10);
        this.minimumUniquePlayersInPool = config.getInt("anti-farm.pattern-detection.small-pool.minimum-unique-players", 5);
        
        this.rapidRepeatedEnabled = config.getBoolean("anti-farm.pattern-detection.rapid-repeated.enabled", true);
        this.rapidTimeThreshold = config.getInt("anti-farm.pattern-detection.rapid-repeated.time-threshold", 60);
        this.rapidRepeatCount = config.getInt("anti-farm.pattern-detection.rapid-repeated.repeat-count", 3);
        
        this.blockStreakGain = config.getBoolean("anti-farm.pattern-detection.action.block-streak-gain", true);
        this.patternLog = config.getBoolean("anti-farm.pattern-detection.action.log", true);
        this.notifyAdmins = config.getBoolean("anti-farm.pattern-detection.action.notify-admins", true);
        
        this.globalMilestoneSounds = config.getBoolean("sounds.global-milestone-sounds", true);
    }
}