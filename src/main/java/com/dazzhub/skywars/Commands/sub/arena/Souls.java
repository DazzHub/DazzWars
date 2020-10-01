package com.dazzhub.skywars.Commands.sub.arena;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Souls implements subCommand {

    private Main main;

    public Souls(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("souls", this);
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

                    GamePlayer gameTarget = main.getPlayerManager().getPlayer(target.getUniqueId());
                    gameTarget.addSouls(Integer.parseInt(args[3]));

                    sender.sendMessage(c("&a&l\u2714 &fSouls added to: &9" + target.getName()));
                    target.sendMessage(c(gameTarget.getLangMessage().getString("Messages.SoulWell.Added")).replace("%amount%", args[3]));

                    XSound.play(p, String.valueOf(XSound.ENTITY_CHICKEN_EGG.parseSound()));
                }
                else if (args[1].equalsIgnoreCase("set")) {
                    main.getPlayerManager().getPlayer(target.getUniqueId()).setSouls(Integer.parseInt(args[3]));
                    sender.sendMessage(c("&a&l\u2714 &fSouls established to: &9" + target.getName()));
                    XSound.play(p, String.valueOf(XSound.ENTITY_CHICKEN_EGG.parseSound()));
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
        return c("/sw souls add/set &e<player>&a <amount>");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}