package com.dazzhub.skywars.Commands;

import com.dazzhub.skywars.Commands.sub.arena.Coins;
import com.dazzhub.skywars.Commands.sub.arena.Enable;
import com.dazzhub.skywars.Commands.sub.arena.Join;
import com.dazzhub.skywars.Commands.sub.arena.config.addSpawn;
import com.dazzhub.skywars.Commands.sub.arena.Create;
import com.dazzhub.skywars.Commands.sub.arena.config.setMax;
import com.dazzhub.skywars.Commands.sub.arena.config.setMin;
import com.dazzhub.skywars.Commands.sub.arena.config.setSpectator;
import com.dazzhub.skywars.Commands.sub.cages.addCage;
import com.dazzhub.skywars.Commands.sub.cages.wandCage;
import com.dazzhub.skywars.Commands.sub.changeLanguage;
import com.dazzhub.skywars.Commands.sub.holograms.addHologram;
import com.dazzhub.skywars.Commands.sub.kit.addKit;
import com.dazzhub.skywars.Commands.sub.reload;
import com.dazzhub.skywars.Commands.sub.setLobby;
import com.dazzhub.skywars.Commands.sub.worldTP;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.CenterMessage;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class adminCmd implements CommandExecutor {

    private Main main;
    public static Map<String, subCommand> subCommandHashMap = Maps.newHashMap();

    public adminCmd(Main main) {
        this.main = main;

        /* ARENA */
        new Create(main);
        new addSpawn(main);
        new setSpectator(main);
        new setMax(main);
        new setMin(main);
        new Coins(main);
        new Join(main);
        new Enable(main);

        /* CAGES */
        new addCage(main);
        new wandCage(main);

        /* KITS */
        new addKit(main);

        /* MORE */
        new worldTP(main);
        new setLobby(main);
        new addHologram(main);
        new reload(main);
        new changeLanguage(main);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("skywars") || cmd.getName().equalsIgnoreCase("sw")){

            if (args.length < 1){
                this.helpCmd(sender);
                return true;
            }

            String subcmd = args[0].toLowerCase();

            if (!subCommandHashMap.containsKey(subcmd)) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cmd.error"));
                } else {
                    sender.sendMessage(c("&c&l➥ &fError command"));
                }
                return true;
            }

            subCommandHashMap.get(subcmd).onCommand(sender, cmd, args);
        }
        return false;
    }

    private void helpCmd(CommandSender sender) {

        if (!(sender instanceof Player)) {
            return;
        }

        Player p = (Player) sender;
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (sender.hasPermission("skywars.admin")) {
            List<String> cmd = gamePlayer.getLangMessage().getStringList("Messages.Cmd.swadmin");
            cmd.stream().map(str -> c(str.startsWith("%center%") ? CenterMessage.centerMessage(str.replaceAll("%center%", "")) : str)).forEach(sender::sendMessage);
        } else {
            List<String> cmd = gamePlayer.getLangMessage().getStringList("Messages.Cmd.sw");
            cmd.stream().map(str -> c(str.startsWith("%center%") ? CenterMessage.centerMessage(str.replaceAll("%center%", "")) : str)).forEach(sender::sendMessage);
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
