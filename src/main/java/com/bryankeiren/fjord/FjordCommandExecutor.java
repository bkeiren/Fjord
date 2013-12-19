package com.bryankeiren.fjord;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FjordCommandExecutor implements CommandExecutor {
	private Fjord m_Plugin;

	public FjordCommandExecutor(Fjord plugin) {
		m_Plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Check to see if we were passed any arguments.
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("version")) {
				if (sender.hasPermission("fjord.version")) {
					sender.sendMessage("Fjord v" + m_Plugin.getDescription().getVersion());
					return true;
				} else {
					sendMessageNoPermission(sender);
				}
			}
		}
		// No arguments, so we should display a helpful message to let the player know what sort of commands
		// are accepted by Fjord.
		else {
			sender.sendMessage("You have to specify a command for Fjord to execute.");
			return true;
		}

		return false;
	}

	private void sendMessageNoPermission(CommandSender sender) {
		sender.sendMessage("You do not have permission to use this command.");
	}
}
