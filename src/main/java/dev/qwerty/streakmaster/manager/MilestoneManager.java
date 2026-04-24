package dev.qwerty.streakmaster.manager;

import dev.qwerty.streakmaster.StreakMaster;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class MilestoneManager {
    
    private final StreakMaster plugin;
    
    public MilestoneManager(StreakMaster plugin) {
        this.plugin = plugin;
    }
    
    public void checkMilestone(Player player, int streak) {
        ConfigurationSection milestones = plugin.getConfig().getConfigurationSection("milestones");
        
        if (milestones == null) {
            plugin.getLogger().warning("Milestones config section bulunamadı!");
            return;
        }
        
        String streakKey = String.valueOf(streak);
        
        if (!milestones.contains(streakKey)) {
            return;
        }
        
        plugin.getLogger().info(player.getName() + " oyuncusu " + streak + " milestone'a ulaştı!");
        
        boolean broadcast = milestones.getBoolean(streakKey + ".broadcast", false);
        
        plugin.getMessageManager().sendMilestoneMessage(player, streak, broadcast);
        
        List<String> commands = milestones.getStringList(streakKey + ".commands");
        
        if (commands != null && !commands.isEmpty()) {
            plugin.getLogger().info("Milestone için " + commands.size() + " komut çalıştırılıyor...");
            for (String command : commands) {
                String finalCommand = command.replace("{player}", player.getName())
                                            .replace("{streak}", String.valueOf(streak));
                
                plugin.getLogger().info("Komut çalıştırılıyor: " + finalCommand);
                Bukkit.getScheduler().runTask(plugin, () -> 
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand)
                );
            }
        } else {
            plugin.getLogger().info(streakKey + " milestone'ı için komut tanımlanmamış.");
        }
    }
}