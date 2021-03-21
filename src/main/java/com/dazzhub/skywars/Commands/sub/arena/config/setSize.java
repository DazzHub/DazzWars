package com.dazzhub.skywars.Commands.sub.arena.config;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setSize implements subCommand {

    private Main main;

    public setSize(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("setsize", this);
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

        if (args.length == 3) {

            if (!Tools.isInteger(args[2])){
                p.sendMessage(help(sender));
                return;
            }

            int size = Integer.parseInt(args[2]);

            main.getArenaManager().setSize(p,args[1], size);
        } else {
            p.sendMessage(help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw setsize <arena> <amount> &8>&f set size");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
