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

            if (gamePlayer.isInArena()) {
                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame", "Error Messages.alredyInGame"));
                return true;
            }

            if (args.length >= 1) {
                String subCmd = args[0];
                if (subCmd.equalsIgnoreCase("SOLO")) {
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
                    comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                    Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                            .filter(arena -> arena.getMode().equals(Enums.Mode.TEAM) && !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                            .findAny().orElse(null);

                    if (arenaTo == null) {
                        return true;
                    }

                    Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));
                }
            } else {
                comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                        .filter(arena -> !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                        .findAny().orElse(null);

                if (arenaTo == null) {
                    return true;
                }

                Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));
            }
        }
        return false;
    }

    public String c(String msg){ return ChatColor.translateAlternateColorCodes('&', msg); }

}
