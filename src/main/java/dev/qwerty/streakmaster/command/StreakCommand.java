package dev.qwerty.streakmaster.command;

import dev.qwerty.streakmaster.StreakMaster;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StreakCommand implements CommandExecutor {
    
    private final StreakMaster plugin;
    
    public StreakCommand(StreakMaster plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("streakmaster.admin")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.usage"));
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "add":
                return handleAdd(sender, args);
            case "remove":
                return handleRemove(sender, args);
            case "set":
                return handleSet(sender, args);
            case "reset":
                return handleReset(sender, args);
            case "addrecord":
                return handleAddRecord(sender, args);
            case "removerecord":
                return handleRemoveRecord(sender, args);
            case "reload":
                return handleReload(sender);
            default:
                sender.sendMessage(plugin.getMessageManager().getMessage("command.usage"));
                return true;
        }
    }
    
    private boolean handleAdd(CommandSender sender, String[] args) {
        if (!sender.hasPermission("streakmaster.command.add")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.no-permission"));
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.usage"));
            return true;
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.player-not-found"));
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.invalid-amount"));
            return true;
        }
        
        plugin.getStreakManager().addStreak(target.getUniqueId(), amount);
        
        String message = plugin.getMessageManager().getMessage("command.streak-added");
        message = message.replace("{player}", target.getName());
        message = message.replace("{amount}", String.valueOf(amount));
        sender.sendMessage(message);
        
        return true;
    }
    
    private boolean handleRemove(CommandSender sender, String[] args) {
        if (!sender.hasPermission("streakmaster.command.remove")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.no-permission"));
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.usage"));
            return true;
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.player-not-found"));
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.invalid-amount"));
            return true;
        }
        
        plugin.getStreakManager().removeStreak(target.getUniqueId(), amount);
        
        String message = plugin.getMessageManager().getMessage("command.streak-removed");
        message = message.replace("{player}", target.getName());
        message = message.replace("{amount}", String.valueOf(amount));
        sender.sendMessage(message);
        
        return true;
    }
    
    private boolean handleSet(CommandSender sender, String[] args) {
        if (!sender.hasPermission("streakmaster.command.set")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.no-permission"));
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.usage"));
            return true;
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.player-not-found"));
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.invalid-amount"));
            return true;
        }
        
        plugin.getStreakManager().setStreak(target.getUniqueId(), amount);
        
        String message = plugin.getMessageManager().getMessage("command.streak-set");
        message = message.replace("{player}", target.getName());
        message = message.replace("{amount}", String.valueOf(amount));
        sender.sendMessage(message);
        
        return true;
    }
    
    private boolean handleReset(CommandSender sender, String[] args) {
        if (!sender.hasPermission("streakmaster.command.reset")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.no-permission"));
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.usage"));
            return true;
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.player-not-found"));
            return true;
        }
        
        plugin.getStreakManager().resetStreak(target.getUniqueId());
        
        String message = plugin.getMessageManager().getMessage("command.streak-reset");
        message = message.replace("{player}", target.getName());
        sender.sendMessage(message);
        
        return true;
    }
    
    private boolean handleAddRecord(CommandSender sender, String[] args) {
        if (!sender.hasPermission("streakmaster.command.addrecord")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.no-permission"));
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.usage"));
            return true;
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.player-not-found"));
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.invalid-amount"));
            return true;
        }
        
        plugin.getStreakManager().addRecord(target.getUniqueId(), amount);
        
        String message = plugin.getMessageManager().getMessage("command.record-added");
        message = message.replace("{player}", target.getName());
        message = message.replace("{amount}", String.valueOf(amount));
        sender.sendMessage(message);
        
        return true;
    }
    
    private boolean handleRemoveRecord(CommandSender sender, String[] args) {
        if (!sender.hasPermission("streakmaster.command.removerecord")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.no-permission"));
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.usage"));
            return true;
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.player-not-found"));
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.invalid-amount"));
            return true;
        }
        
        plugin.getStreakManager().removeRecord(target.getUniqueId(), amount);
        
        String message = plugin.getMessageManager().getMessage("command.record-removed");
        message = message.replace("{player}", target.getName());
        message = message.replace("{amount}", String.valueOf(amount));
        sender.sendMessage(message);
        
        return true;
    }
    
    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("streakmaster.command.reload")) {
            sender.sendMessage(plugin.getMessageManager().getMessage("command.no-permission"));
            return true;
        }
        
        plugin.reload();
        sender.sendMessage(plugin.getMessageManager().getMessage("command.reload-success"));
        
        return true;
    }
}