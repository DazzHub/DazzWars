package com.dazzhub.skywars.Commands.sub.arena;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Enable implements subCommand {

    private Main main;

    public Enable(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("enable", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        if (args.length == 2) {
            Player p = (Player) sender;
            String nameArena = args[1];

            main.getArenaManager().enableArena(nameArena);
            p.sendMessage(c("&a&l\u2714 &fArena &e" + nameArena + "&f was activated"));

        } else {
            sender.sendMessage(this.help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw enable <name> &8>&f Active arena");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
