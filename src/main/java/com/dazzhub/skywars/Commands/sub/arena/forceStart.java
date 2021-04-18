package com.dazzhub.skywars.Commands.sub.arena;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class forceStart implements subCommand {

    private Main main;

    public forceStart(Main main) {
        this.main = main;

        adminCmd.subCommandHashMap.put("start", this);
        adminCmd.subCommandHashMap.put("forcestart", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)){
            return;
        }

        if (!sender.hasPermission("skywars.forcestart")) {
            return;
        }

        Player p = (Player) sender;
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        if (gamePlayer == null) return;
        if (gamePlayer.getArena() == null) return;

        Arena arena = gamePlayer.getArena();

        if (arena.getGameStatus().equals(Enums.GameStatus.STARTING) || arena.getGameStatus().equals(Enums.GameStatus.WAITING) && !arena.isForceStart() && arena.checkStart()){
            arena.setForceStart(true);
        }
    }

    @Override
    public String help(CommandSender sender) {
        return c("/sw forcestart");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}