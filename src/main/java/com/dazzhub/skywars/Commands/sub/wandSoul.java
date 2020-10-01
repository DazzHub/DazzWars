package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class wandSoul implements subCommand {

    private Main main;

    public wandSoul(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("wands", this);
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

        p.getInventory().addItem(main.getItemsCustom().getSoulWand());

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw wands &8>&f Mark location for soulwell");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}