package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.events.border.Border;
import com.dazzhub.skywars.Utils.events.border.eventBorder;
import com.dazzhub.skywars.Utils.events.dragon.Dragon;
import com.dazzhub.skywars.Utils.events.dragon.eventDragon;
import com.dazzhub.skywars.Utils.events.dropParty.dropParty;
import com.dazzhub.skywars.Utils.events.strom.Storm;
import com.dazzhub.skywars.Utils.events.strom.eventStorm;
import com.dazzhub.skywars.Utils.events.tntfall.TNTFall;
import com.dazzhub.skywars.Utils.events.tntfall.eventTNT;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class testEvents implements subCommand {

    private Main main;

    public testEvents(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("test", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        if (args.length == 2){
            Player p = (Player) sender;
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
            String type = args[1];

            switch (type){
                case "1":{
                    eventBorder e = new Border(gamePlayer.getArena());
                    e.startEvent();
                    break;
                }
                case "2":{
                    eventDragon e = new Dragon(gamePlayer.getArena());
                    e.startEvent();

                    Bukkit.getScheduler().runTaskLater(main, e::killDragon,60*20);
                    break;
                }
                case "3":{
                    dropParty e = new dropParty(gamePlayer.getArena());
                    e.startEvent();
                    break;
                }
                case "4":{
                    eventStorm e = new Storm(gamePlayer.getArena());
                    e.startEvent();
                    break;
                }
                case "5":{
                    eventTNT e = new TNTFall(gamePlayer.getArena());
                    e.startEvent();
                    break;
                }
            }
        }

        sender.sendMessage(c("&a&l\u2714 &fSending.."));
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/sw test <type> &8>&f test event");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
