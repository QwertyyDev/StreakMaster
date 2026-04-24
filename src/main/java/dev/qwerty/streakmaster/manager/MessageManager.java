package dev.qwerty.streakmaster.manager;

import com.cryptomorin.xseries.XSound;
import dev.qwerty.streakmaster.StreakMaster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {
    
    private final StreakMaster plugin;
    
    public MessageManager(StreakMaster plugin) {
        this.plugin = plugin;
    }
    
    public void sendKillMessage(Player player, int streak, int record) {
        String message = plugin.getConfig().getString("messages.kill.message", "");
        message = message.replace("{streak}", String.valueOf(streak));
        message = message.replace("{record}", String.valueOf(record));
        message = message.replace("{player}", player.getName());
        
        player.sendMessage(colorize(message));
        
        String soundName = plugin.getConfig().getString("messages.kill.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
        double volume = plugin.getConfig().getDouble("messages.kill.volume", 0.5);
        double pitch = plugin.getConfig().getDouble("messages.kill.pitch", 1.5);
        
        playSound(player, soundName, volume, pitch);
    }
    
    public void sendDeathMessage(Player player, int record) {
        String message = plugin.getConfig().getString("messages.death.message", "");
        message = message.replace("{record}", String.valueOf(record));
        message = message.replace("{player}", player.getName());
        
        player.sendMessage(colorize(message));
        
        String soundName = plugin.getConfig().getString("messages.death.sound", "ENTITY_VILLAGER_NO");
        double volume = plugin.getConfig().getDouble("messages.death.volume", 0.5);
        double pitch = plugin.getConfig().getDouble("messages.death.pitch", 0.8);
        
        playSound(player, soundName, volume, pitch);
    }
    
    public void sendStreakEndedMessage(Player killer, Player victim, int streak) {
        boolean broadcast = plugin.getConfig().getBoolean("messages.ended.broadcast", true);
        String message = plugin.getConfig().getString("messages.ended.message", "");
        message = message.replace("{killer}", killer.getName());
        message = message.replace("{victim}", victim.getName());
        message = message.replace("{streak}", String.valueOf(streak));
        
        if (broadcast) {
            Bukkit.broadcastMessage(colorize(message));
        }
        
        String soundName = plugin.getConfig().getString("messages.ended.sound", "ENTITY_LIGHTNING_BOLT_IMPACT");
        double volume = plugin.getConfig().getDouble("messages.ended.volume", 0.3);
        double pitch = plugin.getConfig().getDouble("messages.ended.pitch", 1.2);
        
        if (broadcast) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                playSound(online, soundName, volume, pitch);
            }
        }
    }
    
    public void sendMilestoneMessage(Player player, int milestone, boolean broadcast) {
        String path = "milestones." + milestone + ".message";
        String message = plugin.getConfig().getString(path, "");
        message = message.replace("{player}", player.getName());
        message = message.replace("{streak}", String.valueOf(milestone));
        
        if (broadcast) {
            Bukkit.broadcastMessage(colorize(message));
        } else {
            player.sendMessage(colorize(message));
        }
        
        String soundPath = "milestones." + milestone + ".sound";
        String soundName = plugin.getConfig().getString(soundPath, "ENTITY_PLAYER_LEVELUP");
        double volume = plugin.getConfig().getDouble("milestones." + milestone + ".volume", 1.0);
        double pitch = plugin.getConfig().getDouble("milestones." + milestone + ".pitch", 1.0);
        
        if (broadcast && plugin.getConfigManager().isGlobalMilestoneSounds()) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                playSound(online, soundName, volume, pitch);
            }
        } else {
            playSound(player, soundName, volume, pitch);
        }
    }
    
    public void sendAntiFarmNotification(String type, Player player, String pattern) {
        String message = plugin.getConfig().getString("messages.anti-farm." + type, "");
        message = message.replace("{player}", player.getName());
        message = message.replace("{pattern}", pattern);
        
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("streakmaster.admin")) {
                online.sendMessage(colorize(message));
            }
        }
        
        Bukkit.getConsoleSender().sendMessage(colorize(message));
    }
    
    public String getMessage(String path) {
        return colorize(plugin.getConfig().getString("messages." + path, ""));
    }
    
    public String getMessage(String path, String placeholder, String value) {
        String message = plugin.getConfig().getString("messages." + path, "");
        message = message.replace(placeholder, value);
        return colorize(message);
    }
    
    public String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    private void playSound(Player player, String soundName, double volume, double pitch) {
        XSound.matchXSound(soundName).ifPresent(sound -> 
            sound.play(player, (float) volume, (float) pitch)
        );
    }
}