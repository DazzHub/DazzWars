package com.dazzhub.skywars.Commands.sub.arena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.comparables.comparator;
import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join implements subCommand {

    private Main main;

    public Join(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("join", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        Player p = (Player) sender;
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) return;

        if (args.length == 2) {
            String subCmd = args[1];
            if (subCmd.equalsIgnoreCase("SOLO")) {
                if (gamePlayer.isInArena()) {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame"));
                    return;
                }

                comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                        .filter(arena -> arena.getMode().equals(Enums.Mode.SOLO) && !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                        .findAny().orElse(null);

                if (arenaTo == null) {
                    return;
                }

                JoinEvent joinEvent = new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND);
                Bukkit.getPluginManager().callEvent(joinEvent);

            } else if (subCmd.equalsIgnoreCase("TEAM")) {
                if (gamePlayer.isInArena()) {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame"));
                    return;
                }

                comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                        .filter(arena -> arena.getMode().equals(Enums.Mode.TEAM) && !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                        .findAny().orElse(null);

                if (arenaTo == null) {
                    return;
                }

                Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));
            } else if (subCmd.equalsIgnoreCase("RANKED")) {
                if (gamePlayer.isInArena()) {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame"));
                    return;
                }

                comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                        .filter(arena -> arena.getMode().equals(Enums.Mode.RANKED) && !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                        .findAny().orElse(null);

                if (arenaTo == null) {
                    return;
                }

                Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));
            } else if (subCmd.equalsIgnoreCase("AGAIN")) {
                if (gamePlayer.isInArena()) {
                    comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                    Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                            .filter(Arena::checkUsable)
                            .findAny().orElse(null);

                    if (arenaTo == null) {
                        gamePlayer.getArena().removePlayer(gamePlayer, true);
                    } else {
                        gamePlayer.getArena().removePlayer(gamePlayer, false);
                        Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));
                    }
                }
            } else if (subCmd.equalsIgnoreCase("AUTO")) {
                gamePlayer.setAutomatic(!gamePlayer.isAutomatic());

                comparator.checkArenaPlayer(main.getArenaManager().getArenaList());
                Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                        .filter(Arena::checkUsable)
                        .findAny().orElse(null);
                if (arenaTo == null) {
                    if (gamePlayer.isInArena()) {
                        gamePlayer.getArena().removePlayer(gamePlayer, true);
                    }
                } else {
                    if (gamePlayer.isInArena()) {
                        gamePlayer.getArena().removePlayer(gamePlayer, false);
                    }
                    Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));
                }
            } else {
                if (gamePlayer.isInArena()) {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame"));
                    return;
                }

                if (main.getArenaManager().getArenas().containsKey(subCmd)){
                    Arena arena = main.getArenaManager().getArenas().get(subCmd);
                    if (arena.checkUsable()){
                        Bukkit.getPluginManager().callEvent(new JoinEvent(p, arena, Enums.JoinCause.COMMAND));
                    }
                }
            }
        } else {
            sender.sendMessage(help(sender));
        }
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw join <arena>/solo/team &8>&f Join arena");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
