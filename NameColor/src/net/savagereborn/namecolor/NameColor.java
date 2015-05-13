package net.savagereborn.namecolor;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NameColor extends JavaPlugin implements Listener {
	
	public void onEnable() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
		     saveDefaultConfig();
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			loadChatColor(player);
		}
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		loadChatColor(event.getPlayer());
	}
	
	public void loadChatColor(Player player) {
		String chatColor = this.getConfig().getString("chatcolor." + player.getUniqueId());
		if (chatColor != null) {
			player.setDisplayName(ChatColor.valueOf(chatColor) + player.getName() + ChatColor.WHITE);
		}
	}
	
	public void setChatColor(Player player, ChatColor chatColor) {
		player.setDisplayName(chatColor + "" + player.getName() + ChatColor.WHITE);
		this.getConfig().set("chatcolor." + player.getUniqueId(), chatColor.name());
		this.saveConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("setNameColor")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("namecolor.setcolor") || player.isOp()) {
					ChatColor chatColor;
					try {
						chatColor = ChatColor.valueOf(args[0].toUpperCase());
					} catch (Exception e) {
						player.sendMessage(ChatColor.DARK_RED + "Invalid color!");
						return true;
					}
					setChatColor(player, chatColor);
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Your name color has been set to " + ChatColor.BOLD + chatColor + args[0] + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "!");
				} else {
					player.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
				}
			}
		}
		return true;
	}
	
}
