package com.dazzhub.skywars.Commands.others;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Party implements CommandExecutor {

    private Main main;

    public Party(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("party") || cmd.getName().equalsIgnoreCase("p")) {

            if (!(sender instanceof Player)){
                return true;
            }

            if (args.length < 1) {
                this.sendHelp(sender);
                return false;
            }

            if (!sender.hasPermission("skywars.party")){
                return true;
            }

            Player p = (Player) sender;
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            String subCmd = args[0];
            if (subCmd.equalsIgnoreCase("create")) {
                main.getPartyManager().createParty(gamePlayer);
            } else if (subCmd.equalsIgnoreCase("invite")) {
                if (args.length > 1) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        GamePlayer gamePlayerTarget = main.getPlayerManager().getPlayer(target.getUniqueId());
                        main.getPartyManager().invitePlayer(gamePlayer, gamePlayerTarget);
                    } else {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.TargetNoExist", "Error Party.TargetNoExist").replace("%target%", args[1]));
                    }
                } else {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.InvalidArgument", "Error Party.InvalidArgument"));
                }
            } else if (subCmd.equalsIgnoreCase("join")) {
                if (args.length > 1) {
                    Player owner = Bukkit.getPlayer(args[1]);
                    if (owner != null) {
                        GamePlayer gamePlayerOwner = main.getPlayerManager().getPlayer(owner.getUniqueId());
                        GamePlayer gamePlayerTarget = main.getPlayerManager().getPlayer(p.getUniqueId());
                        main.getPartyManager().acceptParty(gamePlayerOwner, gamePlayerTarget);
                    } else {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.TargetNoExist", "Error Party.TargetNoExist").replace("%target%", args[1]));
                    }
                } else {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.InvalidArgument", "Error Party.InvalidArgument"));
                }
            } else if (subCmd.equalsIgnoreCase("kick")) {
                if (args.length > 1) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        GamePlayer gamePlayerTarget = main.getPlayerManager().getPlayer(target.getUniqueId());
                        main.getPartyManager().kickParty(gamePlayer, gamePlayerTarget);
                    } else {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.TargetNoExist", "Error Party.TargetNoExist").replace("%target%", args[1]));
                    }
                } else {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.InvalidArgument", "Error Party.InvalidArgument"));
                }
            } else if (subCmd.equalsIgnoreCase("leave")) {
                main.getPartyManager().leaveParty(gamePlayer);
            } else if (subCmd.equalsIgnoreCase("disband") || subCmd.equalsIgnoreCase("delete")) {
                main.getPartyManager().removeParty(gamePlayer);
            }

        }
        return false;
    }

    private void sendHelp(CommandSender sender) {
        Player p = (Player) sender;
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        gamePlayer.sendMessage(gamePlayer.getLangMessage().getStringList("Messages.Cmd.party"));
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}