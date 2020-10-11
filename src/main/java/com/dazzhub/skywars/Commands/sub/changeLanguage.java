package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.io.File;

public class changeLanguage implements subCommand {

    private Main main;

    public changeLanguage(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("lang", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)){
            return;
        }

        if (args.length == 2) {
            Player p = (Player) sender;
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
            Configuration lang = gamePlayer.getLangMessage();

            String select = args[1];
            if (!main.getSettings().getStringList("ListLanguage").contains(select)) {
                gamePlayer.sendMessage(lang.getString("Messages.Language.error", "Error Language.error"));
                return;
            }

            gamePlayer.setLang(select);
            gamePlayer.sendMessage(lang.getString("Messages.Language.change", "Error Language.change"));
        } else {
            sender.sendMessage(this.help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/sw lang <language> &8>&f change language");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
