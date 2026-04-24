package dev.qwerty.streakmaster.listener;

import dev.qwerty.streakmaster.StreakMaster;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDeathListener implements Listener {
    
    private final StreakMaster plugin;
    
    public PlayerDeathListener(StreakMaster plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        if (killer == null || killer.equals(victim)) {
            plugin.getStreakManager().resetStreak(victim, null);
            return;
        }
        
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        if (onlinePlayers < plugin.getConfigManager().getMinimumPlayers()) {
            return;
        }
        
        boolean blocked = plugin.getAntiFarmManager().shouldBlockKill(killer, victim);
        
        if (!blocked) {
            plugin.getStreakManager().addKill(killer);
        }
        
        plugin.getStreakManager().resetStreak(victim, killer);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (plugin.getConfigManager().isResetOnQuit()) {
            plugin.getStreakManager().resetStreak(player.getUniqueId());
        }
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getDataManager().saveAllData();
        }, 20L);
    }
}