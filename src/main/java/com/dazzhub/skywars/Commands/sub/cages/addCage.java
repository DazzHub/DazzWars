package com.dazzhub.skywars.Commands.sub.cages;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Cuboid;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addCage implements subCommand {

    private Main main;

    public addCage(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("addcage", this);
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
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            String name = args[1];
            String mode = args[2];

            if (gamePlayer.getCage1() == null || gamePlayer.getCage2() == null) return;

            main.getCageManager().createCage(name, mode, new Cuboid(gamePlayer.getCage1(), gamePlayer.getCage2()));
        } else {
            sender.sendMessage(help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw addcage <name> <solo/team> &8>&f Add cages");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
