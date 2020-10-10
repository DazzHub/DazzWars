package com.dazzhub.skywars.Commands.sub.arena.config;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setMin implements subCommand {

    private Main main;

    public setMin(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("setmin", this);
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
            if (!Tools.isInteger(args[1])) {
                p.sendMessage(help(sender));
                return;
            }
            main.getArenaManager().minPlayer(p, Integer.parseInt(args[1]), args[2]);
        } else {
            p.sendMessage(help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw setmin <amount> <arena> &8>&f set min players");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
