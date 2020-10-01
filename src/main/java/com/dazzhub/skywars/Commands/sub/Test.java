package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.comparables.comparator;
import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.ballons.Ballons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Test implements subCommand {

    private Main main;
    private Ballons ballons = new Ballons();

    public Test(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("test", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        Player p = (Player) sender;
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (args.length == 2){
            if (args[1].equalsIgnoreCase("spawn")){
                ballons.spawn();
            } else if (args[1].equalsIgnoreCase("remove")){
                ballons.delete();
            } else if (args[1].equalsIgnoreCase("soul")){
                main.getSoulManager().preOpen(gamePlayer, "COMMON", 50);
            }
        }
    }

    @Override
    public String help(CommandSender sender) {
        return "";
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
