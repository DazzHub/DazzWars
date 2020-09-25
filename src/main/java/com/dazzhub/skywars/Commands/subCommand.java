package com.dazzhub.skywars.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface subCommand {

    void onCommand(CommandSender sender, Command cmd, String[] args);

    String help(CommandSender sender);
}
