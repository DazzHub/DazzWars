package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class reload implements subCommand {

    private Main main;

    public reload(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("reload", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        main.getMenuManager().reloadMenu();

        sender.sendMessage(c("&a&l\u2714 &fMenus reloaded"));
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/sw reload &8>&f reload configs");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
