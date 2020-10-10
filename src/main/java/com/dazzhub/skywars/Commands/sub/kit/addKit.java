package com.dazzhub.skywars.Commands.sub.kit;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addKit implements subCommand {

    private Main main;

    public addKit(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("addkit", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        Player p = (Player) sender;

        if (args.length == 3) {
            main.getKitManager().createKit(p, args[1], args[2].toLowerCase());
        } else {
            p.sendMessage(help(sender));
        }
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw addkit <name> <solo/team> &8>&f add kit");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
