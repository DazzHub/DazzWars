package com.dazzhub.skywars.Commands.sub.arena.config;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addSpawn implements subCommand {

    private Main main;

    public addSpawn(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("addspawn", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)){
            return;
        }

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        Player p = (Player) sender;

        if (args.length == 2) {
            main.getArenaManager().addSpawn(p, null, args[1], false);
        } else {
           p.sendMessage(help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw addspawn <arena> &8>&f add spawn");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
