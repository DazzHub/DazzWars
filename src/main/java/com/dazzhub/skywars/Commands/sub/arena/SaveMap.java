package com.dazzhub.skywars.Commands.sub.arena;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveMap implements subCommand {

    private Main main;

    public SaveMap(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("save", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        if (args.length == 3) {
            Player p = (Player) sender;
            String nameArena = args[1];
            String nameWorld = args[2];

            main.getArenaManager().saveArena(p, nameArena, nameWorld);
        } else {
            sender.sendMessage(this.help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw save <name> <world> &8>&f Save map arena");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
