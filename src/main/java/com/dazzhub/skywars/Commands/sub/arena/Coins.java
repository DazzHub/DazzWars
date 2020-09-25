package com.dazzhub.skywars.Commands.sub.arena;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Coins implements subCommand {

    private Main main;

    public Coins(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("coins", this);
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

        if (args.length > 3) {
            Player target = Bukkit.getPlayer(args[2]);
            if (target != null) {
                if (args[1].equalsIgnoreCase("add")) {
                    main.getPlayerManager().getPlayer(target.getUniqueId()).addCoins(Integer.parseInt(args[3]));
                    sender.sendMessage(c("&a&l\u2714 &fCoins added to: &9" + target.getName()));
                    XSound.playSoundFromString(p, String.valueOf(XSound.ENTITY_CHICKEN_EGG.parseSound()));
                }
                else if (args[1].equalsIgnoreCase("set")) {
                    main.getPlayerManager().getPlayer(target.getUniqueId()).setCoins(Integer.parseInt(args[3]));
                    sender.sendMessage(c("&a&l\u2714 &fCoins established to: &9" + target.getName()));
                    XSound.playSoundFromString(p, String.valueOf(XSound.ENTITY_CHICKEN_EGG.parseSound()));
                }
            } else {
                sender.sendMessage(c("&c&l\u2718 &fError name"));
            }
        } else {
            p.sendMessage(help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("/sw coins add/set &e<player>&a <amount>");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
