package dev.qwerty.streakmaster.manager;

import dev.qwerty.streakmaster.StreakMaster;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class RewardManager {
    
    private final StreakMaster plugin;
    
    public RewardManager(StreakMaster plugin) {
        this.plugin = plugin;
    }
    
    public void checkIntervalReward(Player player, int streak) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("interval-rewards");
        
        if (section == null) {
            return;
        }
        
        for (String key : section.getKeys(false)) {
            String[] range = key.split("-");
            
            if (range.length != 2) {
                continue;
            }
            
            try {
                int min = Integer.parseInt(range[0]);
                int max = Integer.parseInt(range[1]);
                
                if (streak >= min && streak <= max) {
                    int interval = section.getInt(key + ".interval", 5);
                    
                    if (streak % interval == 0) {
                        List<String> commands = section.getStringList(key + ".commands");
                        
                        if (commands != null && !commands.isEmpty()) {
                            plugin.getLogger().info(player.getName() + " için interval reward tetiklendi (streak: " + streak + ")");
                            for (String command : commands) {
                                String finalCommand = command.replace("{player}", player.getName())
                                                            .replace("{streak}", String.valueOf(streak));
                                
                                plugin.getLogger().info("Interval komut: " + finalCommand);
                                Bukkit.getScheduler().runTask(plugin, () -> 
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand)
                                );
                            }
                        }
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }
    }
}