package dev.qwerty.streakmaster.manager;

import dev.qwerty.streakmaster.StreakMaster;
import dev.qwerty.streakmaster.data.KillRecord;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AntiFarmManager {
    
    private final StreakMaster plugin;
    private final Map<UUID, List<KillRecord>> killHistory;
    private final Map<UUID, UUID> lastKilledPlayer;
    private final Map<UUID, Map<UUID, Long>> rapidKillTracker;
    
    public AntiFarmManager(StreakMaster plugin) {
        this.plugin = plugin;
        this.killHistory = new ConcurrentHashMap<>();
        this.lastKilledPlayer = new ConcurrentHashMap<>();
        this.rapidKillTracker = new ConcurrentHashMap<>();
    }
    
    public boolean shouldBlockKill(Player killer, Player victim) {
        if (checkSameIP(killer, victim)) {
            return true;
        }
        
        if (checkSamePlayer(killer, victim)) {
            return true;
        }
        
        if (plugin.getConfigManager().isPatternDetectionEnabled()) {
            if (checkAlternatingKills(killer, victim)) {
                return true;
            }
            
            if (checkSmallPool(killer, victim)) {
                return true;
            }
            
            if (checkRapidRepeated(killer, victim)) {
                return true;
            }
        }
        
        recordKill(killer, victim);
        
        return false;
    }
    
    private boolean checkSameIP(Player killer, Player victim) {
        if (!plugin.getConfigManager().isSameIpEnabled()) {
            return false;
        }
        
        String killerIP = killer.getAddress().getAddress().getHostAddress();
        String victimIP = victim.getAddress().getAddress().getHostAddress();
        
        if (killerIP.equals(victimIP)) {
            if (plugin.getConfigManager().isSameIpLog()) {
                plugin.getMessageManager().sendAntiFarmNotification("same-ip", killer, "");
            }
            return true;
        }
        
        return false;
    }
    
    private boolean checkSamePlayer(Player killer, Player victim) {
        if (!plugin.getConfigManager().isSamePlayerEnabled()) {
            return false;
        }
        
        UUID lastKilled = lastKilledPlayer.get(killer.getUniqueId());
        
        if (lastKilled != null && lastKilled.equals(victim.getUniqueId())) {
            if (plugin.getConfigManager().isSamePlayerLog()) {
                plugin.getMessageManager().sendAntiFarmNotification("same-player", killer, "");
            }
            return true;
        }
        
        lastKilledPlayer.put(killer.getUniqueId(), victim.getUniqueId());
        return false;
    }
    
    private boolean checkAlternatingKills(Player killer, Player victim) {
        if (!plugin.getConfigManager().isAlternatingKillsEnabled()) {
            return false;
        }
        
        List<KillRecord> history = killHistory.getOrDefault(killer.getUniqueId(), new ArrayList<>());
        
        long currentTime = System.currentTimeMillis();
        int timeWindow = plugin.getConfigManager().getAlternatingTimeWindow() * 1000;
        int threshold = plugin.getConfigManager().getAlternatingThreshold();
        
        int alternatingCount = 0;
        UUID lastVictimUUID = null;
        
        for (int i = history.size() - 1; i >= 0; i--) {
            KillRecord record = history.get(i);
            
            if (currentTime - record.getTimestamp() > timeWindow) {
                break;
            }
            
            if (lastVictimUUID != null && !lastVictimUUID.equals(record.getVictimUUID())) {
                alternatingCount++;
            }
            
            lastVictimUUID = record.getVictimUUID();
        }
        
        if (alternatingCount >= threshold) {
            if (plugin.getConfigManager().isPatternLog()) {
                plugin.getMessageManager().sendAntiFarmNotification("pattern-detected", killer, "Alternating Kills");
            }
            if (plugin.getConfigManager().isNotifyAdmins()) {
                plugin.getMessageManager().sendAntiFarmNotification("pattern-detected", killer, "Alternating Kills");
            }
            return plugin.getConfigManager().isBlockStreakGain();
        }
        
        return false;
    }
    
    private boolean checkSmallPool(Player killer, Player victim) {
        if (!plugin.getConfigManager().isSmallPoolEnabled()) {
            return false;
        }
        
        List<KillRecord> history = killHistory.getOrDefault(killer.getUniqueId(), new ArrayList<>());
        int checkLastKills = plugin.getConfigManager().getCheckLastKills();
        int minimumUnique = plugin.getConfigManager().getMinimumUniquePlayersInPool();
        
        if (history.size() < checkLastKills) {
            return false;
        }
        
        Set<UUID> uniqueVictims = new HashSet<>();
        for (int i = history.size() - 1; i >= Math.max(0, history.size() - checkLastKills); i--) {
            uniqueVictims.add(history.get(i).getVictimUUID());
        }
        
        if (uniqueVictims.size() < minimumUnique) {
            if (plugin.getConfigManager().isPatternLog()) {
                plugin.getMessageManager().sendAntiFarmNotification("pattern-detected", killer, "Small Pool");
            }
            if (plugin.getConfigManager().isNotifyAdmins()) {
                plugin.getMessageManager().sendAntiFarmNotification("pattern-detected", killer, "Small Pool");
            }
            return plugin.getConfigManager().isBlockStreakGain();
        }
        
        return false;
    }
    
    private boolean checkRapidRepeated(Player killer, Player victim) {
        if (!plugin.getConfigManager().isRapidRepeatedEnabled()) {
            return false;
        }
        
        Map<UUID, Long> killerRapidMap = rapidKillTracker.computeIfAbsent(killer.getUniqueId(), k -> new ConcurrentHashMap<>());
        
        long currentTime = System.currentTimeMillis();
        int timeThreshold = plugin.getConfigManager().getRapidTimeThreshold() * 1000;
        
        Long lastKillTime = killerRapidMap.get(victim.getUniqueId());
        
        if (lastKillTime != null && (currentTime - lastKillTime) < timeThreshold) {
            if (plugin.getConfigManager().isPatternLog()) {
                plugin.getMessageManager().sendAntiFarmNotification("pattern-detected", killer, "Rapid Repeated");
            }
            if (plugin.getConfigManager().isNotifyAdmins()) {
                plugin.getMessageManager().sendAntiFarmNotification("pattern-detected", killer, "Rapid Repeated");
            }
            return plugin.getConfigManager().isBlockStreakGain();
        }
        
        killerRapidMap.put(victim.getUniqueId(), currentTime);
        
        return false;
    }
    
    private void recordKill(Player killer, Player victim) {
        List<KillRecord> history = killHistory.computeIfAbsent(killer.getUniqueId(), k -> new ArrayList<>());
        history.add(new KillRecord(victim.getUniqueId(), System.currentTimeMillis()));
        
        if (history.size() > 50) {
            history.remove(0);
        }
    }
    
    public void clearPlayerData(UUID uuid) {
        killHistory.remove(uuid);
        lastKilledPlayer.remove(uuid);
        rapidKillTracker.remove(uuid);
    }
}