package com.dazzhub.skywars.Commands.others;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.comparables.comparator;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JoinArena implements CommandExecutor {

    private Main main;

    public JoinArena(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("join")) {
            if (!(sender instanceof Player)) return true;

            Player p = (Player) sender;
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            if (gamePlayer == null) return true;

            if (args.length >= 1) {
                String subCmd = args[0];
                if (subCmd.equalsIgnoreCase("SOLO")) {

                    if (gamePlayer.isInArena()) {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame", "Error Messages.alredyInGame"));
                        return true;
                    }

                    comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                    Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                            .filter(arena -> arena.getMode().equals(Enums.Mode.SOLO) && !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                            .findAny().orElse(null);

                    if (arenaTo == null) {
                        return true;
                    }

                    JoinEvent joinEvent = new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND);
                    Bukkit.getPluginManager().callEvent(joinEvent);

                } else if (subCmd.equalsIgnoreCase("TEAM")) {
                    if (gamePlayer.isInArena()) {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame", "Error Messages.alredyInGame"));
                        return true;
                    }

                    comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                    Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                            .filter(arena -> arena.getMode().equals(Enums.Mode.TEAM) && !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                            .findAny().orElse(null);

                    if (arenaTo == null) {
                        return true;
                    }

                    Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));
                } else if (subCmd.equalsIgnoreCase("RANKED")) {
                    if (gamePlayer.isInArena()) {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame", "Error Messages.alredyInGame"));
                        return true;
                    }

                    comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                    Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                            .filter(arena -> arena.getMode().equals(Enums.Mode.RANKED) && !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                            .findAny().orElse(null);

                    if (arenaTo == null) {
                        return true;
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
                } else {
                    if (gamePlayer.isInArena()) {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame", "Error Messages.alredyInGame"));
                        return true;
                    }

                    if (main.getArenaManager().getArenas().containsKey(subCmd)){
                        Arena arena = main.getArenaManager().getArenas().get(subCmd);
                        if (arena.checkUsable()){
                            Bukkit.getPluginManager().callEvent(new JoinEvent(p, arena, Enums.JoinCause.COMMAND));
                        }
                    }
                }
            } else {
                sender.sendMessage(help());
            }
        }
        return false;
    }

    public String help() {
        return c("&e/Sw join <arena>/solo/team &8>&f Join arena");
    }

    public String c(String msg){ return ChatColor.translateAlternateColorCodes('&', msg); }

}
