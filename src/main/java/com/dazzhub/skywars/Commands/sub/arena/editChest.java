package com.dazzhub.skywars.Commands.sub.arena;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class editChest implements subCommand {

    private Main main;

    public editChest(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("editchest", this);
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
            String file = args[1];
            String chance = args[2];

            if (!Tools.isInteger(chance)) {
                p.sendMessage(help(sender));
                return;
            }

            main.getChestManager().editChest(file, Integer.parseInt(chance), p);
        } else {
            p.sendMessage(help(sender));
        }
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/sw editchest <file> <chance> &8>&f edit chest");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}