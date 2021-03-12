package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class editMode implements subCommand {

    private Main main;

    public editMode(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("editmode", this);
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
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        if (gamePlayer.isEditMode()){
            gamePlayer.setEditMode(false);
            p.sendMessage(c("&a&l\u2714 &fEdit mode &9OFF"));
        } else {
            gamePlayer.setEditMode(true);
            p.sendMessage(c("&a&l\u2714 &fEdit mode &9ON"));
        }
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/sw editMode &8>&f Edit mode");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
